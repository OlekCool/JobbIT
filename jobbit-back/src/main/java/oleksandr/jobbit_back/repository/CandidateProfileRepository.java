package oleksandr.jobbit_back.repository;

import oleksandr.jobbit_back.entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Integer> {
}
