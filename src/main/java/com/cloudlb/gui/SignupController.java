package com.cloudlb.gui;

import com.cloudlb.database.DBConnect;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignupController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    public void handleSignup() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill all fields");
            return;
        }

        String hashedPassword = hashPassword(password);

        try (Connection conn = DBConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, 'standard')");
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.executeUpdate();

            showAlert("Success", "Account created! You can now log in.");
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }
    @FXML
public void goToLogin() {
    try {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/cloudlb/gui/login.fxml"));
        javafx.scene.Scene scene = new javafx.scene.Scene(loader.load(), 400, 300);
        javafx.stage.Stage stage = (javafx.stage.Stage) usernameField.getScene().getWindow();
        stage.setScene(scene);
    } catch (Exception e) {
        System.out.println("❌ Navigation error: " + e.getMessage());
    }
}


    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password; // fallback (should never happen)
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

