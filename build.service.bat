call
mvn clean -f base/pom.xml && mvn install -f base/pom.xml^
 && mvn clean -f models/pom.xml && mvn install -f models/pom.xml^
 && mvn clean -f feign-clients/pom.xml && mvn install -f feign-clients/pom.xml^
 && mvn package^
 && docker build -t admin-service:latest -f Dockerfile apps/admin-service^
 && docker build -t auth-service:latest -f Dockerfile apps/auth-service^
 && docker build -t core-service:latest -f Dockerfile apps/core-service^
 && docker build -t mail-service:latest -f Dockerfile apps/mail-service^
 && docker build -t gateway:latest -f Dockerfile apps/gateway