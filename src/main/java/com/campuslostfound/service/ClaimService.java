package com.campuslostfound.service;

import com.campuslostfound.entity.Claim;
import com.campuslostfound.entity.FoundItem;
import com.campuslostfound.entity.User;
import com.campuslostfound.exception.ResourceAlreadyExistsException;
import com.campuslostfound.exception.ResourceNotFoundException;
import com.campuslostfound.repository.ClaimRepository;
import com.campuslostfound.repository.FoundItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@SuppressWarnings("null")
public class ClaimService {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private FoundItemRepository foundItemRepository;

    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    public Claim getClaimById(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        return claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with id: " + id));
    }

    public List<Claim> getClaimsByUserId(Long userId) {
        return claimRepository.findByUserId(userId);
    }

    public List<Claim> getClaimsForFoundItem(Long foundItemId) {
        return claimRepository.findByFoundItemId(foundItemId);
    }

    public List<Claim> getClaimsForUserItems(Long userId) {
        return claimRepository.findClaimsForUserItems(userId);
    }

    public Claim createClaim(Long foundItemId, Claim claim, User user) {
        Objects.requireNonNull(foundItemId, "foundItemId must not be null");
        Objects.requireNonNull(claim, "claim must not be null");
        Objects.requireNonNull(user, "user must not be null");
        FoundItem foundItem = foundItemRepository.findById(foundItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Found item not found with id: " + foundItemId));

        // Prevent multiple pending claims by same user on same item
        List<Claim> existingClaims = claimRepository.findByFoundItemId(foundItemId);
        for (Claim existing : existingClaims) {
            if (existing.getUser() != null
                    && existing.getUser().getId() != null
                    && existing.getUser().getId().equals(user.getId())
                    && existing.getStatus() == Claim.ClaimStatus.PENDING) {
                throw new ResourceAlreadyExistsException("You already have a pending claim for this item");
            }
        }

        claim.setFoundItem(foundItem);
        claim.setUser(user);
        claim.setClaimDate(LocalDate.now());
        claim.setStatus(Claim.ClaimStatus.PENDING);

        return claimRepository.save(claim);
    }

    public Claim updateClaimStatus(Long id, Claim.ClaimStatus status, String adminNotes) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Claim claim = getClaimById(id);
        claim.setStatus(status);
        claim.setAdminNotes(adminNotes);

        if (status == Claim.ClaimStatus.APPROVED) {
            FoundItem foundItem = claim.getFoundItem();
            if (foundItem != null) {
                foundItem.setStatus(FoundItem.ItemStatus.CLAIMED);
                foundItemRepository.save(foundItem);
            }
        }

        return Objects.requireNonNull(claimRepository.save(claim), "updated claim must not be null");
    }

    public void deleteClaim(Long id) {
        Objects.requireNonNull(id, "id must not be null");
        Claim claim = getClaimById(id);
        claimRepository.delete(claim);
    }

    public long getPendingClaimsCount() {
        return claimRepository.countPendingClaims();
    }

    public long getApprovedClaimsCount() {
        return claimRepository.countApprovedClaims();
    }

    public List<Claim> getRecentClaims() {
        return claimRepository.findRecentClaims();
    }
}
