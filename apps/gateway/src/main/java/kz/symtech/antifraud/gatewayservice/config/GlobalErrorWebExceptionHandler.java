package kz.symtech.antifraud.gatewayservice.config;

import kz.symtech.antifraud.gatewayservice.helpers.CatalogOperationExceptionGenerator;
import kz.symtech.antifraud.gatewayservice.dto.error.CatalogOperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Order(-2)
@Slf4j
public class GlobalErrorWebExceptionHandler extends
        AbstractErrorWebExceptionHandler {


    private final ObjectProvider<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;


    public GlobalErrorWebExceptionHandler(ObjectProvider<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer,
                                          ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext) {
        super(errorAttributes, resources, applicationContext);
        this.viewResolvers = viewResolvers;
        this.serverCodecConfigurer = serverCodecConfigurer;
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        super.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
    }
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(
            ErrorAttributes errorAttributes) {

        return RouterFunctions.route(
                RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(
            ServerRequest request) {
        Map<String, Object> errorPropertiesMap = new HashMap<>();
        CatalogOperationException ex = CatalogOperationExceptionGenerator.generateRuntimeException();
        errorPropertiesMap.put("code", ex.getCode());
        errorPropertiesMap.put("status", ex.getHttpStatus().value());
        errorPropertiesMap.put("message", ex.getMessage());
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }
}

