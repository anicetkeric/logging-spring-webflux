package com.bootlabs.logging.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.Sink;
import org.zalando.logbook.autoconfigure.webflux.LogbookWebFluxAutoConfiguration;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.logstash.LogstashLogbackSink;
import org.zalando.logbook.spring.webflux.LogbookExchangeFilterFunction;
import org.zalando.logbook.spring.webflux.LogbookWebFilter;

@Configuration
@Import(LogbookWebFluxAutoConfiguration.class)
public class LogbookConfiguration {

    @Bean
    public WebFilter logbookFilter(Logbook logbook) {
        return new LogbookWebFilter(logbook);
    }

    @Bean
    public ExchangeFilterFunction logbookClientExchangeFunction(final Logbook logbook) {
        return new LogbookExchangeFilterFunction(logbook);
    }

    @Bean
    public WebClient webClient(final ExchangeFilterFunction logbookClientExchangeFunction) {
        return WebClient.builder()
                .filter(logbookClientExchangeFunction)
                .build();
    }

    @Bean
    public Sink sink(ObjectMapper objectMapper) {
        HttpLogFormatter formatter = new JsonHttpLogFormatter(objectMapper);
        return new LogstashLogbackSink(formatter);
    }

}
