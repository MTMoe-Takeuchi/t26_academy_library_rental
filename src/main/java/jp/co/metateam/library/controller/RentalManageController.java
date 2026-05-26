package jp.co.metateam.library.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.model.Stock;
import jp.co.metateam.library.service.AccountService;
import jp.co.metateam.library.service.RentalManageService;
import jp.co.metateam.library.service.StockService;
import jp.co.metateam.library.values.RentalStatus;
import lombok.extern.log4j.Log4j2;

/**
 * 貸出管理関連クラスß
 */
@Log4j2
@Controller
public class RentalManageController {
    // ここから追加
    // 以下の値は変わらない宣言
    private final AccountService accountService;
    private final StockService stockService;
    private final RentalManageService rentalManageService;

    // データベース処理を行うクラスを自動で用意する
    @Autowired
    public RentalManageController(AccountService accountService, StockService stockService,
            RentalManageService rentalManageService) {
        // 受け取ったServiceをクラス変数に代入
        this.accountService = accountService;
        this.stockService = stockService;
        this.rentalManageService = rentalManageService;
    }
    // ここまで追加

    /**
     * 貸出一覧画面初期表示
     * 
     * @param model
     * @return
     */
    @GetMapping("/rental/index")
    public String index(Model model) {
        // 貸出管理テーブルから全件取得
        List<jp.co.metateam.library.model.RentalManage> rentalManagesList = this.rentalManageService.findAll();
        // 貸出一覧画面に渡すデータをmodelに追加
        model.addAttribute("rentalManageList", rentalManagesList);
        // 貸出一覧画面に遷移
        return "rental/index";
    }

    // 貸出登録画面初期表示
    // ここから追加
    @GetMapping("/rental/add")
    public String add(Model model) {
        // プルダウン用のデータを取得
        List<Account> accounts = this.accountService.findAll();
        List<Stock> stockList = this.stockService.findStockAvailableAll();

        // 貸出一覧画面にデータを渡すデータをmodelに追加
        model.addAttribute("accounts", accounts);
        model.addAttribute("stockList", stockList);

        // 入力フォームの箱を準備する
        model.addAttribute("rentalManageDto", new RentalManageDto());

        // ステータス一覧
        model.addAttribute("rentalStatus", RentalStatus.values());
        if (!model.containsAttribute("rentalManageDto")) {
            model.addAttribute("rentalManageDto", new RentalManageDto());
        }
        // 貸出登録画面へ遷移
        return "rental/add";
    }

    @PostMapping("/rental/add")
    public String register(@Valid @ModelAttribute RentalManageDto rentalManageDto, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        // Serviceで保存処理とエラーチェックを実行
        this.rentalManageService.save(rentalManageDto, result);

        // もしエラーが1つでもあれば登録画面に戻す
        if (result.hasErrors()) {
            // 画面に戻す際、プルダウンに再度データを渡す
            model.addAttribute("accounts", this.accountService.findAll());
            model.addAttribute("stockList", this.stockService.findStockAvailableAll());
            model.addAttribute("rentalStatus", RentalStatus.values());
            return "rental/add";
        }
        if (rentalManageDto.getExpectedRentalOn() != null
                && rentalManageDto.getExpectedReturnOn() != null
                && rentalManageDto.getExpectedRentalOn().isEqual(rentalManageDto.getExpectedReturnOn())) {

            result.rejectValue("expectedReturnOn", null, null);
        }

        // エラーがなければ一覧画面へ移動
        redirectAttributes.addFlashAttribute("successMessage", "貸出情報を登録しました");
        return "redirect:/rental/index";
    }
}