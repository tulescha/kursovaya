package ui;

import db.DBHelper;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AuthPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private MainFrame mainFrame;

    public AuthPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridLayout(4, 2, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Вход в систему"));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Войти");
        JButton registerButton = new JButton("Регистрация");

        add(new JLabel("Логин:"));
        add(usernameField);
        add(new JLabel("Пароль:"));
        add(passwordField);
        add(loginButton);
        add(registerButton);

        // Обработка входа
        loginButton.addActionListener(this::onLogin);
        registerButton.addActionListener(this::onRegister);
    }

    private void onLogin(ActionEvent e) {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Заполните все поля!");
            return;
        }

        try {
            User authenticatedUser = DBHelper.authenticateUser(user, pass);
            if (authenticatedUser != null) {
                JOptionPane.showMessageDialog(this, "Вход выполнен успешно!");
                mainFrame.onLoginSuccess(authenticatedUser);
            } else {
                JOptionPane.showMessageDialog(this, "Неверный логин или пароль.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка базы данных: " + ex.getMessage());
        }
    }

    private void onRegister(ActionEvent e) {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Заполните все поля!");
            return;
        }

        try {
            if (DBHelper.addUser(user, pass, "user")) {
                JOptionPane.showMessageDialog(this, "Регистрация прошла успешно!");
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка регистрации!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Пользователь уже существует!");
        }
    }
}