package com.beamofsoul.rabbit.primary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.beamofsoul.rabbit.management.repository.BaseMultielementRepository;
import com.beamofsoul.rabbit.primary.entity.User;

/**
 * {@link JpaRepository} provides basic CRUD and querying (including paged query) functionalities
 * {@link JpaSpecificationExecutor} provides compound querying function
 * @author MingshuJian
 */
@Repository
public interface UserRepository extends BaseMultielementRepository<User, Long> {

	User findByUsername(String username);
}
