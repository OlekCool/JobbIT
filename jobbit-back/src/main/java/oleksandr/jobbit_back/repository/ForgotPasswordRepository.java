package oleksandr.jobbit_back.repository;

import jakarta.transaction.Transactional;
import oleksandr.jobbit_back.entity.ForgotPassword;
import oleksandr.jobbit_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

    @Transactional
    @Modifying
    @Query("delete from ForgotPassword fp where fp.fpid = ?1")
    void deleteById(Integer id);
}
