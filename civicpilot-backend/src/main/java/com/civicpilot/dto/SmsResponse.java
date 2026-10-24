
package com.civicpilot.dto;

public class SmsResponse {

    private boolean success;
    private String message;
    private String messageSid;

    public SmsResponse() {
    }

    public SmsResponse(
            boolean success,
            String message,
            String messageSid) {
        this.success = success;
        this.message = message;
        this.messageSid = messageSid;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageSid() {
        return messageSid;
    }

    public void setMessageSid(String messageSid) {
        this.messageSid = messageSid;
    }
}
