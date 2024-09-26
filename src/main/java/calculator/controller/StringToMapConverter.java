package calculator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Component
public class StringToMapConverter extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) {
        setValue(parseOverpaymentSchema(text));
    }

    private Map<Integer, BigDecimal> parseOverpaymentSchema(String schema) {
        Map<Integer, BigDecimal> overpaymentSchema = new HashMap<>();
        if (schema != null && !schema.isEmpty()) {
            String[] entries = schema.split(",");
            for (String entry : entries) {
                String[] parts = entry.split(":");
                if (parts.length == 2) {
                    try {
                        Integer month = Integer.parseInt(parts[0]);
                        BigDecimal amount = new BigDecimal(parts[1]);
                        overpaymentSchema.put(month, amount);
                    } catch (NumberFormatException e) {
                        log.error("Invalid format for entry: {}", entry, e);
                        throw new IllegalArgumentException("Błędny format danych w polu 'schemat nadpłat'");
                    }
                }
            }
        }
        return overpaymentSchema;
    }
}
