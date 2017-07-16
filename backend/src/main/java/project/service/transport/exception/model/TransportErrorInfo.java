package project.service.transport.exception.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportErrorInfo {

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("userMessage")
    private String userMessage;

    @JsonProperty("remoteMessage")
    private String remoteMessage;

    @JsonProperty("remoteErrorCode")
    private String remoteErrorCode;

    @JsonProperty("remoteNotificationMessages")
    private List<RemoteNotificationMessage> remoteNotificationMessages;

    @JsonProperty("debugMessage")
    private DebugMessage debugMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public DebugMessage getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(DebugMessage debugMessage) {
        this.debugMessage = debugMessage;
    }

    public String getRemoteMessage() {
        return remoteMessage;
    }

    public void setRemoteMessage(String remoteMessage) {
        this.remoteMessage = remoteMessage;
    }

    public String getRemoteErrorCode() {
        return remoteErrorCode;
    }

    public void setRemoteErrorCode(String remoteErrorCode) {
        this.remoteErrorCode = remoteErrorCode;
    }

    public List<RemoteNotificationMessage> getRemoteNotificationMessages() {
        return remoteNotificationMessages;
    }

    public void setRemoteNotificationMessages(List<RemoteNotificationMessage> remoteNotificationMessages) {
        this.remoteNotificationMessages = remoteNotificationMessages;
    }
}