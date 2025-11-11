package com.cloudlb.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginScreen extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("🔐 CloudLB - Login");

        Label title = new Label("Cloud Load Balancer");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Label msg = new Label();
        msg.setStyle("-fx-text-fill: #ff7575;");

        Button login = new Button("Sign In");
        Button signup = new Button("Create Account");
        login.setStyle("-fx-background-color: #3a86ff; -fx-text-fill: white;");
        signup.setStyle("-fx-background-color: transparent; -fx-text-fill: #00b4d8;");

        login.setOnAction(e -> {
            if (username.getText().equals("admin") && password.getText().equals("admin123")) {
                msg.setText("✅ Login successful!");
                new Dashboard().start(new Stage());
                stage.close();
            } else {
                msg.setText("❌ Invalid credentials!");
            }
        });

        signup.setOnAction(e -> {
            new SignupScreen().start(new Stage());
            stage.close();
        });

        VBox root = new VBox(12, title, username, password, login, signup, msg);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:#1e1e1e; -fx-padding:40;");
        stage.setScene(new Scene(root, 420, 340));
        stage.show();
    }
}

