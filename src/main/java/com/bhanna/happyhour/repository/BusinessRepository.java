package com.bhanna.happyhour.repository;

import com.bhanna.happyhour.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findBusinessByYelpId(String yelpId);
    List<Business> findByYelpIdIn(List<String> yelpIds);

    @Query("SELECT b FROM Business b LEFT JOIN FETCH b.specialDetailList s WHERE b.id = :id")
    Optional<Business> findBusinessAndSpecialDetailsEagerly(@Param("id") Long id);

    @Query("SELECT b FROM Business b LEFT JOIN FETCH b.specialDetailList s WHERE b.yelpId = :yelpId")
    Optional<Business> findBusinessAndSpecialDetailsByYelpIdEagerly(@Param("yelpId") String yelpId);

}
