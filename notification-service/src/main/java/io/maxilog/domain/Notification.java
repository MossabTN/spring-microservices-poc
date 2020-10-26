package io.maxilog.domain;

import java.time.LocalDateTime;

public class Notification {

    private String sender;
    private String payload;
    private LocalDateTime createAt;

    public Notification() {
    }

    public Notification(String payload) {
        this.payload = payload;
    }

    public Notification(String sender, String payload) {
        this.sender = sender;
        this.payload = payload;
        this.createAt = LocalDateTime.now();
    }

    public Notification(String sender, String payload, LocalDateTime createAt) {
        this.sender = sender;
        this.payload = payload;
        this.createAt = createAt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
