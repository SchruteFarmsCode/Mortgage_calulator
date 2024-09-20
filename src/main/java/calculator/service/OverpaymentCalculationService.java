package calculator.service;

import calculator.model.InputData;
import calculator.model.Overpayment;

import java.math.BigDecimal;

public interface OverpaymentCalculationService {
    Overpayment calculate(final BigDecimal rateNumber, final InputData inputData);
}
