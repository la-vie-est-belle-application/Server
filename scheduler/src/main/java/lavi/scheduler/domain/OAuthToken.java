package lavi.scheduler.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties({"token_type", "id_token", "expires_in", "refresh_token", "refresh_token_expires_in", "scope"})
@Getter
public class OAuthToken {
    private String accessToken;

    @JsonCreator
    public OAuthToken(@JsonProperty("access_token") String accessToken) {
        this.accessToken = accessToken;
    }
}
