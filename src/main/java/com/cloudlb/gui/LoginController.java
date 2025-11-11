package com.cloudlb.gui;

import com.cloudlb.database.DBConnect;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    public void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        try (Connection conn = DBConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT role FROM users WHERE username=? AND password=?");
            ps.setString(1, user);
            ps.setString(2, hashPassword(pass));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                showAlert("Login Successful", "Welcome " + user + " (" + role + ")");
                openDashboard(role);
            } else {
                showAlert("Login Failed", "Invalid username or password");
            }
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void openSignup() {
        try {
            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource("/com/cloudlb/gui/signup.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load(), 400, 300);
            javafx.stage.Stage stage = (javafx.stage.Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void openDashboard(String role) {
        try {
            String fxmlPath = role.equalsIgnoreCase("admin")
                    ? "/com/cloudlb/gui/dashboards/admin.fxml"
                    : "/com/cloudlb/gui/dashboards/user.fxml";

            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource(fxmlPath));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load(), 400, 400);
            javafx.stage.Stage stage = (javafx.stage.Stage) usernameField.getScene().getWindow();

            if (!role.equalsIgnoreCase("admin")) {
                com.cloudlb.gui.FileManagerController controller = loader.getController();
                controller.setUsername(usernameField.getText());
            }

            stage.setScene(scene);
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md =
                    java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

