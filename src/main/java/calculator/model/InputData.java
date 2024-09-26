package calculator.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

@With
@Builder
public record InputData(
        @NotNull LocalDate repaymentStartDate,
        @DecimalMin(value = "0", message = "Wibor percent must be non-negative") BigDecimal wiborPercent,
        @DecimalMin(value = "0", message = "Amount must be non-negative") BigDecimal amount,
        @DecimalMin(value = "1", message = "Months duration must be at least 1") BigDecimal monthsDuration,
        @NotNull MortgageType rateType,
        @DecimalMin(value = "0", message = "Margin percent must be non-negative") BigDecimal marginPercent,
        @DecimalMin(value = "0", message = "Overpayment provision percent must be non-negative") BigDecimal overpaymentProvisionPercent,
        @DecimalMin(value = "0", message = "Overpayment provision months must be non-negative") BigDecimal overpaymentProvisionMonths,
        @DecimalMin(value = "1", message = "Overpayment start month must be at least 1") BigDecimal overpaymentStartMonth,
        @NotNull Map<Integer, BigDecimal> overpaymentSchema,
        @NotNull Overpayment overpaymentReduceWay,
        boolean mortgagePrintPayoffsSchedule,
        BigDecimal mortgageRateNumberToPrint
) {
    private static final BigDecimal PERCENT = new BigDecimal("100");

    public BigDecimal wiborPercent() {
        return wiborPercent.divide(PERCENT, 4, RoundingMode.HALF_UP);
    }

    public BigDecimal marginPercent() {
        return marginPercent.divide(PERCENT, 4, RoundingMode.HALF_UP);
    }

    public BigDecimal overpaymentProvisionPercent() {
        return overpaymentProvisionPercent.divide(PERCENT, 4, RoundingMode.HALF_UP);
    }

    public BigDecimal interestPercent() {
        return marginPercent().add(wiborPercent());
    }

    public BigDecimal interestToDisplay() {
        return wiborPercent().add(marginPercent());
    }
}


