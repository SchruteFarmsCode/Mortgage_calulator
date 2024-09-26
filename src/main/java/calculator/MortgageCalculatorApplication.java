package calculator;

import calculator.model.MortgageType;
import calculator.model.Overpayment;
import calculator.service.InputDataRepository;
import calculator.service.MortgageCalculationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

@SpringBootApplication
public class MortgageCalculatorApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MortgageCalculatorApplication.class, args);

        InputDataRepository inputDataRepository = context.getBean(InputDataRepository.class);
        final var inputData = inputDataRepository.read();
        if (inputData.isEmpty()) {
            return;
        }


        var overpaymentSchema = new TreeMap<>(
                Map.of(
                        5, BigDecimal.valueOf(12000),
                        19, BigDecimal.valueOf(10000),
                        28, BigDecimal.valueOf(11000),
                        64, BigDecimal.valueOf(16000),
                        78, BigDecimal.valueOf(18000)
                )
        );

        var updatedInputData = inputData.get()
                .withAmount(new BigDecimal("296192.11"))
                .withMonthsDuration(BigDecimal.valueOf(360))
                .withOverpaymentReduceWay(Overpayment.REDUCE_PERIOD)
                .withRateType(MortgageType.DECREASING)
                .withOverpaymentSchema(overpaymentSchema);

        MortgageCalculationService mortgageCalculationService = context.getBean(MortgageCalculationService.class);
        mortgageCalculationService.calculate(updatedInputData);

    }
}
