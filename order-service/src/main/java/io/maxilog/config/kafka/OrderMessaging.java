package io.maxilog.config.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface OrderMessaging {

    @Input("input")
    SubscribableChannel input();

    @Output("output-product")
    MessageChannel outputProduct();
}
