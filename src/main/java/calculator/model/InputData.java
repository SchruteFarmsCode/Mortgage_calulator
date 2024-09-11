package calculator.model;

import lombok.Builder;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@With
public record InputData(
        LocalDate repaymentStartDate,
        BigDecimal wiborPercent,
        BigDecimal amount,
        BigDecimal monthsDuration,
        MortgageType rateType,
        BigDecimal marginPercent,
        BigDecimal overpaymentProvisionPercent,
        BigDecimal overpaymentProvisionMonths,
        BigDecimal overpaymentStartMonth,
        Map<Integer, BigDecimal> overpaymentSchema,
        String overpaymentReduceWay,
        boolean mortgagePrintPayoffsSchedule,
        Integer mortgageRateNumberToPrint
) {
    @Builder
    public InputData {
    }

    private static final BigDecimal PERCENT = new BigDecimal("100");

}

