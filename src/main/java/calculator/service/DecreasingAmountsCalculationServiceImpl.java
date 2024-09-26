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
public class DecreasingAmountsCalculationServiceImpl implements DecreasingAmountsCalculationService {
    @Override
    public RateAmounts calculate(final InputData inputData, final OverpaymentDetails overpaymentDetails) {
        log.info("Calculating decreasing amounts for InputData: {} and OverpaymentDetails: {}", inputData, overpaymentDetails);
        BigDecimal interestPercent = inputData.interestPercent();

        final BigDecimal residualAmount = inputData.amount();
        final BigDecimal residualDuration = inputData.monthsDuration();

        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        log.debug("Interest Amount calculated: {}", interestAmount);

        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(
                calculateDecreasingCapitalAmount(residualAmount, residualDuration), residualAmount);
        log.debug("Capital Amount calculated: {}", capitalAmount);

        BigDecimal rateAmount = capitalAmount.add(interestAmount);
        log.debug("Rate Amount calculated: {}", rateAmount);

        RateAmounts result = new RateAmounts(rateAmount, interestAmount, capitalAmount, overpaymentDetails);
        log.info("Calculated RateAmounts: {}", result);
        return result;
    }

    @Override
    public RateAmounts calculate(final InputData inputData, final OverpaymentDetails overpaymentDetails, final Rate previousRate) {
        log.info("Calculating decreasing amounts for InputData: {}, OverpaymentDetails: {}, and previous Rate: {}", inputData, overpaymentDetails, previousRate);
        BigDecimal interestPercent = inputData.interestPercent();

        BigDecimal residualAmount = previousRate.mortgageResidual().residualAmount();
        BigDecimal referenceAmount = previousRate.mortgageReference().referenceAmount();
        BigDecimal referenceDuration = previousRate.mortgageReference().referenceDuration();

        BigDecimal interestAmount = AmountsCalculationService.calculateInterestAmount(residualAmount, interestPercent);
        log.debug("Interest Amount calculated: {}", interestAmount);

        BigDecimal capitalAmount = AmountsCalculationService.compareCapitalWithResidual(
                calculateDecreasingCapitalAmount(referenceAmount, referenceDuration), residualAmount);
        log.debug("Capital Amount calculated: {}", capitalAmount);

        BigDecimal rateAmount = capitalAmount.add(interestAmount);
        log.debug("Rate Amount calculated: {}", rateAmount);

        RateAmounts result = new RateAmounts(rateAmount, interestAmount, capitalAmount, overpaymentDetails);
        log.info("Calculated RateAmounts: {}", result);
        return result;
    }

    private BigDecimal calculateDecreasingCapitalAmount(final BigDecimal residualAmount, final BigDecimal residualDuration) {
        BigDecimal capitalAmount = residualAmount.divide(residualDuration, 2, RoundingMode.HALF_UP);
        log.debug("Decreasing Capital Amount calculated: {}", capitalAmount);
        return capitalAmount;
    }
}
