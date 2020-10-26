package io.maxilog.service;

import io.maxilog.domain.Notification;

public interface NotificationService {

    void notifyUser(String username, Notification body);

    void notifyAll(Notification body);
}
