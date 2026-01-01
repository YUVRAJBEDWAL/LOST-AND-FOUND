package com.campuslostfound.controller;

import com.campuslostfound.entity.LostItem;
import com.campuslostfound.entity.User;
import com.campuslostfound.service.AuthService;
import com.campuslostfound.service.LostItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items/lost")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LostItemController {

    @Autowired
    private LostItemService lostItemService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<Page<LostItem>> getAllLostItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<LostItem> lostItems = lostItemService.getAllLostItems(pageable);
        return ResponseEntity.ok(lostItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LostItem> getLostItemById(@PathVariable Long id) {
        LostItem lostItem = lostItemService.getLostItemById(id);
        return ResponseEntity.ok(lostItem);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LostItem>> getMyLostItems() {
        User currentUser = authService.getCurrentUser();
        List<LostItem> lostItems = lostItemService.getLostItemsByUserId(currentUser.getId());
        return ResponseEntity.ok(lostItems);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LostItem>> searchLostItems(@RequestParam String keyword) {
        List<LostItem> lostItems = lostItemService.searchLostItems(keyword);
        return ResponseEntity.ok(lostItems);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<LostItem>> filterLostItems(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {
        
        LostItem.Category categoryEnum = null;
        LostItem.ItemStatus statusEnum = null;
        
        if (category != null && !category.isEmpty()) {
            try {
                categoryEnum = LostItem.Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid category, will be treated as null
            }
        }
        
        if (status != null && !status.isEmpty()) {
            try {
                statusEnum = LostItem.ItemStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Invalid status, will be treated as null
            }
        }
        
        List<LostItem> lostItems = lostItemService.filterLostItems(categoryEnum, statusEnum);
        return ResponseEntity.ok(lostItems);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<LostItem>> getLostItemsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        
        List<LostItem> lostItems = lostItemService.getLostItemsByDateRange(startDate, endDate);
        return ResponseEntity.ok(lostItems);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<LostItem>> getRecentLostItems() {
        List<LostItem> lostItems = lostItemService.getRecentLostItems();
        return ResponseEntity.ok(lostItems);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LostItem> createLostItem(@Valid @RequestBody LostItem lostItem) {
        User currentUser = authService.getCurrentUser();
        LostItem createdItem = lostItemService.createLostItem(lostItem, currentUser);
        return ResponseEntity.ok(createdItem);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LostItem> updateLostItem(@PathVariable Long id, @Valid @RequestBody LostItem lostItem) {
        LostItem updatedItem = lostItemService.updateLostItem(id, lostItem);
        return ResponseEntity.ok(updatedItem);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LostItem> updateLostItemStatus(
            @PathVariable Long id, 
            @RequestParam LostItem.ItemStatus status) {
        LostItem updatedItem = lostItemService.updateLostItemStatus(id, status);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteLostItem(@PathVariable Long id) {
        lostItemService.deleteLostItem(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getLostItemStats() {
        Map<String, Long> stats = Map.of(
                "openItems", lostItemService.getOpenLostItemsCount(),
                "claimedItems", lostItemService.getClaimedLostItemsCount()
        );
        return ResponseEntity.ok(stats);
    }
}
