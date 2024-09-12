package calculator.service;

import calculator.model.InputData;
import calculator.model.Overpayment;
import calculator.model.Rate;
import calculator.model.RateAmounts;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface AmountsCalculationService {
    BigDecimal YEAR = BigDecimal.valueOf(12);


    static BigDecimal calculateInterestAmount(final BigDecimal residualAmount, final BigDecimal interestPercentValue) {
        return residualAmount.multiply(interestPercentValue).divide(AmountsCalculationService.YEAR, 2, RoundingMode.HALF_UP);
    }


}

