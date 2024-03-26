package com.jason.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jason.model.entity.InsuranceEntity;

@Repository
public interface InsuranceRepository extends JpaRepository<InsuranceEntity, String>{
}
