package ui;

import db.DBHelper;
import model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);
    private User currentUser;

    // Новая панель, содержащая все вкладки после входа
    private JTabbedPane mainContentPanel;

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

        // 1. Инициализация панели основного содержимого с вкладками (JTabbedPane)
        initializeMainContentPanel();

        // 2. Создаем панели
        AuthPanel authPanel = new AuthPanel(this);

        // 3. Добавляем панели в карты
        cards.add(authPanel, "auth");
        cards.add(mainContentPanel, "main"); // Основное окно после входа

        add(cards, BorderLayout.CENTER);

        // 4. Устанавливаем меню (будет видно постоянно, но активно после входа)
        setJMenuBar(createMenuBar());

        // 5. Показываем панель авторизации при старте
        showAuth();
    }

    // Инициализация JTabbedPane со всеми вкладками из скриншотов
    private void initializeMainContentPanel() {
        mainContentPanel = new JTabbedPane(JTabbedPane.TOP); // Вкладки сверху

        // 1. Калькулятор
        mainContentPanel.addTab("Калькулятор", new CalculationPanel());

        // 2. Визуализация Алгоритма
        mainContentPanel.addTab("Визуализация Алгоритма", new AlgorithmPanel());

        // 3. Управление Данными
        mainContentPanel.addTab("Управление Данными", new DataManagementPanel());

        // 4. Графики
        mainContentPanel.addTab("Графики", new GraphsPanel());

        // 5. Оборудование
        mainContentPanel.addTab("Оборудование", new EquipmentPanel());

        // 6. Формулы
        mainContentPanel.addTab("Формулы", new FormulasPanel());
    }

    // Создание строки меню, как на скриншотах
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Справка");
        JMenuItem aboutItem = new JMenuItem("О программе");
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        // Добавление заголовков основных разделов в меню, как на скриншоте
        menuBar.add(new JMenu("Калькулятор"));
        menuBar.add(new JMenu("Визуализация Алгоритма"));
        menuBar.add(new JMenu("Управление Данными"));
        menuBar.add(new JMenu("Графики"));
        menuBar.add(new JMenu("Оборудование"));
        menuBar.add(new JMenu("Формулы"));
        menuBar.add(Box.createHorizontalGlue()); // Выравнивание
        menuBar.add(helpMenu);

        return menuBar;
    }

    public void onLoginSuccess(User user) {
        this.currentUser = user;
        showMainContent(); // Переходим к основному содержимому после успешного входа
        setTitle("Калькулятор металлурга — Пользователь: " + user.getUsername());
    }

    public void showAuth() { cardLayout.show(cards, "auth"); }
    public void showMainContent() { cardLayout.show(cards, "main"); }
}