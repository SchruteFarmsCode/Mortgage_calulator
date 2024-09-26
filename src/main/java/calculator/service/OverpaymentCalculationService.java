package calculator.service;

import calculator.model.InputData;
import calculator.model.OverpaymentDetails;

import java.math.BigDecimal;

public interface OverpaymentCalculationService {
    OverpaymentDetails calculate(final BigDecimal rateNumber, final InputData inputData);
}
