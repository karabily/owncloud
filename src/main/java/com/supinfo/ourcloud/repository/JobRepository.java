package com.supinfo.ourcloud.repository;

import com.supinfo.ourcloud.domain.Job;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("select job from Job job where job.userToJob.login = ?#{principal.username}")
    List<Job> findByUserToJobIsCurrentUser();

}
