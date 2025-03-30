package oleksandr.jobbit_back.repository;

import jakarta.transaction.Transactional;
import oleksandr.jobbit_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("update User u set u.userPassword = ?2 where u.email = ?1")
    void updatePassword(String email, String password);

    @Modifying
    @Transactional
    @Query("update User u set u.lastPasswordChanged = ?2 where u.email = ?1")
    void updatePasswordChangingData(String email, LocalDateTime date);

    @Query("select u from User u where u.verificationToken = ?1")
    User findByVerificationToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.verified = true, u.verificationToken = NULL WHERE u.verificationToken = ?1")
    void verifyUser(String token);
}
