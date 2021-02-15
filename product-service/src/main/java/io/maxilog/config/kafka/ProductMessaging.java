package io.maxilog.config.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface ProductMessaging {

    @Input("input")
    SubscribableChannel input();
}
