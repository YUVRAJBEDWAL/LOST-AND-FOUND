package com.campuslostfound.service;

import com.campuslostfound.entity.LostItem;
import com.campuslostfound.entity.User;
import com.campuslostfound.repository.LostItemRepository;
import com.campuslostfound.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class LostItemService {

    @Autowired
    private LostItemRepository lostItemRepository;

    public Page<LostItem> getAllLostItems(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable must not be null");
        return lostItemRepository.findAll(pageable);
    }

    public LostItem getLostItemById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return lostItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lost item not found with id: " + id));
    }

    public List<LostItem> getLostItemsByUserId(Long userId) {
        return lostItemRepository.findByUserId(userId);
    }

    public List<LostItem> searchLostItems(String keyword) {
        return lostItemRepository.searchByKeyword(keyword);
    }

    public List<LostItem> filterLostItems(LostItem.Category category, LostItem.ItemStatus status) {
        if (category != null && status != null) {
            return lostItemRepository.findByStatusAndCategory(status, category);
        } else if (category != null) {
            return lostItemRepository.findByCategory(category);
        } else if (status != null) {
            return lostItemRepository.findByStatus(status);
        } else {
            return lostItemRepository.findAll();
        }
    }

    public List<LostItem> getLostItemsByDateRange(LocalDate startDate, LocalDate endDate) {
        return lostItemRepository.findByDateRange(startDate, endDate);
    }

    public LostItem createLostItem(LostItem lostItem, User user) {
        Objects.requireNonNull(lostItem, "lostItem must not be null");
        Objects.requireNonNull(user, "user must not be null");
        lostItem.setUser(user);
        return Objects.requireNonNull(lostItemRepository.save(lostItem), "saved lostItem must not be null");
    }

    public LostItem updateLostItem(Long id, LostItem lostItemDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(lostItemDetails, "lostItemDetails must not be null");
        LostItem lostItem = getLostItemById(id);
        
        lostItem.setItemName(lostItemDetails.getItemName());
        lostItem.setDescription(lostItemDetails.getDescription());
        lostItem.setLocation(lostItemDetails.getLocation());
        lostItem.setLostDate(lostItemDetails.getLostDate());
        lostItem.setCategory(lostItemDetails.getCategory());
        lostItem.setPhotoUrl(lostItemDetails.getPhotoUrl());
        
        return Objects.requireNonNull(lostItemRepository.save(lostItem), "updated lostItem must not be null");
    }

    public LostItem updateLostItemStatus(Long id, LostItem.ItemStatus status) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(status, "status must not be null");
        LostItem lostItem = getLostItemById(id);
        lostItem.setStatus(status);
        return Objects.requireNonNull(lostItemRepository.save(lostItem), "updated lostItem must not be null");
    }

    public void deleteLostItem(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        LostItem lostItem = getLostItemById(id);
        lostItemRepository.delete(lostItem);
    }

    public long getOpenLostItemsCount() {
        return lostItemRepository.countOpenItems();
    }

    public long getClaimedLostItemsCount() {
        return lostItemRepository.countClaimedItems();
    }

    public List<LostItem> getRecentLostItems() {
        return lostItemRepository.findRecentItems();
    }
}
