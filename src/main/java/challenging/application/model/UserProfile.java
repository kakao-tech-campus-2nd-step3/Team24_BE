package challenging.application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    private String userId;
    private String userNickName ;
    private String userBody;
    private String imageUrl;
    private Integer point;


    // getter and setter
    public Long getProfileId() {
        return profileId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public String getUserBody() {
        return userBody;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public void setUserBody(String userBody) {
        this.userBody = userBody;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
