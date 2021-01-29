package io.maxilog.config;

import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.maxilog.config.logging.LoggingAspect;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableAspectJAutoProxy
public class LoggingConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LoggingConfiguration.class);

    public LoggingConfiguration(@Value("${spring.application.name}") String appName,
                                @Value("${server.port}") String serverPort,
                                @Value("${logstash.enabled}") boolean logstashEnabled,
                                @Value("${logstash.host}") String logstashHost,
                                @Value("${logstash.port}") int logstashPort,
                                ObjectMapper mapper) throws JsonProcessingException {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        if(logstashEnabled){
            Map<String, String> map = new HashMap<>();
            map.put("app_name", appName);
            map.put("app_port", serverPort);
            String customFields = mapper.writeValueAsString(map);

            log.info("Initializing Logstash loggingProperties");
            LogstashTcpSocketAppender logstashAppender = new LogstashTcpSocketAppender();
            logstashAppender.addDestinations(new InetSocketAddress(logstashHost, logstashPort));
            logstashAppender.setContext(context);
            logstashAppender.setEncoder(logstashEncoder(customFields));
            logstashAppender.setName("ASYNC_LOGSTASH");
            logstashAppender.setQueueSize(512);
            logstashAppender.start();
            context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).addAppender(logstashAppender);
        }
    }


    @Bean
    //@Profile("dev")
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }

    private static LogstashEncoder logstashEncoder(String customFields) {
        LogstashEncoder logstashEncoder = new LogstashEncoder();
        logstashEncoder.setThrowableConverter(new ShortenedThrowableConverter());
        logstashEncoder.setCustomFields(customFields);
        return logstashEncoder;
    }
}
