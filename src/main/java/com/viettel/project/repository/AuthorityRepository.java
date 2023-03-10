package com.viettel.project.repository;

import com.viettel.project.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByMethodNameAndPathAndStatus(String methodName, String path, int status);
}
