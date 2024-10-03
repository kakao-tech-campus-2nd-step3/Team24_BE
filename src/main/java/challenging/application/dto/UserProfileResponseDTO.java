package challenging.application.dto;


public class UserProfileResponseDTO {
    private String userNickName;
    private String imageUrl;
    private Integer point;

    // Constructor
    public UserProfileResponseDTO(String userNickName,  String imageUrl, Integer point) {
        this.userNickName = userNickName;
        this.imageUrl = imageUrl;
        this.point = point;
    }

    // Getter and Setter
    public String getUserNickName() {
        return userNickName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getPoint() {
        return point;
    }
}
