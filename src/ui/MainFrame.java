package ui;

import db.DBHelper;
import model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public static User currentUser;
    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);
//    private User currentUser;

    // Панель, содержащая все вкладки после входа
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
        cards.add(mainContentPanel, "main");

        add(cards, BorderLayout.CENTER);

        // 4. Устанавливаем меню (будет видно постоянно)
        setJMenuBar(createMenuBar());

        // 5. Показываем панель авторизации при старте
        showAuth();
    }

    // Инициализация JTabbedPane со всеми вкладками из скриншотов
    private void initializeMainContentPanel() {
        mainContentPanel = new JTabbedPane(JTabbedPane.TOP);

        // 1. Калькулятор (Ваш оригинальный CalculationPanel)
        mainContentPanel.addTab("Калькулятор", new CalculationPanel(this));


        // 2. Визуализация Алгоритма
        mainContentPanel.addTab("Визуализация Алгоритма", new AlgorithmPanel());

        // 3. Управление Данными (Ваш DataPanel, переименованный)
        mainContentPanel.addTab("Управление Данными", new DataManagementPanel());


        // 4. Графики
        mainContentPanel.addTab("Графики", new GraphsPanel());

        // 5. Оборудование
        mainContentPanel.addTab("Оборудование", new EquipmentPanel());

        // 6. Формулы
        mainContentPanel.addTab("Формулы", new FormulasPanel());
    }

    // Создание строки меню: "Файл" и "Справка"
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        fileMenu.add(new JMenuItem("Сохранить расчет"));
        fileMenu.add(new JMenuItem("Импорт данных"));
        fileMenu.add(new JMenuItem("Экспорт данных"));
        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Справка");

        // Добавляем пункты справки
        JMenuItem instructionItem = new JMenuItem("Инструкция пользователя");
        JMenuItem errorsItem = new JMenuItem("Решение ошибок");
        JMenuItem aboutItem = new JMenuItem("О программе");

        instructionItem.addActionListener(e -> showHelpDialog("instruction"));
        errorsItem.addActionListener(e -> showHelpDialog("errors"));
        aboutItem.addActionListener(e -> showHelpDialog("about"));

        helpMenu.add(instructionItem);
        helpMenu.add(errorsItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(helpMenu);

        return menuBar;
    }

    // Новый метод для показа диалога справки
    private void showHelpDialog(String tab) {
        HelpDialog helpDialog = new HelpDialog(this);
        helpDialog.setVisible(true);
    }

    public void onLoginSuccess(User user) {
        this.currentUser = user;

        showMainContent();
        setTitle("Калькулятор металлурга — Пользователь: " + user.getUsername());
    }

    public void showAuth() { cardLayout.show(cards, "auth"); }
    public void showMainContent() { cardLayout.show(cards, "main"); }
    public User getCurrentUser() {
        return currentUser;
    }

}