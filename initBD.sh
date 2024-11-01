#!/bin/bash
echo "Start added bd env $DATASOURCE_USERNAME_CORE"
psql -U anti_fraud_common -d anti_fraud_common -c "CREATE DATABASE $DATASOURCE_USERNAME_CORE"
psql -U anti_fraud_common -d anti_fraud_common -c "CREATE USER $DATASOURCE_USERNAME_CORE WITH PASSWORD '${DATASOURCE_PASSWORD_CORE}'"