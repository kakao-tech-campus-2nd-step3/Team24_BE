package challenging.application.domain.userprofile.domain;

import challenging.application.domain.auth.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "userProfile")
    Member member;
    private String userNickName;

    private Integer point;

    private String imgUrl;

    public UserProfile() {
        point = 0;
    }

    public UserProfile(Long id, Member member, String userNickName, Integer point) {
        this.id = id;
        this.member = member;
        this.userNickName = userNickName;
        this.point = point;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public Integer getPoint() {
        return point;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Member getMember() {
        return member;
    }

    public void updateUserNickName(String userNickName) {
        if (userNickName != null) {
            this.userNickName = userNickName;
        }
    }

    public void updateImgUrl(String imgUrl) {
        if (imgUrl != null) {
            this.imgUrl = imgUrl;
        }
    }

    public void addPoint(Integer point) {
        this.point += point;
    }

    public void usePoint(Integer point) {
        if(this.point < point){
            throw new IllegalArgumentException("보유 포인트가 부족합니다.");
        }

        this.point -= point;
    }
}
