package calculator.service;

import calculator.model.InputData;
import calculator.model.OverpaymentDetails;
import calculator.model.Rate;
import calculator.model.RateAmounts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
@Slf4j
@Service
public class ConstantAmountsCalculationServiceImpl implements ConstantAmountsCalculationService {

    @Override
    public RateAmounts calculate(final InputData inputData, final OverpaymentDetails overpaymentDetails) {
        log.info("Calculating constant amounts for InputData: {} and OverpaymentDetails: {}", inputData, overpaymentDetails);
        BigDecimal interestPercent = inputData.interestPercent();
        BigDecimal q = AmountsCalculationService.calculateQ(interestPercent);
        BigDecimal residualAmount = inputData.amount();

        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        log.debug("Interest Amount calculated: {}", interestAmount);

        BigDecimal rateAmount = calculateConstantRateAmount(q, interestAmount, residualAmount, inputData.amount(), inputData.monthsDuration());
        log.debug("Rate Amount calculated: {}", rateAmount);

        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(rateAmount.subtract(interestAmount), residualAmount);
        log.debug("Capital Amount calculated: {}", capitalAmount);

        RateAmounts result = new RateAmounts(rateAmount, interestAmount, capitalAmount, overpaymentDetails);
        log.info("Calculated RateAmounts: {}", result);
        return result;

    }

    @Override
    public RateAmounts calculate(final InputData inputData, final OverpaymentDetails overpaymentDetails, final Rate previousRate) {
        log.info("Calculating constant amounts for InputData: {}, OverpaymentDetails: {}, and previous Rate: {}", inputData, overpaymentDetails, previousRate);
        BigDecimal interestPercent = inputData.interestPercent();
        BigDecimal q = AmountsCalculationService.calculateQ(interestPercent);

        BigDecimal residualAmount = previousRate.mortgageResidual().residualAmount();
        BigDecimal referenceAmount = previousRate.mortgageReference().referenceAmount();
        BigDecimal referenceDuration = previousRate.mortgageReference().referenceDuration();

        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        log.info("Calculating constant amounts for InputData: {}, OverpaymentDetails: {}, and previous Rate: {}", inputData, overpaymentDetails, previousRate);

        BigDecimal rateAmount = calculateConstantRateAmount(q, interestAmount, residualAmount, referenceAmount, referenceDuration);
        log.debug("Rate Amount calculated: {}", rateAmount);

        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(rateAmount.subtract(interestAmount), residualAmount);
        log.debug("Capital Amount calculated: {}", capitalAmount);

        RateAmounts result = new RateAmounts(rateAmount, interestAmount, capitalAmount, overpaymentDetails);
        log.info("Calculated RateAmounts: {}", result);
        return result;
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
        log.debug("Calculated constant rate amount: {}", rateAmount);

        return compareRateWithResidual(rateAmount, interestAmount, residualAmount);
    }

    private BigDecimal compareRateWithResidual(
            final BigDecimal rateAmount,
            final BigDecimal interestAmount,
            final BigDecimal residualAmount
    ) {
        if (rateAmount.subtract(interestAmount).compareTo(residualAmount) >= 0) {
            log.debug("Rate amount exceeds residual amount. Returning adjusted amount.");
            return residualAmount.add(interestAmount);
        }
        return rateAmount;
    }
}
