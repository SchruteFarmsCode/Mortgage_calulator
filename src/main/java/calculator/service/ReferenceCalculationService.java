package calculator.service;

import calculator.model.InputData;
import calculator.model.MortgageReference;
import calculator.model.Rate;
import calculator.model.RateAmounts;

public interface ReferenceCalculationService {
    MortgageReference calculate(RateAmounts rateAmounts, InputData inputData);

    MortgageReference calculate(RateAmounts rateAmounts, final InputData inputData, Rate previousRate);
}
