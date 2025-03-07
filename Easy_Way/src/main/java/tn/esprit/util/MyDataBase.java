package tn.esprit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    private static MyDataBase instance;
    private Connection cnx;
    final String URL = "jdbc:mysql://127.0.0.1:3306/easy_way";
    final String USERNAME = "root";
    final String PASSWORD = "";

    public MyDataBase() {
        try {
            cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("⚠️ Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    public Connection getCnx() {
        try {
            // Vérifie si la connexion est fermée et la rouvre si nécessaire
            if (cnx == null || cnx.isClosed()) {
                System.out.println("🔄 Réouverture de la connexion à la base de données...");
                cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Impossible de rétablir la connexion : " + e.getMessage());
        }
        return cnx;
    }
}
