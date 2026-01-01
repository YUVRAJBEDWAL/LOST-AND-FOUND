package com.campuslostfound.repository;

import com.campuslostfound.entity.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
    
    List<FoundItem> findByUserId(Long userId);
    
    List<FoundItem> findByStatus(FoundItem.ItemStatus status);
    
    List<FoundItem> findByCategory(FoundItem.Category category);
    
    @Query("SELECT fi FROM FoundItem fi WHERE fi.itemName LIKE %:keyword% OR fi.description LIKE %:keyword% OR fi.location LIKE %:keyword%")
    List<FoundItem> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT fi FROM FoundItem fi WHERE fi.foundDate BETWEEN :startDate AND :endDate")
    List<FoundItem> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT fi FROM FoundItem fi WHERE fi.status = :status AND fi.category = :category")
    List<FoundItem> findByStatusAndCategory(@Param("status") FoundItem.ItemStatus status, @Param("category") FoundItem.Category category);
    
    @Query("SELECT COUNT(fi) FROM FoundItem fi WHERE fi.status = 'OPEN'")
    long countOpenItems();
    
    @Query("SELECT COUNT(fi) FROM FoundItem fi WHERE fi.status = 'CLAIMED'")
    long countClaimedItems();
    
    @Query("SELECT fi FROM FoundItem fi ORDER BY fi.createdAt DESC")
    List<FoundItem> findRecentItems();
    
    @Query("SELECT fi FROM FoundItem fi WHERE fi.location = :location AND fi.status = 'OPEN'")
    List<FoundItem> findOpenItemsByLocation(@Param("location") String location);
}
