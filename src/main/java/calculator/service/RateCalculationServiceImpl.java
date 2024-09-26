package calculator.service;

import calculator.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RateCalculationServiceImpl implements RateCalculationService {

    private final TimePointCalculationService timePointCalculationService;

    private final AmountsCalculationService amountsCalculationService;

    private final ResidualCalculationService residualCalculationService;

    private final ReferenceCalculationService referenceCalculationService;

    private final OverpaymentCalculationService overpaymentCalculationService;

    @Override
    public List<Rate> calculate(final InputData inputData) {
        List<Rate> rateList = new ArrayList<>();

        BigDecimal rateNumber = BigDecimal.ONE;

        Rate zeroRate = calculateZeroRate(rateNumber, inputData);

        Rate previousRate = zeroRate;
        rateList.add(zeroRate);

        for (BigDecimal i = rateNumber.add(BigDecimal.ONE); i.compareTo(inputData.monthsDuration()) <= 0; i = i.add(BigDecimal.ONE)) {
            Rate nextRate = calculateNextRate(i, inputData, previousRate);
            previousRate = nextRate;
            rateList.add(nextRate);

            if (BigDecimal.ZERO.equals(nextRate.mortgageResidual().residualAmount().setScale(0, RoundingMode.HALF_UP))) {
                break;
            }
        }

        return rateList;
    }

    private Rate calculateZeroRate(final BigDecimal rateNumber, final InputData inputData) {
        TimePoint timePoint = timePointCalculationService.calculate(rateNumber, inputData);
        OverpaymentDetails overpaymentDetails = overpaymentCalculationService.calculate(rateNumber, inputData);
        RateAmounts rateAmounts = amountsCalculationService.calculate(inputData, overpaymentDetails);
        MortgageResidual mortgageResidual = residualCalculationService.calculate(rateAmounts, inputData);
        MortgageReference mortgageReference = referenceCalculationService.calculate(rateAmounts, inputData);

        return new Rate(rateNumber, timePoint, rateAmounts, mortgageResidual, mortgageReference);
    }

    private Rate calculateNextRate(final BigDecimal rateNumber, final InputData inputData, final Rate previousRate) {
        TimePoint timepoint = timePointCalculationService.calculate(rateNumber, previousRate);
        OverpaymentDetails overpaymentDetails = overpaymentCalculationService.calculate(rateNumber, inputData);
        RateAmounts rateAmounts = amountsCalculationService.calculate(inputData, overpaymentDetails, previousRate);
        MortgageResidual mortgageResidual = residualCalculationService.calculate(rateAmounts, inputData, previousRate);
        MortgageReference mortgageReference = referenceCalculationService.calculate(rateAmounts, inputData, previousRate);

        return new Rate(rateNumber, timepoint, rateAmounts, mortgageResidual, mortgageReference);
    }
}
