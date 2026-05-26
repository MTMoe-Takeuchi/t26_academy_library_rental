package jp.co.metateam.library.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.model.Stock;
import jp.co.metateam.library.repository.AccountRepository;
import jp.co.metateam.library.repository.RentalManageRepository;
import jp.co.metateam.library.repository.StockRepository;

@Service
public class RentalManageService {
    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final RentalManageRepository rentalManageRepository;

    @Autowired
    public RentalManageService(RentalManageRepository rentalManageRepository, AccountRepository accountRepository,
            StockRepository stockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.rentalManageRepository = rentalManageRepository;
    }

    // 貸出登録のバリデーションおよび保存処理
    @Transactional
    public void save(RentalManageDto rentalManageDto, BindingResult result) {

        // データベースから情報を取得して存在チェック
        Account account = this.accountRepository.findByEmployeeId(rentalManageDto.getEmployeeId()).orElse(null);
        Stock stock = this.stockRepository.findById(rentalManageDto.getStockId()).orElse(null);

        // ここでエラーがあれば戻る
        if (result.hasErrors())
            return;

        // バリデーションチェック
        LocalDate rentalDate = rentalManageDto.getExpectedRentalOn();
        LocalDate returnDate = rentalManageDto.getExpectedReturnOn();

        // 貸出予定日 ≦ 返却予定日 のチェック
        if (rentalDate.isAfter(returnDate) || rentalDate.isEqual(returnDate)) {
            result.rejectValue("expectedReturnOn", "error.expectedReturnOn", "返却日は貸出日より後の日付（次の日以降）にしてください");
        }

        // 対象書籍の在庫ステータスが「貸出可(0)」かチェック
        if (stock.getStatus() != 0) {
            result.rejectValue("stockId", "error.stockId", "この本は現在貸し出しできません");
        }

        // 既存データとの期間重複チェック
        Boolean overlapping = rentalManageRepository.existsOverlappingRental(
                rentalManageDto.getStockId(), rentalDate, returnDate);
        if (Boolean.TRUE.equals(overlapping)) {
            result.rejectValue("expectedRentalOn", "error.expectedRentalOn", "この期間は既に予約があります");
            result.rejectValue("expectedReturnOn", "error.expectedReturnOn", "この期間は既に予約があります");
        }

        // エラーが1つでもあれば保存せずに戻る
        if (result.hasErrors())
            return;

        // DBへの保存処理
        RentalManage rentalManage = new RentalManage();
        rentalManage.setAccount(account);
        rentalManage.setStock(stock);
        rentalManage.setStatus(rentalManageDto.getStatus());
        rentalManage.setExpectedRentalOn(rentalDate);
        rentalManage.setExpectedReturnOn(returnDate);

        // ステータスが「貸出中(1)」であれば貸出実績日を現在時刻で設定
        if (rentalManageDto.getStatus() == 1) {
            rentalManage.setRentaledAt(new Timestamp(new java.util.Date().getTime()));
        }
        this.rentalManageRepository.save(rentalManage);
    }

    // 貸出一覧を取得するためのメソッド
    public List<RentalManage> findAll() {
        return this.rentalManageRepository.findAll();
    }
}