package calculator.service;

import calculator.model.InputData;
import calculator.model.Rate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RateCalculationServiceImpl implements RateCalculationService {

    @Override
    public List<Rate> calculate(InputData inputData) {
        return null;
    }
}
