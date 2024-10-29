package challenging.application.userprofile.domain;

import challenging.application.auth.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Null;

@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "member_id")
    Member user;
    private String userNickName;

    private Integer point;
    private String imageExtension;

    public UserProfile() {}

    public UserProfile(Long id, Member user, String userNickName, Integer point) {
        this.id = id;
        this.user = user;
        this.userNickName = userNickName;
        this.point = point;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public Integer getPoint() {
        return point;
    }

    public String getImageExtension() {
        return imageExtension;
    }

    public Member getUser() {
        return user;
    }

    public void updateUserNickName(String userNickName) {
        if (userNickName != null){
            this.userNickName = userNickName;
        }
    }

    public void updateImageExtension(String imageExtension) {
        if (imageExtension != null){
            this.imageExtension = imageExtension;
        }
    }
}
