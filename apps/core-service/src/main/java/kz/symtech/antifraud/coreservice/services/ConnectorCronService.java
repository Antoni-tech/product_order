package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.coreservice.enums.SummaryDataType;

public interface ConnectorCronService {
    void start(Long sDVConnectorId, SummaryDataType type);
    void stop(Long sDVConnectorId);
}
