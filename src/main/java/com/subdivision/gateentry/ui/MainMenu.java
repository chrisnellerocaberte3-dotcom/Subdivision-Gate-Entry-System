package com.subdivision.gateentry.ui;

import com.subdivision.gateentry.dao.UserDao;
import com.subdivision.gateentry.model.User;

import java.util.Optional;
import java.util.Scanner;

/** Main menu: Register, Login, Exit. */
public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== SUBDIVISION GATE ENTRY SYSTEM ===");
            System.out.println("1. Register Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void register() {
        System.out.println("\n--- Register Account ---");
        System.out.print("Full name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }
        if (userDao.usernameExists(username)) {
            System.out.println("Username already taken.");
            return;
        }

        String role = chooseRole();
        if (role == null) {
            System.out.println("Invalid role. Registration cancelled.");
            return;
        }

        User user = new User(0, username, UserDao.hash(password), fullName, role, "PENDING");
        if (userDao.register(user)) {
            System.out.println("Registered! Your account is PENDING until an administrator approves it.");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private String chooseRole() {
        System.out.println("Select role:");
        System.out.println("1. GUARD (Security Guard / Gate Personnel)");
        System.out.println("2. ADMIN (Subdivision Administrator / HOA Staff)");
        System.out.println("3. RESIDENT (Resident / Homeowner)");
        System.out.print("Choose: ");
        return switch (scanner.nextLine().trim()) {
            case "1" -> "GUARD";
            case "2" -> "ADMIN";
            case "3" -> "RESIDENT";
            default -> null;
        };
    }

    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Optional<User> result = userDao.login(username, password);
        if (result.isEmpty()) {
            System.out.println("Invalid username or password.");
            return;
        }

        User user = result.get();
        if (!"ACTIVE".equals(user.status())) {
            System.out.println("Your account is " + user.status() + ". Please wait for admin approval.");
            return;
        }

        System.out.println("Welcome, " + user.fullName() + " (" + user.role() + ")!");
        new DashboardMenu(scanner, userDao, user).show();
        System.out.println("You have been logged out.");
    }
}
