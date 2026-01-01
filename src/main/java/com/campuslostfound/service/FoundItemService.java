package com.campuslostfound.service;

import com.campuslostfound.entity.FoundItem;
import com.campuslostfound.entity.User;
import com.campuslostfound.exception.ResourceNotFoundException;
import com.campuslostfound.repository.FoundItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@SuppressWarnings("null")
public class FoundItemService {

    @Autowired
    private FoundItemRepository foundItemRepository;

    public Page<FoundItem> getAllFoundItems(Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable must not be null");
        return foundItemRepository.findAll(pageable);
    }

    public FoundItem getFoundItemById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return foundItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Found item not found with id: " + id));
    }

    public List<FoundItem> getFoundItemsByUserId(Long userId) {
        return foundItemRepository.findByUserId(userId);
    }

    public List<FoundItem> searchFoundItems(String keyword) {
        return foundItemRepository.searchByKeyword(keyword);
    }

    public List<FoundItem> filterFoundItems(FoundItem.Category category, FoundItem.ItemStatus status) {
        if (category != null && status != null) {
            return foundItemRepository.findByStatusAndCategory(status, category);
        } else if (category != null) {
            return foundItemRepository.findByCategory(category);
        } else if (status != null) {
            return foundItemRepository.findByStatus(status);
        } else {
            return foundItemRepository.findAll();
        }
    }

    public List<FoundItem> getFoundItemsByDateRange(LocalDate startDate, LocalDate endDate) {
        return foundItemRepository.findByDateRange(startDate, endDate);
    }

    public FoundItem createFoundItem(FoundItem foundItem, User user) {
        Objects.requireNonNull(foundItem, "foundItem must not be null");
        Objects.requireNonNull(user, "user must not be null");
        foundItem.setUser(user);
        return Objects.requireNonNull(foundItemRepository.save(foundItem), "saved foundItem must not be null");
    }

    public FoundItem updateFoundItem(Long id, FoundItem foundItemDetails) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(foundItemDetails, "foundItemDetails must not be null");
        FoundItem foundItem = getFoundItemById(id);

        foundItem.setItemName(foundItemDetails.getItemName());
        foundItem.setDescription(foundItemDetails.getDescription());
        foundItem.setLocation(foundItemDetails.getLocation());
        foundItem.setFoundDate(foundItemDetails.getFoundDate());
        foundItem.setCategory(foundItemDetails.getCategory());
        foundItem.setPhotoUrl(foundItemDetails.getPhotoUrl());

        return Objects.requireNonNull(foundItemRepository.save(foundItem), "updated foundItem must not be null");
    }

    public FoundItem updateFoundItemStatus(Long id, FoundItem.ItemStatus status) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(status, "status must not be null");
        FoundItem foundItem = getFoundItemById(id);
        foundItem.setStatus(status);
        return Objects.requireNonNull(foundItemRepository.save(foundItem), "updated foundItem must not be null");
    }

    public void deleteFoundItem(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        FoundItem foundItem = getFoundItemById(id);
        foundItemRepository.delete(foundItem);
    }

    public long getOpenFoundItemsCount() {
        return foundItemRepository.countOpenItems();
    }

    public long getClaimedFoundItemsCount() {
        return foundItemRepository.countClaimedItems();
    }

    public List<FoundItem> getRecentFoundItems() {
        return foundItemRepository.findRecentItems();
    }

    public List<FoundItem> getOpenFoundItemsByLocation(String location) {
        return foundItemRepository.findOpenItemsByLocation(location);
    }
}
