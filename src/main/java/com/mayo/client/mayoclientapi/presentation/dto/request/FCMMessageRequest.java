package com.mayo.client.mayoclientapi.presentation.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FCMMessageRequest {
    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Notification notification;
        private String token;
        private Data data;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Data {
        private String initialPageName;
    }

}