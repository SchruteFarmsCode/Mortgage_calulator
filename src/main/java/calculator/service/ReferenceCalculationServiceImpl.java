package calculator.service;

import calculator.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Slf4j
@Service
public class ReferenceCalculationServiceImpl implements ReferenceCalculationService{

    @Override
    public MortgageReference calculate(RateAmounts rateAmounts, InputData inputData) {
        if (BigDecimal.ZERO.equals(inputData.amount())) {
            log.warn("Input amount is zero, returning zero MortgageReference.");
            return new MortgageReference(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        log.info("Calculating MortgageReference with amount: {} and months duration: {}", inputData.amount(), inputData.monthsDuration());
        return new MortgageReference(inputData.amount(), inputData.monthsDuration());
    }

    @Override
    public MortgageReference calculate(RateAmounts rateAmounts, final InputData inputData, Rate previousRate) {
        if (BigDecimal.ZERO.equals(previousRate.mortgageResidual().residualAmount())) {
            log.warn("Previous residual amount is zero, returning zero MortgageReference.");
            return new MortgageReference(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        return switch (inputData.overpaymentReduceWay()) {
            case REDUCE_RATE -> reduceRateMortgageReference(rateAmounts, previousRate.mortgageResidual());
            case REDUCE_PERIOD -> new MortgageReference(inputData.amount(), inputData.monthsDuration());
            default -> throw new MortgageException("Case not handled");
        };

    }

    private MortgageReference reduceRateMortgageReference(final RateAmounts rateAmounts, final MortgageResidual previousResidual) {
        if (rateAmounts.overpaymentDetails().getAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal residualAmount = calculateResidualAmount(previousResidual.residualAmount(), rateAmounts);
            log.info("Reduced rate MortgageReference calculated with residual amount: {} and duration: {}"
                    , residualAmount, previousResidual.residualDuration().subtract(BigDecimal.ONE));
            return new MortgageReference(residualAmount, previousResidual.residualDuration().subtract(BigDecimal.ONE));
        }
        log.info("No overpayment; returning previous residual amount and duration.");
        return new MortgageReference(previousResidual.residualAmount(), previousResidual.residualDuration());
    }

    private BigDecimal calculateResidualAmount(final BigDecimal residualAmount, final RateAmounts rateAmounts) {
        BigDecimal calculatedResidualAmount = residualAmount
                .subtract(rateAmounts.capitalAmount())
                .subtract(rateAmounts.overpaymentDetails().getAmount())
                .max(BigDecimal.ZERO);
        log.debug("Calculated residual amount: {}", calculatedResidualAmount);
        return calculatedResidualAmount;
    }
}
