package calculator.service;

import calculator.model.Rate;
import calculator.model.Summary;

import java.util.List;

@FunctionalInterface
public interface SummaryService {
    Summary calculateSummary(List<Rate> rates);
}
