package com.timvero.example.admin.application.entity;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    @Query("SELECT a.status FROM Application a WHERE a.id = :applicationId")
    Optional<ApplicationStatus> findStatusById(UUID applicationId);
}
