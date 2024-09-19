package calculator.service;

import calculator.model.InputData;
import calculator.model.Overpayment;
import calculator.model.Rate;
import calculator.model.RateAmounts;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ConstantAmountsCalculationServiceImpl implements ConstantAmountsCalculationService {

    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment) {
        BigDecimal interestPercent = inputData.interestPercent();
        BigDecimal q = AmountsCalculationService.calculateQ(interestPercent);

        BigDecimal residualAmount = inputData.amount();

        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        BigDecimal rateAmount = calculateConstantRateAmount(q, interestAmount, residualAmount, inputData.amount(), inputData.monthsDuration());
        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(rateAmount.subtract(interestAmount), residualAmount);

        return new RateAmounts(rateAmount, interestAmount, capitalAmount, overpayment);
    }

    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment, Rate previousRate) {
        BigDecimal interestPercent = inputData.interestPercent();
        BigDecimal q = AmountsCalculationService.calculateQ(interestPercent);

        BigDecimal residualAmount = previousRate.mortgageResidual().residualAmount();
        BigDecimal referenceAmount = previousRate.mortgageReference().referenceAmount();
        BigDecimal referenceDuration = previousRate.mortgageReference().referenceDuration();

        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        BigDecimal rateAmount = calculateConstantRateAmount(q, interestAmount, residualAmount, referenceAmount, referenceDuration);
        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(rateAmount.subtract(interestAmount), residualAmount);

        return new RateAmounts(rateAmount, interestAmount, capitalAmount, overpayment);
    }

    private BigDecimal calculateConstantRateAmount(
            final BigDecimal q,
            final BigDecimal interestAmount,
            final BigDecimal residualAmount,
            final BigDecimal referenceAmount,
            final BigDecimal referenceDuration
    ) {
        BigDecimal rateAmount = referenceAmount
                .multiply(q.pow(referenceDuration.intValue()))
                .multiply(q.subtract(BigDecimal.ONE))
                .divide(q.pow(referenceDuration.intValue()).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);

        return compareRateWithResidual(rateAmount, interestAmount, residualAmount);
    }

    private BigDecimal compareRateWithResidual(
            final BigDecimal rateAmount,
            final BigDecimal interestAmount,
            final BigDecimal residualAmount
    ) {
        if (rateAmount.subtract(interestAmount).compareTo(residualAmount) >= 0) {
            return residualAmount.add(interestAmount);
        }
        return rateAmount;
    }
}
