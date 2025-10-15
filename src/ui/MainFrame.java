package ui;

import db.DBHelper;
import model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);
    private User currentUser;

    public MainFrame() {
        setTitle("Калькулятор металлурга");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        try {
            DBHelper.init();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка инициализации БД: " + ex.getMessage());
        }

        // Создаем панели
        AuthPanel authPanel = new AuthPanel(this);
        DataPanel dataPanel = new DataPanel();
        CalculationPanel calcPanel = new CalculationPanel();

        // Добавляем панели в карты
        cards.add(authPanel, "auth");
        cards.add(dataPanel, "data");
        cards.add(calcPanel, "calc");

        add(cards, BorderLayout.CENTER);

        // Показываем панель авторизации при старте
        showAuth();
    }

    public void onLoginSuccess(User user) {
        this.currentUser = user;
        showCalc(); // Переходим к расчетам после успешного входа
    }

    public void showAuth() { cardLayout.show(cards, "auth"); }
    public void showData() { cardLayout.show(cards, "data"); }
    public void showCalc() { cardLayout.show(cards, "calc"); }
}