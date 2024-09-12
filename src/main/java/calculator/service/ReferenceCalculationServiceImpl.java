package calculator.service;

import calculator.model.InputData;
import calculator.model.MortgageReference;
import calculator.model.Rate;
import calculator.model.RateAmounts;
import org.springframework.stereotype.Service;

@Service
public class ReferenceCalculationServiceImpl implements ReferenceCalculationService{
    @Override
    public MortgageReference calculate(RateAmounts rateAmounts, InputData inputData) {
        return null;
    }

    @Override
    public MortgageReference calculate(RateAmounts rateAmounts, InputData inputData, Rate previousRate) {
        return null;
    }
}
