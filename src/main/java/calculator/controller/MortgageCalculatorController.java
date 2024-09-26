package calculator.controller;

import calculator.model.InputData;
import calculator.model.MortgageType;
import calculator.model.Overpayment;
import calculator.model.Rate;
import calculator.service.RateCalculationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/mortgage")
public class MortgageCalculatorController {

    private final RateCalculationService rateCalculationService;

    @Autowired
    public MortgageCalculatorController(RateCalculationService rateCalculationService) {
        this.rateCalculationService = rateCalculationService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Map.class, "overpaymentSchema", new StringToMapConverter());
    }

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("inputData", new InputData(
                LocalDate.now(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                MortgageType.CONSTANT,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new HashMap<>(),
                Overpayment.REDUCE_RATE,
                false,
                BigDecimal.ZERO
        ));
        return "mortgageForm";
    }

    @PostMapping()
    public String calculateMortgage(@Valid @ModelAttribute InputData inputData, Model model) {
        log.info("InputData: {}", inputData);
        log.info("OverpaymentSchema: {}", inputData.overpaymentSchema());

        List<Rate> rates = rateCalculationService.calculate(inputData);

        model.addAttribute("rates", rates);
        model.addAttribute("inputData", inputData);

        return "mortgageResult";
    }
}




