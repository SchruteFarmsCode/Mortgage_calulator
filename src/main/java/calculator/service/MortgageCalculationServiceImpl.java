package calculator.service;

import calculator.model.InputData;
import calculator.model.Rate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class MortgageCalculationServiceImpl implements MortgageCalculationService {

    private final RateCalculationService rateCalculationService;

    private final PrintingService printingService;

    private final SummaryService summaryService;

    @Override
    public void calculate(InputData inputData) {
        log.info("Calculating mortgage for input data: {}", inputData);

        printingService.printIntroInformation(inputData);

        List<Rate> rates = rateCalculationService.calculate(inputData);
        log.debug("Calculated rates: {}", rates);

        printingService.printSummary(summaryService.calculateSummary(rates));
        printingService.printSchedule(rates, inputData);

    }
}
