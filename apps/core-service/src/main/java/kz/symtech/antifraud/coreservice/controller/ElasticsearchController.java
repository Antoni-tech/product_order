package kz.symtech.antifraud.coreservice.controller;

import kz.symtech.antifraud.coreservice.dto.ElasticConnectorSearchDto;
import kz.symtech.antifraud.coreservice.enums.ConnectorConflictType;
import kz.symtech.antifraud.coreservice.services.CountDocumentService;
import kz.symtech.antifraud.coreservice.services.ElasticsearchService;
import kz.symtech.antifraud.models.dto.ElasticSaveRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/elastic")
@RequiredArgsConstructor
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;
    private final CountDocumentService countDocumentService;

    @PostMapping("/count-errors-rules")
    public  Map<ConnectorConflictType, Map<String, Long>> countErrorsAndRules() {
        return elasticsearchService.countErrorsAndRules();
    }

    @PostMapping
    public ResponseEntity<?> createIndex(@RequestParam Long id) {
        elasticsearchService.createIndex(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public List<Object> search(@RequestBody ElasticConnectorSearchDto elasticConnectorSearchDto) {
        return elasticsearchService.searchByFields(elasticConnectorSearchDto);
    }

    @GetMapping("/count-documents")
    public Map<String, Long> getCountOfDocuments(@RequestParam Long userId,
                                                 @RequestParam String fieldName) {
        return countDocumentService.countDocumentsById(userId, fieldName);
    }

    @GetMapping("/count-documents-by-rule-id")
    private Long countDocumentsByRuleId(@RequestParam Long userId,
                                        @RequestParam Long ruleId) {
        return countDocumentService.countDocumentsByRuleId(userId, ruleId);
    }

    @PostMapping("/check-date")
    public void check() {
        elasticsearchService.deleteExpiredDocuments();
    }

    @PostMapping("/save-document")
    public String save(@RequestBody ElasticSaveRequestDTO elasticSaveRequestDTO) {
        return elasticsearchService.saveDocument(elasticSaveRequestDTO);
    }

}
