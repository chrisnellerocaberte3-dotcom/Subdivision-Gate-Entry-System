package com.subdivision.gateentry;

import com.subdivision.gateentry.dao.DatabaseConnection;
import com.subdivision.gateentry.ui.MainMenu;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initialize();
        new MainMenu().start();
    }
}
