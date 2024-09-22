package calculator.service;

import calculator.model.InputData;
import calculator.model.Rate;
import calculator.model.Summary;

import java.util.List;

public interface PrintingService {

    String SCHEDULE_TABLE_FORMAT =
            "%-4s %3s " +
                    "%-4s %3s " +
                    "%-7s %3s " +
                    "%-7s %3s " +
                    "%-4s %10s " +
                    "%-7s %10s " +
                    "%-7s %10s " +
                    "%-7s %10s " +
                    "%-8s %10s " +
                    "%-8s %10s%n";

    List<String> RATE_LINE_KEYS = List.of(
            "NR:",
            "ROK:",
            "MIESIAC:",
            "DATA:",
            "RATA:",
            "ODSETKI:",
            "KAPITAL:",
            "NADPlATA:",
            "PKWT:",
            "PMSC:"
    );

    String INTRO_INFORMATION = """
            KWOTA KREDYTU: %s ZL
            OKRES KREDYTOWANIA: %s MIESIECY
            ODSETKI: %s %%
            MIESIAC ROZPOCZECIA NADPLAT: %s MIESIAC
            """;

    String OVERPAYMENT_INFORMATION = """
            %s
            SCHEMAT DOKONYWANIA NADPLAT:
            %s""";

    String SUMMARY_INFORMATION = """
                    
            SUMA ODSETEK: %s ZL
            PROWIZJA ZA NADPLATY: %s ZL
            SUMA STRAT: %s ZL
            SUMA KAPITALU: %s ZL
                    
            """;

    String OVERPAYMENT_REDUCE_RATE = "NADPLATA, ZMNIEJSZENIE RATY";
    String OVERPAYMENT_REDUCE_PERIOD = "NADPLATA, SKROCENIE OKRESU";
    String OVERPAYMENT_SCHEMA = "MIESIAC: %s, KWOTA: %s ZL%n";

    void printIntroInformation(InputData inputData);

    void printSchedule(List<Rate> rates, final InputData inputData);

    void printSummary(Summary summaryNoOverpayment);
}
