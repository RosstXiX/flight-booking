package io.github.rosstxix.flightbooking.infrastructure.logging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class HttpLoggingConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 10)
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();

        filter.setIncludeQueryString(true); // Logging for ?from=...
        filter.setIncludePayload(false);    // Not logging request body
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA: [");

        return filter;
    }
}
