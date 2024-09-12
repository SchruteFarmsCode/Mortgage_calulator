package calculator.service;

import calculator.model.InputData;
import calculator.model.Overpayment;
import calculator.model.Rate;
import calculator.model.RateAmounts;

public interface ConstantAmountsCalculationService {
    RateAmounts calculate(InputData inputData, Overpayment overpayment);

    RateAmounts calculate(InputData inputData, Overpayment overpayment, Rate previousRate);
}
