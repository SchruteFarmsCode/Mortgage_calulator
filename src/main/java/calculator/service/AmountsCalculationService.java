package calculator.service;

import calculator.model.InputData;
import calculator.model.OverpaymentDetails;
import calculator.model.Rate;
import calculator.model.RateAmounts;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface AmountsCalculationService {
    BigDecimal YEAR = BigDecimal.valueOf(12);

    RateAmounts calculate(final InputData inputData, final OverpaymentDetails overpaymentDetails);

    RateAmounts calculate(final InputData inputData, final OverpaymentDetails overpaymentDetails, final Rate previousRate);


    static BigDecimal calculateInterestAmount(final BigDecimal residualAmount, final BigDecimal interestPercentValue) {
        return residualAmount.multiply(interestPercentValue).divide(AmountsCalculationService.YEAR, 2, RoundingMode.HALF_UP);
    }

    static BigDecimal calculateQ(final BigDecimal interestPercent) {
        return interestPercent.divide(AmountsCalculationService.YEAR, 10, RoundingMode.HALF_UP).add(BigDecimal.ONE);
    }

    static BigDecimal compareCapitalWithResidual(final BigDecimal capitalAmount, final BigDecimal residualAmount) {
        if (capitalAmount.compareTo(residualAmount) >= 0) {
            return residualAmount;
        }
        return capitalAmount;
    }


}

