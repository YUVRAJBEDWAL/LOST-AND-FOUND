package com.campuslostfound.controller;

import com.campuslostfound.entity.FoundItem;
import com.campuslostfound.entity.User;
import com.campuslostfound.service.AuthService;
import com.campuslostfound.service.FoundItemService;
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
import java.util.Objects;

@RestController
@RequestMapping("/api/items/found")
@CrossOrigin(origins = "*", maxAge = 3600)
@SuppressWarnings("null")
public class FoundItemController {

    @Autowired
    private FoundItemService foundItemService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<Page<FoundItem>> getAllFoundItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<FoundItem> foundItems = foundItemService.getAllFoundItems(pageable);
        return ResponseEntity.ok(foundItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoundItem> getFoundItemById(@PathVariable Long id) {
        FoundItem foundItem = foundItemService.getFoundItemById(Objects.requireNonNull(id, "id must not be null"));
        return ResponseEntity.ok(foundItem);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<FoundItem>> getMyFoundItems() {
        User currentUser = authService.getCurrentUser();
        List<FoundItem> foundItems = foundItemService.getFoundItemsByUserId(currentUser.getId());
        return ResponseEntity.ok(foundItems);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoundItem>> searchFoundItems(@RequestParam String keyword) {
        List<FoundItem> foundItems = foundItemService.searchFoundItems(keyword);
        return ResponseEntity.ok(foundItems);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<FoundItem>> filterFoundItems(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status) {

        FoundItem.Category categoryEnum = null;
        FoundItem.ItemStatus statusEnum = null;

        if (category != null && !category.isEmpty()) {
            try {
                categoryEnum = FoundItem.Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }

        if (status != null && !status.isEmpty()) {
            try {
                statusEnum = FoundItem.ItemStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }

        List<FoundItem> foundItems = foundItemService.filterFoundItems(categoryEnum, statusEnum);
        return ResponseEntity.ok(foundItems);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<FoundItem>> getFoundItemsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        List<FoundItem> foundItems = foundItemService.getFoundItemsByDateRange(startDate, endDate);
        return ResponseEntity.ok(foundItems);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<FoundItem>> getRecentFoundItems() {
        List<FoundItem> foundItems = foundItemService.getRecentFoundItems();
        return ResponseEntity.ok(foundItems);
    }

    @GetMapping("/location")
    public ResponseEntity<List<FoundItem>> getFoundItemsByLocation(@RequestParam String location) {
        List<FoundItem> foundItems = foundItemService.getOpenFoundItemsByLocation(location);
        return ResponseEntity.ok(foundItems);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FoundItem> createFoundItem(@Valid @RequestBody FoundItem foundItem) {
        User currentUser = authService.getCurrentUser();
        FoundItem createdItem = foundItemService.createFoundItem(foundItem, currentUser);
        return ResponseEntity.ok(createdItem);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FoundItem> updateFoundItem(
            @PathVariable Long id,
            @Valid @RequestBody FoundItem foundItem) {
        FoundItem updatedItem = foundItemService.updateFoundItem(
                Objects.requireNonNull(id, "id must not be null"),
                Objects.requireNonNull(foundItem, "foundItem must not be null")
        );
        return ResponseEntity.ok(updatedItem);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FoundItem> updateFoundItemStatus(
            @PathVariable Long id,
            @RequestParam FoundItem.ItemStatus status) {
        FoundItem updatedItem = foundItemService.updateFoundItemStatus(
                Objects.requireNonNull(id, "id must not be null"),
                Objects.requireNonNull(status, "status must not be null")
        );
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteFoundItem(@PathVariable Long id) {
        foundItemService.deleteFoundItem(Objects.requireNonNull(id, "id must not be null"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getFoundItemStats() {
        Map<String, Long> stats = Map.of(
                "openItems", foundItemService.getOpenFoundItemsCount(),
                "claimedItems", foundItemService.getClaimedFoundItemsCount()
        );
        return ResponseEntity.ok(stats);
    }
}
