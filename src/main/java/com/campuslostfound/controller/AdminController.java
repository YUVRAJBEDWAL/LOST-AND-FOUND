package com.campuslostfound.controller;

import com.campuslostfound.repository.ClaimRepository;
import com.campuslostfound.repository.FoundItemRepository;
import com.campuslostfound.repository.LostItemRepository;
import com.campuslostfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LostItemRepository lostItemRepository;

    @Autowired
    private FoundItemRepository foundItemRepository;

    @Autowired
    private ClaimRepository claimRepository;

    // Matches API.admin.getDashboard()
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("totalAdmins", userRepository.countAdmins());

        stats.put("totalLostItems", lostItemRepository.count());
        stats.put("openLostItems", lostItemRepository.countOpenItems());
        stats.put("claimedLostItems", lostItemRepository.countClaimedItems());

        stats.put("totalFoundItems", foundItemRepository.count());
        stats.put("openFoundItems", foundItemRepository.countOpenItems());
        stats.put("claimedFoundItems", foundItemRepository.countClaimedItems());

        stats.put("pendingClaims", claimRepository.countPendingClaims());
        stats.put("approvedClaims", claimRepository.countApprovedClaims());

        return ResponseEntity.ok(stats);
    }

    // Matches API.admin.getUsers()
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // Matches API.admin.getActivity()
    @GetMapping("/activity")
    public ResponseEntity<Map<String, Object>> getRecentActivity() {
        Map<String, Object> activity = new HashMap<>();
        activity.put("recentLostItems", lostItemRepository.findRecentItems());
        activity.put("recentFoundItems", foundItemRepository.findRecentItems());
        activity.put("recentClaims", claimRepository.findRecentClaims());
        return ResponseEntity.ok(activity);
    }
}
