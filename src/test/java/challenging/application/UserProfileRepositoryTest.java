package challenging.application;


import challenging.application.model.UserProfile;
import challenging.application.repository.UserProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    @DisplayName("Member의 id로 프로필 조회")
    void testFindByUserId() {
        // given
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(1L);
        userProfileRepository.save(userProfile);

        // when
        Optional<UserProfile> result = userProfileRepository.findByUserId(1L);

        // then
        assertTrue(result.isPresent());
    }
}
