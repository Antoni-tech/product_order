package kz.symtech.antifraud.connectorhandlerservice.controllers;

import kz.symtech.antifraud.connectorhandlerservice.dto.QualityTestRequestDTO;
import kz.symtech.antifraud.connectorhandlerservice.dto.QualityTestResponseDTO;
import kz.symtech.antifraud.connectorhandlerservice.dto.QuantityTestRequestDTO;
import kz.symtech.antifraud.connectorhandlerservice.services.RuleEvaluateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rule-evaluate")
@RequiredArgsConstructor
public class RuleEvaluateController {

    private final RuleEvaluateService ruleEvaluateService;

    @PostMapping("/quantity")
    public Object testQuantity(@RequestBody QuantityTestRequestDTO quantityTestRequestDTO) {
        return ruleEvaluateService.testQuantityRule(quantityTestRequestDTO);
    }

    @PostMapping("/quality")
    public List<QualityTestResponseDTO> testQuality(@RequestBody QualityTestRequestDTO qualityTestRequestDTO) {
        return ruleEvaluateService.testQualityRule(qualityTestRequestDTO);
    }
}
