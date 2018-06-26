package com.supinfo.ourcloud.repository;

import com.supinfo.ourcloud.domain.UserSup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UserSup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSupRepository extends JpaRepository<UserSup, Long> {

}
