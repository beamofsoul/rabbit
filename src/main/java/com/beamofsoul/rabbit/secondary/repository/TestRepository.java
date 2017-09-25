package com.beamofsoul.rabbit.secondary.repository;

import org.springframework.stereotype.Repository;

import com.beamofsoul.rabbit.management.repository.BaseMultielementRepository;
import com.beamofsoul.rabbit.secondary.entity.Test;

@Repository
public interface TestRepository extends BaseMultielementRepository<Test, Long> {

}
