package com.beamofsoul.rabbit.primary.repository;

import org.springframework.stereotype.Repository;

import com.beamofsoul.rabbit.management.repository.BaseMultielementRepository;
import com.beamofsoul.rabbit.primary.entity.Permission;

@Repository
public interface PermissionRepository extends BaseMultielementRepository<Permission,Long> {

	Permission findByName(String name);
}
