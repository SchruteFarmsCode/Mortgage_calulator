package calculator.service;

import calculator.model.InputData;
import calculator.model.Rate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MortgageCalculationServiceImpl implements MortgageCalculationService {
    private final RateCalculationService rateCalculationService;

    private final PrintingService printingService;

    private final SummaryService summaryService;

    @Override
    public void calculate(InputData inputData) {
        printingService.printIntroInformation(inputData);

        List<Rate> rates = rateCalculationService.calculate(inputData);

        printingService.printSummary(summaryService.calculateSummary(rates));
        printingService.printSchedule(rates, inputData);

    }
}
