package io.maxilog.service.impl;

import io.maxilog.chat.NotificationKafka;
import io.maxilog.domain.Notification;
import io.maxilog.order.OrderAvro;
import io.maxilog.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class NotificationServiceImp implements NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationServiceImp.class);

    private final SimpUserRegistry userRegistry;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImp(SimpUserRegistry userRegistry, SimpMessagingTemplate messagingTemplate) {
        this.userRegistry = userRegistry;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    private void handleSessionConnected(SessionConnectedEvent event) {
        System.out.println(event.toString());
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        System.out.println(event.toString());
    }

    @Override
    public void notifyUser(String username, Notification body) {
        messagingTemplate.convertAndSendToUser(username, "/notifications", body);
    }

    @Override
    public void notifyAll(Notification body) {
        messagingTemplate.convertAndSend("/notifications", body);
    }

    @StreamListener(Sink.INPUT)
    public void consumeKafkaOrder(Message<NotificationKafka> message) {
        LOG.info("receiving order from kafka");
        LOG.info(message.getPayload().toString());
        NotificationKafka notification = message.getPayload();
        notifyUser(notification.getTo(), new Notification(notification.getSender(), notification.getPayload()));

    }
}