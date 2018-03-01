package com.beamofsoul.rabbit.primary.repository;


import org.springframework.stereotype.Repository;

import com.beamofsoul.rabbit.management.repository.BaseMultielementRepository;
import com.beamofsoul.rabbit.primary.entity.Role;

@Repository
public interface RoleRepository extends BaseMultielementRepository<Role,Long> {
	
	Role findByName(String name);
}
