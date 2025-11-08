package com.fitness.userservice.controller;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UpdateGoalsRequest;
import com.fitness.userservice.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fitness.userservice.service.UserService;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.existByUserId(userId));
    }

    // -----------------------------
    // GOALS
    // -----------------------------
    @PatchMapping("/{userId}/goals")
    public ResponseEntity<UserResponse> updateGoals(
            @PathVariable String userId,
            @Valid @RequestBody UpdateGoalsRequest request) {
        return ResponseEntity.ok(userService.updateGoals(userId, request));
    }

    // -----------------------------
    // ACTIVE WORKOUT PLAN
    // -----------------------------
    @PatchMapping("/{userId}/active-plan/{planId}")
    public ResponseEntity<UserResponse> updateActivePlan(
            @PathVariable String userId,
            @PathVariable String planId) {
        return ResponseEntity.ok(userService.updateActivePlan(userId, planId));
    }

    @DeleteMapping("/{userId}/active-plan")
    public ResponseEntity<UserResponse> clearActivePlan(
            @PathVariable String userId) {
        return ResponseEntity.ok(userService.clearActivePlan(userId));
    }
}

