package com.subdivision.gateentry.ui;

import com.subdivision.gateentry.dao.UserDao;
import com.subdivision.gateentry.model.User;

import java.util.List;
import java.util.Scanner;

/** Screen shown after login. Logout returns to the main menu. */
public class DashboardMenu {

    private final Scanner scanner;
    private final UserDao userDao;
    private final User currentUser;

    public DashboardMenu(Scanner scanner, UserDao userDao, User currentUser) {
        this.scanner = scanner;
        this.userDao = userDao;
        this.currentUser = currentUser;
    }

    public void show() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n=== " + currentUser.role() + " DASHBOARD ===");
            boolean isAdmin = "ADMIN".equals(currentUser.role());
            if (isAdmin) {
                System.out.println("1. View pending accounts");
                System.out.println("2. Approve an account");
            } else {
                System.out.println("(Gate entry features coming in the next module.)");
            }
            System.out.println("0. Logout");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();
            if ("0".equals(choice)) {
                loggedIn = false;
            } else if (isAdmin && "1".equals(choice)) {
                listPending();
            } else if (isAdmin && "2".equals(choice)) {
                approve();
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private void listPending() {
        List<User> pending = userDao.findByStatus("PENDING");
        if (pending.isEmpty()) {
            System.out.println("No pending accounts.");
            return;
        }
        System.out.printf("%-5s %-15s %-25s %-10s%n", "ID", "Username", "Full Name", "Role");
        for (User u : pending) {
            System.out.printf("%-5d %-15s %-25s %-10s%n",
                    u.userId(), u.username(), u.fullName(), u.role());
        }
    }

    private void approve() {
        System.out.print("Enter user ID to approve: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.println(userDao.updateStatus(id, "ACTIVE")
                    ? "Account approved." : "Could not approve account.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
}
