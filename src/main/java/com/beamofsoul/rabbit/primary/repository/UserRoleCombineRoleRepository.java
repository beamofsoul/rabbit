package com.beamofsoul.rabbit.primary.repository;

import org.springframework.stereotype.Repository;

import com.beamofsoul.rabbit.management.repository.BaseMultielementRepository;
import com.beamofsoul.rabbit.primary.entity.UserRoleCombineRole;

@Repository
public interface UserRoleCombineRoleRepository extends BaseMultielementRepository<UserRoleCombineRole, Long> {

}
