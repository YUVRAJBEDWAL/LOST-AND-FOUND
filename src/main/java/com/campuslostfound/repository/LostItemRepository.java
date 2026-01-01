package com.campuslostfound.repository;

import com.campuslostfound.entity.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    
    List<LostItem> findByUserId(Long userId);
    
    List<LostItem> findByStatus(LostItem.ItemStatus status);
    
    List<LostItem> findByCategory(LostItem.Category category);
    
    @Query("SELECT li FROM LostItem li WHERE li.itemName LIKE %:keyword% OR li.description LIKE %:keyword% OR li.location LIKE %:keyword%")
    List<LostItem> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT li FROM LostItem li WHERE li.lostDate BETWEEN :startDate AND :endDate")
    List<LostItem> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT li FROM LostItem li WHERE li.status = :status AND li.category = :category")
    List<LostItem> findByStatusAndCategory(@Param("status") LostItem.ItemStatus status, @Param("category") LostItem.Category category);
    
    @Query("SELECT COUNT(li) FROM LostItem li WHERE li.status = 'OPEN'")
    long countOpenItems();
    
    @Query("SELECT COUNT(li) FROM LostItem li WHERE li.status = 'CLAIMED'")
    long countClaimedItems();
    
    @Query("SELECT li FROM LostItem li ORDER BY li.createdAt DESC")
    List<LostItem> findRecentItems();
}
