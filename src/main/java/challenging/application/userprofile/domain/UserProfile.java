package challenging.application.userprofile.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    private Long userId;  // id값 Long으로 변경
    private String userNickName;
    private String imageUrl;
    private Integer point;

    // getter와 setter
    public Long getProfileId() {
        return profileId;
    }

    public Long getUserId() {  // Long으로 수정
        return userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getPoint() {
        return point;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public void setUserId(Long userId) {  // Long으로 수정
        this.userId = userId;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
