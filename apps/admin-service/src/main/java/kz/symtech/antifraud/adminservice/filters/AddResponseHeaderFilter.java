package kz.symtech.antifraud.adminservice.filters;

import kz.symtech.antifraud.adminservice.config.OriginConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Данный класс предоставляет функционал
 * модификации ответа сервера, однозначно указывая
 * на разрешенный ресурс
 *
 * В случае, если запрос к сервису будет выполнен с неразрешенного
 * адреса клиента (например, с другого веб сайта)
 * запрос не будет успешным
 */
@Component
public class AddResponseHeaderFilter implements Filter {

    private final OriginConfig originConfig;

    public AddResponseHeaderFilter(OriginConfig originConfig) {
        this.originConfig = originConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader(
                "Access-Control-Allow-Origin", originConfig.getAllowedOrigin()
        );
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {}
}