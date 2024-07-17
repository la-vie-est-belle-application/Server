package lavi.scheduler.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"kakao_account"})
@Getter
public class KakaoInfo {
    private Long id;
    private LocalDateTime connectedAt;
    private Properties properties;

    @JsonCreator
    public KakaoInfo(@JsonProperty("id") Long id,
                     @JsonProperty("connected_at") LocalDateTime connectedAt,
                     @JsonProperty("properties") Properties properties) {
        this.id = id;
        this.connectedAt = connectedAt;
        this.properties = properties;
    }

    @Getter
    public static class Properties {
        private String nickName;
        private String profileImage;
        private String thumbnailImage;

        @JsonCreator
        public Properties(@JsonProperty("nickname") String nickName,
                          @JsonProperty("profile_image") String profileImage,
                          @JsonProperty("thumbnail_image") String thumbnailImage) {
            this.nickName = nickName;
            this.profileImage = profileImage;
            this.thumbnailImage = thumbnailImage;
        }
    }


}
