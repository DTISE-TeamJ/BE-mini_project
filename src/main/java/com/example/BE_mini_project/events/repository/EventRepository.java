package com.example.BE_mini_project.events.repository;

import com.example.BE_mini_project.events.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Events, Long> {
//    List<Events> findByLocation(String location);
//    List<Events> findByDateBetween(Timestamp startDate, Timestamp endDate);
//    List<Events> findByOrganization(String organization);
//    List<Events> findByLocationAndOrganization(String location, String organization);
//    List<Events> findByLocationAndDateBetween(String location, Timestamp startDate, Timestamp endDate);
//    List<Events> findByOrganizationAndDateBetween(String organization, Timestamp startDate, Timestamp endDate);
//    List<Events> findByLocationAndOrganizationAndDateBetween(String location, String organization, Timestamp startDate, Timestamp endDate);

    @Query("SELECT e FROM Events e WHERE " +
            "(:location IS NULL OR e.location = :location) AND " +
            "(:organization IS NULL OR e.organization = :organization) AND " +
            "(:startDate IS NULL OR e.date >= :startDate) AND " +
            "(:endDate IS NULL OR e.date <= :endDate)")
    List<Events> findByFilters(@Param("location") String location,
                               @Param("organization") String organization,
                               @Param("startDate") Timestamp startDate,
                               @Param("endDate") Timestamp endDate);
}
