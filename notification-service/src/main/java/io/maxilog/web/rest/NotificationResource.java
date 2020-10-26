package io.maxilog.web.rest;

import io.maxilog.domain.Notification;
import io.maxilog.service.NotificationService;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController()
@RequestMapping("/api")
public class NotificationResource {

    private final NotificationService notificationService;

    public NotificationResource(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @PermitAll()
    @GetMapping("/notifications/{username}")
    public ResponseEntity<?> sendTo(@PathVariable("username") String username){
        notificationService.notifyUser(username, null);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/notifications/")
    public ResponseEntity<Page<Notification>> getNotification(@ApiParam Pageable pageable){
        return ResponseEntity.ok().body(new PageImpl<>(Arrays.asList(
                new Notification("SYSTEM", "new Order#100"),
                new Notification("SYSTEM", "new Order#124")
        )));
    }
}