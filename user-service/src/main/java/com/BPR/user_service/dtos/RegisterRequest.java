package com.BPR.user_service.dtos;

public record RegisterRequest(String username, String password, String genrePreferences, String role) {}

