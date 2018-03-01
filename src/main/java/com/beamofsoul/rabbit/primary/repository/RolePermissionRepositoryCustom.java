package com.beamofsoul.rabbit.primary.repository;

import java.util.List;

import com.beamofsoul.rabbit.primary.entity.dto.RolePermissionDTO;

public interface RolePermissionRepositoryCustom<T,ID> {

	List<RolePermissionDTO> findAllRolePermissionMapping();
}
