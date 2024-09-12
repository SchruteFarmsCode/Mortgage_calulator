package calculator.service;

import calculator.model.InputData;
import calculator.model.Rate;
import calculator.model.Summary;

import java.util.List;

public interface PrintingService {
    void printIntroInformation(InputData inputData);

    void printSchedule(List<Rate> rates, final InputData inputData);

    void printSummary(Summary summaryNoOverpayment);
}
