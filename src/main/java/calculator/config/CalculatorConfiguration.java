package calculator.config;

import calculator.model.Rate;
import calculator.model.RateAmounts;
import calculator.model.Summary;
import calculator.service.SummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
@Slf4j
@Configuration
@ComponentScan(basePackages = "calculator")
public class CalculatorConfiguration {
    @Bean
    public static SummaryService create() {
        return rates -> {
            log.info("Calculating summary for {} rates", rates.size());
            BigDecimal interestSum = calculate(rates, rate -> rate.rateAmounts().interestAmount());
            BigDecimal overpaymentProvisionSum = calculate(rates, rate -> rate.rateAmounts().overpaymentDetails().getProvisionAmount());
            BigDecimal totalLostSum = interestSum.add(overpaymentProvisionSum);
            BigDecimal totalCapital = calculate(rates, rate -> totalCapital(rate.rateAmounts()));
            return new Summary(interestSum, overpaymentProvisionSum, totalLostSum, totalCapital);
        };
    }

    private static BigDecimal totalCapital(final RateAmounts rateAmounts) {
        return rateAmounts.capitalAmount().add(rateAmounts.overpaymentDetails().getAmount());
    }

    private static BigDecimal calculate(final List<Rate> rates, Function<Rate, BigDecimal> function) {
        return rates.stream()
                .reduce(BigDecimal.ZERO, (sum, next) -> sum.add(function.apply(next)), BigDecimal::add);
    }

}


