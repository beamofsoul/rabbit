package com.beamofsoul.rabbit.primary.repository;

import org.springframework.stereotype.Repository;

import com.beamofsoul.rabbit.management.repository.BaseMultielementRepository;
import com.beamofsoul.rabbit.primary.entity.User;

@Repository
public interface UserRepository extends BaseMultielementRepository<User, Long> {

}
