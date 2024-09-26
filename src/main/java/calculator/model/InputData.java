package calculator.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
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
        BigDecimal result = wiborPercent.divide(PERCENT, 4, RoundingMode.HALF_UP);
        log.debug("Converted wiborPercent: {} to {}", wiborPercent, result);
        return result;
    }

    public BigDecimal marginPercent() {
        BigDecimal result = marginPercent.divide(PERCENT, 4, RoundingMode.HALF_UP);
        log.debug("Converted marginPercent: {} to {}", marginPercent, result);
        return result;
    }

    public BigDecimal overpaymentProvisionPercent() {
        BigDecimal result = overpaymentProvisionPercent.divide(PERCENT, 4, RoundingMode.HALF_UP);
        log.debug("Converted overpaymentProvisionPercent: {} to {}", overpaymentProvisionPercent, result);
        return result;
    }

    public BigDecimal interestPercent() {
        BigDecimal result = marginPercent().add(wiborPercent());
        log.debug("Calculated interestPercent: {}", result);
        return result;
    }

    public BigDecimal interestToDisplay() {
        BigDecimal result = wiborPercent().add(marginPercent());
        log.debug("Calculated interestToDisplay: {}", result);
        return result;
    }
}


