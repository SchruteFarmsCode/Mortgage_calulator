package calculator.service;

import calculator.model.InputData;
import calculator.model.OverpaymentDetails;
import calculator.model.Rate;
import calculator.model.RateAmounts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AmountsCalculationServiceImpl implements AmountsCalculationService {

    private final ConstantAmountsCalculationService constantAmountsCalculationService;

    private final DecreasingAmountsCalculationService decreasingAmountsCalculationService;


    public RateAmounts calculate(final InputData inputData, final OverpaymentDetails overpaymentDetails) {
        return switch (inputData.rateType()) {
            case CONSTANT -> constantAmountsCalculationService.calculate(inputData, overpaymentDetails);
            case DECREASING -> decreasingAmountsCalculationService.calculate(inputData, overpaymentDetails);
        };
    }


    public RateAmounts calculate(final InputData inputData, final OverpaymentDetails overpaymentDetails, final Rate previousRate) {
        return switch (inputData.rateType()) {
            case CONSTANT -> constantAmountsCalculationService.calculate(inputData, overpaymentDetails, previousRate);
            case DECREASING -> decreasingAmountsCalculationService.calculate(inputData, overpaymentDetails, previousRate);
        };
    }


    }

