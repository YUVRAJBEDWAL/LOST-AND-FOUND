package com.campuslostfound.controller;

import com.campuslostfound.entity.Claim;
import com.campuslostfound.entity.User;
import com.campuslostfound.service.AuthService;
import com.campuslostfound.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*", maxAge = 3600)
@SuppressWarnings("null")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @Autowired
    private AuthService authService;

    // Matches API.admin.getAll() (admin-only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Claim>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAllClaims());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Claim> getClaimById(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaimById(Objects.requireNonNull(id, "id must not be null")));
    }

    // Matches API.claims.getMyClaims()
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Claim>> getMyClaims() {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(claimService.getClaimsByUserId(currentUser.getId()));
    }

    // Matches API.claims.getForFoundItem(foundItemId)
    @GetMapping("/found-item/{foundItemId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Claim>> getClaimsForFoundItem(@PathVariable Long foundItemId) {
        return ResponseEntity.ok(claimService.getClaimsForFoundItem(foundItemId));
    }

    // Matches API.claims.getForMyItems()
    @GetMapping("/my-items")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Claim>> getClaimsForMyItems() {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(claimService.getClaimsForUserItems(currentUser.getId()));
    }

    // Matches API.claims.create(foundItemId, claimData)
    @PostMapping("/found-item/{foundItemId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Claim> createClaim(
            @PathVariable Long foundItemId,
            @Valid @RequestBody Claim claim) {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(claimService.createClaim(
                Objects.requireNonNull(foundItemId, "foundItemId must not be null"),
                Objects.requireNonNull(claim, "claim must not be null"),
                Objects.requireNonNull(currentUser, "currentUser must not be null")
        ));
    }

    // Matches API.claims.updateStatus(id, status, adminNotes)
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Claim> updateClaimStatus(
            @PathVariable Long id,
            @RequestParam Claim.ClaimStatus status,
            @RequestParam(required = false) String adminNotes) {

        return ResponseEntity.ok(claimService.updateClaimStatus(
                Objects.requireNonNull(id, "id must not be null"),
                Objects.requireNonNull(status, "status must not be null"),
                adminNotes
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getClaimStats() {
        Map<String, Long> stats = Map.of(
                "pendingClaims", claimService.getPendingClaimsCount(),
                "approvedClaims", claimService.getApprovedClaimsCount()
        );
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Claim>> getRecentClaims() {
        return ResponseEntity.ok(claimService.getRecentClaims());
    }
}
