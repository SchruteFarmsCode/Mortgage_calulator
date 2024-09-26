package calculator.service;

import calculator.model.InputData;
import calculator.model.Overpayment;
import calculator.model.OverpaymentDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
@Service
public class OverpaymentCalculationServiceImpl implements OverpaymentCalculationService{

    @Override
    public OverpaymentDetails calculate(final BigDecimal rateNumber, final InputData inputData) {
        BigDecimal overpaymentAmount = calculateOverpaymentAmount(rateNumber, inputData.overpaymentSchema()).orElse(BigDecimal.ZERO);
        BigDecimal overpaymentProvision = calculateOverpaymentProvision(rateNumber, overpaymentAmount, inputData);
        Overpayment overpaymentType = inputData.overpaymentReduceWay();
        return new OverpaymentDetails(overpaymentAmount, overpaymentProvision, overpaymentType);
    }

    private Optional<BigDecimal> calculateOverpaymentAmount(final BigDecimal rateNumber, Map<Integer, BigDecimal> overpaymentSchema) {
        return overpaymentSchema.entrySet().stream()
                .filter(entry -> BigDecimal.valueOf(entry.getKey()).equals(rateNumber))
                .findFirst()
                .map(Map.Entry::getValue);
    }

    private BigDecimal calculateOverpaymentProvision(final BigDecimal rateNumber, final BigDecimal overpaymentAmount, final InputData inputData) {
        if (BigDecimal.ZERO.equals(overpaymentAmount)) {
            return BigDecimal.ZERO;
        }

        if (rateNumber.compareTo(inputData.overpaymentProvisionMonths()) > 0) {
            return BigDecimal.ZERO;
        }

        return overpaymentAmount.multiply(inputData.overpaymentProvisionPercent());
    }

}
