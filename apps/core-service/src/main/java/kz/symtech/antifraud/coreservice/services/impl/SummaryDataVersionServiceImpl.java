package kz.symtech.antifraud.coreservice.services.impl;

import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.repository.SummaryDataVersionRepository;
import kz.symtech.antifraud.coreservice.services.SummaryDataVersionService;
import kz.symtech.antifraud.models.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SummaryDataVersionServiceImpl implements SummaryDataVersionService {

    private final SummaryDataVersionRepository summaryDataVersionRepository;

    @Override
    public SummaryDataVersion getBySummaryDataIdAndActive(UUID summaryDataId) {
        return summaryDataVersionRepository
                .findBySummaryDataIdAndIsActive(summaryDataId, true)
                .orElseThrow(() -> new NotFoundException(
                        String.format("summary data with id '%s' is not found or not active",  summaryDataId)));
    }

    @Override
    public List<SummaryDataVersion> getAll() {
        return summaryDataVersionRepository.findAll();
    }

    @Override
    public SummaryDataVersion get(Long id) {
        return summaryDataVersionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("summary data version not found"));
    }

}
