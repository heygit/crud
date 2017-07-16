package project.service.transport.exception.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteNotificationMessage {

    @JsonProperty("messageLocalizationId")
    private String messageLocalizationId;

    @JsonProperty("messageLocalizationInfoParameters")
    private List<String> messageLocalizationInfoParameters;

    @JsonProperty("notificationMessageType")
    private String notificationMessageType;

    public String getMessageLocalizationId() {
        return messageLocalizationId;
    }

    public void setMessageLocalizationId(String messageLocalizationId) {
        this.messageLocalizationId = messageLocalizationId;
    }

    public List<String> getMessageLocalizationInfoParameters() {
        return messageLocalizationInfoParameters;
    }

    public void setMessageLocalizationInfoParameters(List<String> messageLocalizationInfoParameters) {
        this.messageLocalizationInfoParameters = messageLocalizationInfoParameters;
    }

    public String getNotificationMessageType() {
        return notificationMessageType;
    }

    public void setNotificationMessageType(String notificationMessageType) {
        this.notificationMessageType = notificationMessageType;
    }
}