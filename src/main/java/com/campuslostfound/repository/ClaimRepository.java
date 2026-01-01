package com.campuslostfound.repository;

import com.campuslostfound.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByUserId(Long userId);

    List<Claim> findByFoundItemId(Long foundItemId);

    List<Claim> findByStatus(Claim.ClaimStatus status);

    @Query("SELECT c FROM Claim c WHERE c.foundItem.user.id = :userId")
    List<Claim> findClaimsForUserItems(@Param("userId") Long userId);

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.status = 'PENDING'")
    long countPendingClaims();

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.status = 'APPROVED'")
    long countApprovedClaims();

    @Query("SELECT c FROM Claim c WHERE c.claimDate BETWEEN :startDate AND :endDate")
    List<Claim> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT c FROM Claim c ORDER BY c.createdAt DESC")
    List<Claim> findRecentClaims();
}
