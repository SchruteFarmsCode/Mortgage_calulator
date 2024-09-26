package calculator.service;

import calculator.model.InputData;
import calculator.model.OverpaymentDetails;
import calculator.model.Rate;
import calculator.model.RateAmounts;

public interface ConstantAmountsCalculationService {
    RateAmounts calculate(InputData inputData, OverpaymentDetails overpaymentDetails);

    RateAmounts calculate(InputData inputData, OverpaymentDetails overpaymentDetails, Rate previousRate);
}
