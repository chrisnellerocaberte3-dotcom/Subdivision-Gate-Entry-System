package com.subdivision.gateentry.model;

/**
 * Immutable data model for the Users table.
 * role   : GUARD, ADMIN, or RESIDENT
 * status : PENDING (default), ACTIVE, or INACTIVE
 */
public record User(
        int userId,
        String username,
        String passwordHash,
        String fullName,
        String role,
        String status) {
}
