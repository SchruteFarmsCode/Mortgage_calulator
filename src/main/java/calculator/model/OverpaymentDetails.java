package calculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public class OverpaymentDetails {
    private final BigDecimal amount;
    private final BigDecimal provisionAmount;
    private final Overpayment overpaymentType;


}

