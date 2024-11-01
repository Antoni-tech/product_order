package kz.symtech.antifraud.gatewayservice.config;

import kz.symtech.antifraud.gatewayservice.helpers.CatalogOperationExceptionGenerator;
import kz.symtech.antifraud.gatewayservice.dto.UserTokenValidationResponse;
import kz.symtech.antifraud.gatewayservice.dto.error.CatalogOperationException;
import kz.symtech.antifraud.gatewayservice.helpers.SimpleACL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;


/**
 * Данный класс представляет собой аутентификационный фильтр
 * в системе ТФ
 */
@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final ApiConfig apiConfig;
    private final WebClient.Builder webclientBuilder;

    private final SimpleACL simpleACL;

    public AuthFilter(WebClient.Builder webclientBuilder, ApiConfig apiConfig) {
        super(Config.class);
        this.webclientBuilder = webclientBuilder;
        simpleACL = new SimpleACL();
        this.apiConfig = apiConfig;
    }

    /**
     * Данный фильтр проверяет валидность токена при каждом запросе
     * для которого включен этот фильтр
     * <p>
     * Включение и отключание осуществляется изменением файла application.yml
     *
     * @param config
     * @return
     */
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            try {
                HttpCookie cookie = exchange.getRequest().getCookies().getFirst("token");
                if (Objects.isNull(cookie)) {
                    cookie = new HttpCookie("token", "---");
                }

                return webclientBuilder.build()
                        .post()
                        .uri(apiConfig.getAuthServiceUrl() + "/validate")
                        .cookie(cookie.getName(), cookie.getValue())
                        .retrieve()
                        .onStatus(HttpStatus::isError, clientResponse -> {
                            if (HttpStatus.UNAUTHORIZED.equals(clientResponse.statusCode())) {
                                return Mono.error(CatalogOperationExceptionGenerator.generateAuthFailException());
                            }
                            return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                                log.error("Auth Service Error: {}", errorBody);
                                return Mono.error(new RuntimeException("Auth Service Error: " + errorBody));
                            });
                        })
                        .bodyToMono(UserTokenValidationResponse.class)
                        .mapNotNull(res -> {
                            // При успешной аутентификации прокидываем
                            // в заголовки запроса логин пользователя
                            exchange.getRequest().mutate().header("X-Username", res.getLogin());
                            try {
                                if (simpleACL.canAccess(res.getPrivileges(), exchange.getRequest().getURI().getPath())) {
                                    return exchange;
                                }
                            } catch (CatalogOperationException e) {
                                throw new RuntimeException(e);
                            }
                            return null;
                        })
                        .map(res -> {
                                    log.info("Request : ");
                                    return exchange;
                                }
                        )
                        .flatMap(chain::filter);

            } catch (RuntimeException ex) {
                log.error("Request Error: " + ex.getMessage());
                return Mono.error(CatalogOperationExceptionGenerator.generateAuthFailException());
            }
        });
    }

    public static class Config {
    }
}
