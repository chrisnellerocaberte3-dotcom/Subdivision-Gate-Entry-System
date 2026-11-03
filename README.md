# Subdivision Gate Entry System

Layered Java console app (Maven + SQLite).

## Structure
- `dao`   - DatabaseConnection, UserDao (PreparedStatement SQL)
- `model` - User (Java record)
- `ui`    - MainMenu, DashboardMenu
- `Main.java` - entry point

## Run
Requires JDK 17+ and Maven.

    mvn compile exec:java

Default admin (seeded on first run): `admin` / `admin123`

## Features
- Register account (GUARD / ADMIN / RESIDENT, status defaults to PENDING)
- Login (only ACTIVE accounts)
- Logout / Exit
- Admin can view and approve pending accounts
