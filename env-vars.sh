export PROFILE=prod,db
export NODE_OPTIONS='--max_old_space_size=2560'

export POSTGRES_DB=anti_fraud
export DATASOURCE_URL=jdbc:postgresql://db:5432/${POSTGRES_DB}
export DATASOURCE_USERNAME=anti_fraud
export DATASOURCE_PASSWORD=NbEMagx0LvJ0VMWuqqc8HXa89o0AcaTXjUCqtp7Qziat4LsN0Rki8dyXi

export ADMIN_SERVICE=http://admin:8110
export AUTH_SERVICE=http://auth:8100
export CORE_SERVICE=http://core:8095
export MAIL_SERVICE=http://mail:8090
export TEST_SERVICE=http://test:8096
export CONNECTOR_HANDLER_SERVICE=http://connector-handler:8097
export REDIS_HOST_CORE=http://redis-master
export REDIS_PORT=6379
export DOMAIN=antifraud-test.symtech-cloud.kz
export GATEWAY=http://gateway:8080
export LOGSTASH_URL=${DOMAIN}
export SSL_ENABLED=true
export EMAIL=noreply@symtech.kz
export GATEWAY_URL=https://${DOMAIN}

export ELASTICSEARCH_HOST_CORE=${DOMAIN}
export ELASTICSEARCH_HOST=http://elasticsearch
export ELASTICSEARCH_PORT=9200
export ELASTICSEARCH_USERNAME=elastic
export ELASTICSEARCH_PASSWORD=elastic

export REDIS_HOST_CORE=${DOMAIN}
export REDIS_PASSWORD=redispassword