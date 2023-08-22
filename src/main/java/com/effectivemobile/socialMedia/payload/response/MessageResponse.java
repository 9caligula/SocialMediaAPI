package com.effectivemobile.socialMedia.payload.response;

public class MessageResponse {

    private String successMessage;

    public MessageResponse(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
