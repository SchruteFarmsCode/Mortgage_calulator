package calculator.service;

import calculator.model.InputData;
import calculator.model.MortgageResidual;
import calculator.model.Rate;
import calculator.model.RateAmounts;

public interface ResidualCalculationService {

    MortgageResidual calculate(RateAmounts rateAmounts, InputData inputData);

    MortgageResidual calculate(RateAmounts rateAmounts, final InputData inputData, Rate previousRate);
}
