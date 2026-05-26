package jp.co.metateam.library.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

//貸出管理
@Getter
@Setter
public class RentalManageDto {

    @NotEmpty(message = "社員番号は必須です")
    private String employeeId;

    @NotEmpty(message = "在庫管理番号は必須です")
    private String stockId;

    @NotNull(message = "貸出ステータスは必須です")
    private Integer status;

    @NotNull(message = "貸出予定日は必須です")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedRentalOn;

    @NotNull(message = "返却予定日は必須です")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedReturnOn;

}