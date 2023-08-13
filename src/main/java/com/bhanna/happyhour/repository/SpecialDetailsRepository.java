package com.bhanna.happyhour.repository;

import com.bhanna.happyhour.model.SpecialDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpecialDetailsRepository extends JpaRepository<SpecialDetail, UUID> {
}
