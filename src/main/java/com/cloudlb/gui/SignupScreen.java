package com.cloudlb.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SignupScreen {

    public void start(Stage stage) {
        stage.setTitle("📝 CloudLB - Sign Up");

        Label title = new Label("Create Your Account");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Label msg = new Label();
        msg.setStyle("-fx-text-fill: #ff7575;");

        Button signup = new Button("Sign Up");
        signup.setStyle("-fx-background-color: #06d6a0; -fx-text-fill: white;");
        Button back = new Button("← Back to Login");
        back.setStyle("-fx-background-color: transparent; -fx-text-fill: #00b4d8;");

        signup.setOnAction(e -> {
            msg.setText("✅ Account created (demo) — use admin/admin123 to sign in");
        });

        back.setOnAction(e -> {
            new LoginScreen().start(new Stage());
            stage.close();
        });

        VBox root = new VBox(12, title, username, password, signup, back, msg);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:#1e1e1e; -fx-padding:40;");
        stage.setScene(new Scene(root, 420, 340));
        stage.show();
    }
}

