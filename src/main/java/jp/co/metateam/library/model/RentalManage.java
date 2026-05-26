package jp.co.metateam.library.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import org.hibernate.annotations.IdGeneratorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "RentalManage")
public class RentalManage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 貸出ステータス
    @Column(name = "status", nullable = false)
    private Integer status;

    // 貸出予定日
    @Column(name = "expected_rental_on", nullable = false)
    private LocalDate expectedRentalOn;

    // 返却予定日
    @Column(name = "expected_return_on", nullable = false)
    private LocalDate expectedReturnOn;

    // 貸出日
    @Column(name = "rentaled_at")
    private Timestamp rentaledAt;

    // 返却日
    @Column(name = "returned_at")
    private Timestamp returnedAt;

    // キャンセル日
    @Column(name = "canceled_at")
    private Timestamp canceledAt;

    // アカウント情報
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Account account;

    // 在庫管理番号
    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    // 値を取り出す(getter)
    public Long getId() {
        return id;
    }

    public Integer getStatus() {
        return status;
    }

    public LocalDate getExpectedRentalOn() {
        return expectedRentalOn;
    }

    public LocalDate getExpectedReturnOn() {
        return expectedReturnOn;
    }

    public Timestamp getRentaledAt() {
        return rentaledAt;
    }

    public Timestamp getReturnedAt() {
        return returnedAt;
    }

    public Timestamp getCanceledAt() {
        return canceledAt;
    }

    public Account getAccount() {
        return account;
    }

    public Stock getStock() {
        return stock;
    }

    // 値を入れる(setter)
    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setExpectedRentalOn(LocalDate expectedRentalOn) {
        this.expectedRentalOn = expectedRentalOn;
    }

    public void setExpectedReturnOn(LocalDate expectedReturnOn) {
        this.expectedReturnOn = expectedReturnOn;
    }

    public void setRentaledAt(Timestamp rentaledAt) {
        this.rentaledAt = rentaledAt;
    }

    public void setReturnedAt(Timestamp returnedAt) {
        this.returnedAt = returnedAt;
    }

    public void setCanceledAt(Timestamp canceledAt) {
        this.canceledAt = canceledAt;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

}
