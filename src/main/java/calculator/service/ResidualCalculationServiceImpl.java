package calculator.service;

import calculator.model.InputData;
import calculator.model.MortgageResidual;
import calculator.model.Rate;
import calculator.model.RateAmounts;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ResidualCalculationServiceImpl implements ResidualCalculationService {

    @Override
    public MortgageResidual calculate(RateAmounts rateAmounts, InputData inputData) {
        if (BigDecimal.ZERO.equals(inputData.amount())) {
            return new MortgageResidual(BigDecimal.ZERO, BigDecimal.ZERO);
        } else {
            BigDecimal residualAmount = calculateResidualAmount(inputData.amount(), rateAmounts);
            BigDecimal residualDuration = calculateResidualDuration(inputData, residualAmount, inputData.monthsDuration(), rateAmounts);
            return new MortgageResidual(residualAmount, residualDuration);
        }
    }

    @Override
    public MortgageResidual calculate(RateAmounts rateAmounts, final InputData inputData, Rate previousRate) {
        BigDecimal previousResidualAmount = previousRate.mortgageResidual().residualAmount();
        BigDecimal previousResidualDuration = previousRate.mortgageResidual().residualDuration();

        if (BigDecimal.ZERO.equals(previousResidualAmount)) {
            return new MortgageResidual(BigDecimal.ZERO, BigDecimal.ZERO);
        } else {
            BigDecimal residualAmount = calculateResidualAmount(previousResidualAmount, rateAmounts);
            BigDecimal residualDuration = calculateResidualDuration(inputData, residualAmount, previousResidualDuration, rateAmounts);
            return new MortgageResidual(residualAmount, residualDuration);
        }
    }

    private BigDecimal calculateResidualDuration(
            InputData inputData,
            BigDecimal residualAmount,
            BigDecimal previousResidualDuration,
            RateAmounts rateAmounts
    ) {
        if (rateAmounts.overpaymentDetails().getAmount().compareTo(BigDecimal.ZERO) > 0) {
            return switch (inputData.rateType()) {
                case CONSTANT -> calculateConstantResidualDuration(inputData, residualAmount, rateAmounts);
                case DECREASING -> calculateDecreasingResidualDuration(residualAmount, rateAmounts);
            };
        } else {
            return previousResidualDuration.subtract(BigDecimal.ONE);
        }
    }

    private BigDecimal calculateDecreasingResidualDuration(BigDecimal residualAmount, RateAmounts rateAmounts) {
        return residualAmount.divide(rateAmounts.capitalAmount(), 0, RoundingMode.CEILING);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private BigDecimal calculateConstantResidualDuration(InputData inputData, BigDecimal residualAmount, RateAmounts rateAmounts) {
        BigDecimal q = AmountsCalculationService.calculateQ(inputData.interestPercent());

        BigDecimal xNumerator = rateAmounts.rateAmount();
        BigDecimal xDenominator = rateAmounts.rateAmount().subtract(residualAmount.multiply(q.subtract(BigDecimal.ONE)));

        BigDecimal x = xNumerator.divide(xDenominator, 10, RoundingMode.HALF_UP);
        BigDecimal y = q;

        BigDecimal logX = BigDecimal.valueOf(Math.log(x.doubleValue()));
        BigDecimal logY = BigDecimal.valueOf(Math.log(y.doubleValue()));

        return logX.divide(logY, 0, RoundingMode.CEILING);
    }

    private BigDecimal calculateResidualAmount(final BigDecimal residualAmount, final RateAmounts rateAmounts) {
        return residualAmount
                .subtract(rateAmounts.capitalAmount())
                .subtract(rateAmounts.overpaymentDetails().getAmount())
                .max(BigDecimal.ZERO);
    }
}
