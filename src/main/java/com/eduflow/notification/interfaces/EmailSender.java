package com.eduflow.notification.interfaces;

public interface EmailSender {
    void sendHtml(String to, String subject, String html);
}
