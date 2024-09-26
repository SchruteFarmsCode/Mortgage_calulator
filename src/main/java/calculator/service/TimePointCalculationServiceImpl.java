package calculator.service;

import calculator.model.InputData;
import calculator.model.Rate;
import calculator.model.TimePoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
@Slf4j
@Service
public class TimePointCalculationServiceImpl implements TimePointCalculationService{

    public TimePoint calculate(final BigDecimal rateNumber, final InputData inputData) {
        BigDecimal year = calculateYear(rateNumber);
        BigDecimal month = calculateMonth(rateNumber);
        LocalDate date = inputData.repaymentStartDate();

        TimePoint timePoint = new TimePoint(year, month, date);
        log.info("Calculated TimePoint: {}", timePoint);
        return timePoint;
    }

    public TimePoint calculate(BigDecimal rateNumber, Rate previousRate) {
        BigDecimal year = calculateYear(rateNumber);
        BigDecimal month = calculateMonth(rateNumber);
        LocalDate date = previousRate.timePoint().date().plusMonths(1);

        TimePoint timePoint = new TimePoint(year, month, date);
        log.info("Calculated TimePoint from previous rate: {}", timePoint);
        return timePoint;
    }

    private BigDecimal calculateYear(final BigDecimal rateNumber) {
        BigDecimal year = rateNumber.divide(AmountsCalculationService.YEAR, RoundingMode.UP).max(BigDecimal.ONE);
        log.debug("Calculated year: {}", year);
        return year;

    }

    private BigDecimal calculateMonth(final BigDecimal rateNumber) {
        BigDecimal month = BigDecimal.ZERO.equals(rateNumber.remainder(AmountsCalculationService.YEAR))
                ? AmountsCalculationService.YEAR
                : rateNumber.remainder(AmountsCalculationService.YEAR);
        log.debug("Calculated month: {}", month);
        return month;
    }
}
