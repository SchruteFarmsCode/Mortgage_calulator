package calculator.service;

import calculator.model.InputData;
import calculator.model.OverpaymentDetails;
import calculator.model.Rate;
import calculator.model.RateAmounts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AmountsCalculationServiceImpl implements AmountsCalculationService {

    private final ConstantAmountsCalculationService constantAmountsCalculationService;

    private final DecreasingAmountsCalculationService decreasingAmountsCalculationService;


    public RateAmounts calculate(final InputData inputData, final OverpaymentDetails overpaymentDetails) {
        log.info("Calculating amounts for InputData: {} and OverpaymentDetails: {}", inputData, overpaymentDetails);
        RateAmounts result = switch (inputData.rateType()) {
            case CONSTANT -> constantAmountsCalculationService.calculate(inputData, overpaymentDetails);
            case DECREASING -> decreasingAmountsCalculationService.calculate(inputData, overpaymentDetails);
        };
        log.info("Calculated RateAmounts: {}", result);
        return result;
    }


    public RateAmounts calculate(final InputData inputData, final OverpaymentDetails overpaymentDetails, final Rate previousRate) {
        log.info("Calculating amounts for InputData: {}, OverpaymentDetails: {}, and previous Rate: {}", inputData, overpaymentDetails, previousRate);
        RateAmounts result = switch (inputData.rateType()) {
            case CONSTANT -> constantAmountsCalculationService.calculate(inputData, overpaymentDetails, previousRate);
            case DECREASING ->
                    decreasingAmountsCalculationService.calculate(inputData, overpaymentDetails, previousRate);
        };
        log.info("Calculated RateAmounts: {}", result);
        return result;
    }


}

