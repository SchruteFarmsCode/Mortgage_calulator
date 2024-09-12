package calculator.service;

import calculator.model.InputData;
import calculator.model.Rate;

import java.util.List;

public interface RateCalculationService {
    List<Rate> calculate(InputData inputData);

}
