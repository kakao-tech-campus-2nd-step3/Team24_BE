package challenging.application.dto;


public class UserProfileResponseDTO {
    private String userNickName;
    private String userBody;
    private String imageUrl;
    private Integer point;

    // Constructor
    public UserProfileResponseDTO(String userNickName, String userBody, String imageUrl, Integer point) {
        this.userNickName = userNickName;
        this.userBody = userBody;
        this.imageUrl = imageUrl;
        this.point = point;
    }

    // Getter and Setter
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
}
