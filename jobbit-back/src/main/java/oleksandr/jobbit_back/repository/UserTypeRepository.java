package oleksandr.jobbit_back.repository;

import oleksandr.jobbit_back.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {
    UserType findByUserTypeName(String name);
}
