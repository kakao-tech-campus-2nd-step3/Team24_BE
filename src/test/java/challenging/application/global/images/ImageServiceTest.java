package challenging.application.global.images;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @MockBean
    private S3Client s3Client;

    @Mock
    private MultipartFile multipartFile;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketUserprofile;

    @Value("${cloud.aws.s3.bucket2}")
    private String bucketChallenge;

    @Value("${cloud.aws.region.static}")
    private String region;

    private static final String TEST_LOCAL_LOCATION = "C:\\picture\\";
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        // ReflectionTestUtils를 사용하여 private 필드를 설정합니다.
        ReflectionTestUtils.setField(imageService, "localLocation", TEST_LOCAL_LOCATION);

        tempFile = File.createTempFile("test", ".jpg");
        Files.write(tempFile.toPath(), "Test Content".getBytes());

        lenient().when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        lenient().when(multipartFile.isEmpty()).thenReturn(false);

        doAnswer(invocation -> {
            File file = invocation.getArgument(0);
            Files.copy(tempFile.toPath(), file.toPath());
            return null;
        }).when(multipartFile).transferTo(any(File.class));
    }

    @AfterEach
    void tearDown() throws IOException {
        Path filePath = Paths.get(TEST_LOCAL_LOCATION + "1.jpg");
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    @Test
    void 사용자프로필이미지업로드_테스트() {
        Long userId = 1L;
        String expectedUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketUserprofile, region, userId + ".jpg");
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);
        String s3Url = imageService.imageloadUserProfile(multipartFile, userId);
        assertEquals(expectedUrl, s3Url);
    }

    @Test
    void 챌린지이미지업로드_테스트() {
        Long challengeId = 1L;
        String expectedUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketChallenge, region, challengeId + ".jpg");
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);
        String s3Url = imageService.imageloadChallenge(multipartFile, challengeId);
        assertEquals(expectedUrl, s3Url);
    }

    @Test
    void 사용자프로필이미지삭제_테스트() {
        String s3Url = String.format("https://%s.s3.%s.amazonaws.com/1.jpg", bucketUserprofile, region);
        when(s3Client.deleteObject(any(DeleteObjectRequest.class))).thenReturn(null);
        assertDoesNotThrow(() -> imageService.deleteImageUserProfile(s3Url));
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void 챌린지이미지삭제_테스트() {
        String s3Url = String.format("https://%s.s3.%s.amazonaws.com/1.jpg", bucketChallenge, region);
        when(s3Client.deleteObject(any(DeleteObjectRequest.class))).thenReturn(null);
        assertDoesNotThrow(() -> imageService.deleteImageChallenge(s3Url));
        verify(s3Client, times(1)).deleteObject(any(DeleteObjectRequest.class));
    }

    @Test
    void 빈파일업로드_예외처리_테스트() {
        when(multipartFile.isEmpty()).thenReturn(true);
        Exception exception = assertThrows(RuntimeException.class,
            () -> imageService.imageloadUserProfile(multipartFile, 1L));
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("업로드할 파일이 없습니다.", exception.getCause().getMessage());
    }

    @Test
    void S3업로드_예외처리_테스트() {
        when(multipartFile.isEmpty()).thenReturn(false);
        doThrow(new RuntimeException("S3 업로드 중 오류 발생")).when(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> imageService.imageloadUserProfile(multipartFile, 1L));

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("파일 업로드 중 오류가 발생했습니다.") ||
            exception.getCause() != null && exception.getCause().getMessage().contains("S3 업로드 중 오류 발생"));
    }

    @Test
    void 다양한파일확장자_업로드_테스트() throws IOException {
        String[] extensions = { ".jpg", ".png", ".gif" };
        for (String ext : extensions) {
            when(multipartFile.getOriginalFilename()).thenReturn("test" + ext);
            String expectedUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketUserprofile, region, "1" + ext);
            when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);
            String s3Url = imageService.imageloadUserProfile(multipartFile, 1L);
            assertEquals(expectedUrl, s3Url);
        }
    }

    @Test
    void 동일한ID_파일이름_충돌_테스트() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        String expectedUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketUserprofile, region, "1.jpg");
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);

        String s3Url1 = imageService.imageloadUserProfile(multipartFile, 1L);
        String s3Url2 = imageService.imageloadUserProfile(multipartFile, 1L);

        assertEquals(expectedUrl, s3Url1);
        assertEquals(expectedUrl, s3Url2);
    }

    @Test
    void 대용량파일_업로드_테스트() throws IOException {
        tempFile = File.createTempFile("large", ".jpg");
        byte[] largeContent = new byte[20 * 1024 * 1024];
        Files.write(tempFile.toPath(), largeContent);
        doAnswer(invocation -> {
            File file = invocation.getArgument(0);
            Files.copy(tempFile.toPath(), file.toPath());
            return null;
        }).when(multipartFile).transferTo(any(File.class));

        when(multipartFile.getOriginalFilename()).thenReturn("large.jpg");
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);
        String expectedUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketUserprofile, region, "1.jpg");
        String s3Url = imageService.imageloadUserProfile(multipartFile, 1L);
        assertEquals(expectedUrl, s3Url);
    }

    @Test
    void S3URL형식_검증_테스트() {
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        String expectedUrlPattern = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketUserprofile, region, "1.jpg");
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class))).thenReturn(null);
        String s3Url = imageService.imageloadUserProfile(multipartFile, 1L);
        assertTrue(s3Url.matches(expectedUrlPattern.replace("1.jpg", ".*")));
    }
}
