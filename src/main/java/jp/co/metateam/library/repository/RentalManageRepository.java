package jp.co.metateam.library.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;

import jp.co.metateam.library.model.RentalManage;
import jp.co.metateam.library.model.RentalManageDto;

@Repository
public interface RentalManageRepository extends JpaRepository<RentalManage, Long> {

    // 貸出一覧取得
    List<RentalManage> findAll();

    // 貸出期間の重複チェック
    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
            FROM RentalManage r
            WHERE r.stock.id = :stockId
              AND r.status IN (0, 1)
              AND r.expectedRentalOn <= :expectedReturnOn
              AND r.expectedReturnOn >= :expectedRentalOn """)

    Boolean existsOverlappingRental(
            @Param("stockId") String stockId,
            @Param("expectedRentalOn") LocalDate expectedRentalOn,
            @Param("expectedReturnOn") LocalDate expectedReturnOn);

    void save(RentalManageDto rentalManageDto, BindingResult result);
}
