package calculator.service;

import calculator.model.InputData;
import calculator.model.Overpayment;
import calculator.model.Rate;
import calculator.model.RateAmounts;
import org.springframework.stereotype.Service;

@Service
public class DecreasingAmountsCalculationServiceImpl implements DecreasingAmountsCalculationService {
    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment) {
        return null;
    }

    @Override
    public RateAmounts calculate(InputData inputData, Overpayment overpayment, Rate previousRate) {
        return null;
    }
}
