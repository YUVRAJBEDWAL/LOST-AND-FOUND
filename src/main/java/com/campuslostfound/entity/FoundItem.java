package com.campuslostfound.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "found_items")
public class FoundItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Item name is required")
    @Size(max = 100, message = "Item name must not exceed 100 characters")
    @Column(name = "item_name", nullable = false)
    private String itemName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @NotBlank(message = "Location is required")
    @Size(max = 150, message = "Location must not exceed 150 characters")
    @Column(name = "location", nullable = false)
    private String location;
    
    @NotNull(message = "Found date is required")
    @Column(name = "found_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate foundDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ItemStatus status = ItemStatus.OPEN;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;
    
    @Column(name = "photo_url")
    private String photoUrl;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "foundItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private java.util.List<Claim> claims;
    
    public enum ItemStatus {
        OPEN, CLAIMED, RESOLVED
    }
    
    public enum Category {
        ELECTRONICS, ACCESSORIES, BOOKS, IDS, CLOTHING, OTHERS
    }
    
    public FoundItem() {}
    
    public FoundItem(String itemName, String description, String location, LocalDate foundDate, User user) {
        this.itemName = itemName;
        this.description = description;
        this.location = location;
        this.foundDate = foundDate;
        this.user = user;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public LocalDate getFoundDate() { return foundDate; }
    public void setFoundDate(LocalDate foundDate) { this.foundDate = foundDate; }
    
    public ItemStatus getStatus() { return status; }
    public void setStatus(ItemStatus status) { this.status = status; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public java.util.List<Claim> getClaims() { return claims; }
    public void setClaims(java.util.List<Claim> claims) { this.claims = claims; }
}
