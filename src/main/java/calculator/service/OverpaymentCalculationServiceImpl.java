package calculator.service;

import calculator.model.InputData;
import calculator.model.Overpayment;
import calculator.model.OverpaymentDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
@Slf4j
@Service
public class OverpaymentCalculationServiceImpl implements OverpaymentCalculationService{

    @Override
    public OverpaymentDetails calculate(final BigDecimal rateNumber, final InputData inputData) {
        log.info("Calculating overpayment details for rate number: {} with input data: {}", rateNumber, inputData);

        BigDecimal overpaymentAmount = calculateOverpaymentAmount(rateNumber, inputData.overpaymentSchema()).orElse(BigDecimal.ZERO);
        BigDecimal overpaymentProvision = calculateOverpaymentProvision(rateNumber, overpaymentAmount, inputData);
        Overpayment overpaymentType = inputData.overpaymentReduceWay();

        log.info("Calculated overpayment amount: {}, provision: {}, type: {}",
                overpaymentAmount, overpaymentProvision, overpaymentType);

        return new OverpaymentDetails(overpaymentAmount, overpaymentProvision, overpaymentType);
    }

    private Optional<BigDecimal> calculateOverpaymentAmount(final BigDecimal rateNumber, Map<Integer, BigDecimal> overpaymentSchema) {
        log.debug("Calculating overpayment amount for rate number: {}", rateNumber);

        return overpaymentSchema.entrySet().stream()
                .filter(entry -> BigDecimal.valueOf(entry.getKey()).equals(rateNumber))
                .findFirst()
                .map(Map.Entry::getValue);
    }

    private BigDecimal calculateOverpaymentProvision(final BigDecimal rateNumber, final BigDecimal overpaymentAmount, final InputData inputData) {
        log.debug("Calculating overpayment provision for rate number: {}, overpayment amount: {}", rateNumber, overpaymentAmount);

        if (BigDecimal.ZERO.equals(overpaymentAmount)) {
            return BigDecimal.ZERO;
        }

        if (rateNumber.compareTo(inputData.overpaymentProvisionMonths()) > 0) {
            log.info("No overpayment provision applicable as rate number exceeds provision months.");
            return BigDecimal.ZERO;
        }

        BigDecimal provision = overpaymentAmount.multiply(inputData.overpaymentProvisionPercent());
        log.info("Calculated overpayment provision: {}", provision);
        return provision;
    }

}
