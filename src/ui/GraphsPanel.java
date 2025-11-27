package ui;

import db.CalculationLogDAO;
import db.DBHelper;
import model.CalculationLog;
import model.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphsPanel extends JPanel {
    private JComboBox<User> userComboBox;
    private JComboBox<String> calculationTypeComboBox;
    private JComboBox<String> parameterXComboBox;
    private JComboBox<String> parameterYComboBox;
    private JPanel chartPanel;
    private JTextArea dataTextArea;
    private List<CalculationLog> currentLogs;
    private ChartPanel currentChartPanel;

    public GraphsPanel() {
        setLayout(new BorderLayout(10, 10));
        currentLogs = new ArrayList<>();

        // --- Верхняя панель управления ---
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        // --- Центральная панель с графиком и данными ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Левая часть - график
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("График"));
        centerPanel.add(chartPanel);

        // Правая часть - таблица данных
        dataTextArea = new JTextArea();
        dataTextArea.setEditable(false);
        dataTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane dataScrollPane = new JScrollPane(dataTextArea);
        dataScrollPane.setBorder(BorderFactory.createTitledBorder("Данные для графика"));
        centerPanel.add(dataScrollPane);

        add(centerPanel, BorderLayout.CENTER);

        // --- Нижняя панель с кнопками ---
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Загружаем пользователей при инициализации
        loadUsers();

        // Показываем пустой график при старте
        showEmptyChart();
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Параметры графика"));

        // Выбор пользователя
        panel.add(new JLabel("Пользователь:"));
        userComboBox = new JComboBox<>();
        userComboBox.addActionListener(e -> onUserSelected());
        panel.add(userComboBox);

        // Выбор типа расчета
        panel.add(new JLabel("Тип расчета:"));
        calculationTypeComboBox = new JComboBox<>(new String[]{
                "Все расчеты", "Расчёт кислородной фурмы", "Расчёт сопла Лаваля"
        });
        calculationTypeComboBox.addActionListener(e -> onCalculationTypeSelected());
        panel.add(calculationTypeComboBox);

        // Выбор параметра X
        panel.add(new JLabel("Параметр X:"));
        parameterXComboBox = new JComboBox<>();
        panel.add(parameterXComboBox);

        // Выбор параметра Y
        panel.add(new JLabel("Параметр Y:"));
        parameterYComboBox = new JComboBox<>();
        panel.add(parameterYComboBox);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton buildGraphButton = new JButton("Построить график");
        JButton exportDataButton = new JButton("Экспорт данных");
        JButton exportImageButton = new JButton("Экспорт изображения");
        JButton clearButton = new JButton("Очистить");

        buildGraphButton.addActionListener(e -> buildGraph());
        exportDataButton.addActionListener(e -> exportData());
        exportImageButton.addActionListener(e -> exportImage());
        clearButton.addActionListener(e -> clearGraph());

        panel.add(clearButton);
        panel.add(exportDataButton);
        panel.add(exportImageButton);
        panel.add(buildGraphButton);

        return panel;
    }

    private void loadUsers() {
        try {
            List<User> users = DBHelper.listUsers();
            userComboBox.removeAllItems();
            userComboBox.addItem(new User(-1, "Все пользователи", "", ""));

            for (User user : users) {
                userComboBox.addItem(user);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки пользователей: " + e.getMessage());
        }
    }

    private void onUserSelected() {
        loadCalculationData();
        updateParameterComboBoxes();
    }

    private void onCalculationTypeSelected() {
        loadCalculationData();
        updateParameterComboBoxes();
    }

    private void loadCalculationData() {
        try {
            User selectedUser = (User) userComboBox.getSelectedItem();
            String calcType = (String) calculationTypeComboBox.getSelectedItem();

            List<CalculationLog> allLogs = CalculationLogDAO.getAll();
            currentLogs.clear();

            for (CalculationLog log : allLogs) {
                boolean userMatch = (selectedUser == null || selectedUser.getId() == -1 || log.getUserId() == selectedUser.getId());
                boolean typeMatch = "Все расчеты".equals(calcType) || calcType.equals(log.getCalcType());

                if (userMatch && typeMatch) {
                    currentLogs.add(log);
                }
            }

            updateDataTextArea();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка загрузки данных: " + e.getMessage());
        }
    }

    private void updateParameterComboBoxes() {
        parameterXComboBox.removeAllItems();
        parameterYComboBox.removeAllItems();

        // Базовые параметры для графиков
        String[] commonParams = {"ID расчета", "Время расчета"};
        String[] lavalParams = {"Давление p₀", "Температура T₀", "Показатель адиабаты γ", "Массовый расход", "Скорость звука", "Критическое сечение"};
        String[] furnaceParams = {"Масса металла", "Расход O₂", "Начальная температура", "Конечная температура", "Время продувки", "Расход кислорода"};

        for (String param : commonParams) {
            parameterXComboBox.addItem(param);
            parameterYComboBox.addItem(param);
        }

        String selectedType = (String) calculationTypeComboBox.getSelectedItem();
        if ("Расчёт сопла Лаваля".equals(selectedType) || "Все расчеты".equals(selectedType)) {
            for (String param : lavalParams) {
                parameterXComboBox.addItem(param);
                parameterYComboBox.addItem(param);
            }
        }
        if ("Расчёт кислородной фурмы".equals(selectedType) || "Все расчеты".equals(selectedType)) {
            for (String param : furnaceParams) {
                parameterXComboBox.addItem(param);
                parameterYComboBox.addItem(param);
            }
        }

        // Установка значений по умолчанию
        if (parameterXComboBox.getItemCount() > 0) parameterXComboBox.setSelectedIndex(0);
        if (parameterYComboBox.getItemCount() > 1) parameterYComboBox.setSelectedIndex(1);
    }

    private void updateDataTextArea() {
        StringBuilder sb = new StringBuilder();
        sb.append("Найдено расчетов: ").append(currentLogs.size()).append("\n\n");

        for (int i = 0; i < currentLogs.size(); i++) {
            CalculationLog log = currentLogs.get(i);
            sb.append(i + 1).append(". ").append(log.getCalcType()).append("\n");
            sb.append("   Вход: ").append(log.getInputData()).append("\n");
            sb.append("   Результат: ").append(log.getResultData()).append("\n");
            sb.append("   Дата: ").append(log.getCreatedAt()).append("\n\n");
        }

        dataTextArea.setText(sb.toString());
    }

    private void buildGraph() {
        if (currentLogs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Нет данных для построения графика");
            return;
        }

        String paramX = (String) parameterXComboBox.getSelectedItem();
        String paramY = (String) parameterYComboBox.getSelectedItem();

        System.out.println("=== ПОСТРОЕНИЕ ГРАФИКА ===");
        System.out.println("Параметр X: " + paramX);
        System.out.println("Параметр Y: " + paramY);
        System.out.println("Количество записей: " + currentLogs.size());

        try {
            // Создаем dataset для точечного графика
            XYSeries series = new XYSeries(paramY + " vs " + paramX);
            int validPoints = 0;

            for (int i = 0; i < currentLogs.size(); i++) {
                CalculationLog log = currentLogs.get(i);
                double xValue = getParameterValue(log, paramX, i);
                double yValue = getParameterValue(log, paramY, i);

                System.out.println("Запись " + i + ": X=" + xValue + ", Y=" + yValue);

                if (!Double.isNaN(xValue) && !Double.isNaN(yValue)) {
                    series.add(xValue, yValue);
                    validPoints++;
                }
            }

            System.out.println("Успешно добавлено точек: " + validPoints);

            if (validPoints == 0) {
                JOptionPane.showMessageDialog(this,
                        "Не удалось извлечь данные для построения графика.\n" +
                                "Проверьте формат данных в базе.",
                        "Нет данных",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(series);

            // Создаем график
            JFreeChart chart = ChartFactory.createScatterPlot(
                    paramY + " в зависимости от " + paramX,
                    paramX,
                    paramY,
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            displayChart(chart);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка построения графика: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private double getParameterValue(CalculationLog log, String parameter, int index) {
        try {
            switch (parameter) {
                case "ID расчета":
                    return log.getId();
                case "Время расчета":
                    return index;

                // Параметры сопла Лаваля
                case "Давление p₀":
                    return extractValueFromInput(log, "p0", 101325.0);
                case "Температура T₀":
                    return extractValueFromInput(log, "T0", 300.0);
                case "Показатель адиабаты γ":
                    return extractValueFromInput(log, "gamma", 1.4);
                case "Массовый расход":
                    return extractValueFromInput(log, "massFlow", 1.0);
                case "Скорость звука":
                    return extractValueFromResult(log, "Speed of Sound", 300.0);
                case "Критическое сечение":
                    return extractValueFromResult(log, "A\\*", 0.01);

                // Параметры кислородной фурмы
                case "Масса металла":
                    return extractValueFromInput(log, "mass", 1000.0);
                case "Расход O₂":
                    return extractValueFromInput(log, "oxygenFlow", 300.0);
                case "Начальная температура":
                    return extractValueFromInput(log, "tInitial", 25.0);
                case "Конечная температура":
                    return extractValueFromResult(log, "Final Temperature", 100.0);
                case "Время продувки":
                    return extractValueFromResult(log, "Process Time", 1.0);
                case "Расход кислорода":
                    return extractValueFromResult(log, "Oxygen Used", 300.0);

                default:
                    return index;
            }
        } catch (Exception e) {
            System.out.println("Ошибка парсинга параметра " + parameter + ": " + e.getMessage());
            return Double.NaN;
        }
    }

    private double extractValueFromInput(CalculationLog log, String key, double defaultValue) {
        String inputData = log.getInputData();
        if (inputData == null || inputData.isEmpty()) {
            return defaultValue;
        }

        System.out.println("Парсим входные данные: " + inputData + " для ключа: " + key);

        // Пробуем разные форматы
        String[] patterns = {
                key + "=",                    // {mass=1000.0, oxygenFlow=300.0}
                key + ": ",                   // {mass: 1000.0, oxygenFlow: 300.0}
                "\"" + key + "\":",           // JSON-like
                key + " "                     // С пробелом
        };

        for (String pattern : patterns) {
            int startIndex = inputData.indexOf(pattern);
            if (startIndex != -1) {
                startIndex += pattern.length();
                int endIndex = findNumberEnd(inputData, startIndex);

                if (endIndex > startIndex) {
                    String valueStr = inputData.substring(startIndex, endIndex).trim();
                    valueStr = cleanValueString(valueStr);

                    try {
                        double value = Double.parseDouble(valueStr);
                        System.out.println("Найдено значение для " + key + ": " + value);
                        return value;
                    } catch (NumberFormatException e) {
                        System.out.println("Не удалось распарсить: '" + valueStr + "'");
                    }
                }
            }
        }

        // Если не нашли по ключу, ищем любое число
        return extractFirstNumber(inputData, defaultValue);
    }

    private double extractValueFromResult(CalculationLog log, String key, double defaultValue) {
        String resultData = log.getResultData();
        if (resultData == null || resultData.isEmpty()) {
            return defaultValue;
        }

        System.out.println("Парсим результаты: " + resultData + " для ключа: " + key);

        // Пробуем разные форматы для результатов
        String[] patterns = {
                key + "=",                    // {Final Temperature=125.0}
                key + ": ",                   // {Final Temperature: 125.0}
                key + " ",                    // С пробелом
                key.replace(" ", "") + "="    // Без пробелов
        };

        for (String pattern : patterns) {
            int startIndex = resultData.indexOf(pattern);
            if (startIndex != -1) {
                startIndex += pattern.length();
                int endIndex = findNumberEnd(resultData, startIndex);

                if (endIndex > startIndex) {
                    String valueStr = resultData.substring(startIndex, endIndex).trim();
                    valueStr = cleanValueString(valueStr);

                    try {
                        double value = Double.parseDouble(valueStr);
                        System.out.println("Найдено значение для " + key + ": " + value);
                        return value;
                    } catch (NumberFormatException e) {
                        System.out.println("Не удалось распарсить: '" + valueStr + "'");
                    }
                }
            }
        }

        // Если не нашли по ключу, ищем любое число
        return extractFirstNumber(resultData, defaultValue);
    }

    private int findNumberEnd(String data, int startIndex) {
        for (int i = startIndex; i < data.length(); i++) {
            char c = data.charAt(i);
            if (c == ',' || c == '}' || c == ']' || c == ' ' || c == '°' || c == '(' || c == ')' || c == '[') {
                return i;
            }
        }
        return data.length();
    }

    private String cleanValueString(String valueStr) {
        // Убираем единицы измерения и лишние символы
        return valueStr.replace("°C", "")
                .replace("m³", "")
                .replace("h", "")
                .replace("Pa", "")
                .replace("K", "")
                .replace("(", "")
                .replace(")", "")
                .replace("[", "")
                .replace("]", "")
                .trim();
    }

    private double extractFirstNumber(String data, double defaultValue) {
        try {
            Pattern pattern = Pattern.compile("-?\\d+\\.?\\d*");
            Matcher matcher = pattern.matcher(data);
            if (matcher.find()) {
                return Double.parseDouble(matcher.group());
            }
        } catch (Exception e) {
            System.out.println("Не удалось извлечь число из: " + data);
        }
        return defaultValue;
    }

    private void displayChart(JFreeChart chart) {
        // Удаляем старый график
        if (currentChartPanel != null) {
            chartPanel.remove(currentChartPanel);
        }

        // Создаем новый ChartPanel
        currentChartPanel = new ChartPanel(chart);
        currentChartPanel.setPreferredSize(new Dimension(600, 400));

        chartPanel.removeAll();
        chartPanel.add(currentChartPanel, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private void showEmptyChart() {
        XYSeries series = new XYSeries("Нет данных");
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Выберите данные для построения графика",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        displayChart(chart);
    }

    private void exportData() {
        if (currentLogs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Нет данных для экспорта");
            return;
        }

        try (FileWriter fw = new FileWriter("graph_data.csv")) {
            // Заголовок CSV
            fw.write("ID;Пользователь;Тип расчета;Входные данные;Результаты;Дата\n");

            for (CalculationLog log : currentLogs) {
                fw.write(log.getId() + ";");
                fw.write(getUsername(log.getUserId()) + ";");
                fw.write(log.getCalcType() + ";");
                fw.write("\"" + log.getInputData() + "\";");
                fw.write("\"" + log.getResultData() + "\";");
                fw.write(log.getCreatedAt() + "\n");
            }

            JOptionPane.showMessageDialog(this, "Данные экспортированы в graph_data.csv");
        } catch (IOException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка экспорта: " + e.getMessage());
        }
    }

    private void exportImage() {
        if (currentChartPanel == null) {
            JOptionPane.showMessageDialog(this, "Нет графика для экспорта");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить график как изображение");
        fileChooser.setSelectedFile(new File("график.png"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                org.jfree.chart.ChartUtils.saveChartAsPNG(
                        file,
                        currentChartPanel.getChart(),
                        currentChartPanel.getWidth(),
                        currentChartPanel.getHeight()
                );
                JOptionPane.showMessageDialog(this, "График сохранен как: " + file.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Ошибка сохранения: " + e.getMessage());
            }
        }
    }

    private void clearGraph() {
        currentLogs.clear();
        dataTextArea.setText("");
        parameterXComboBox.removeAllItems();
        parameterYComboBox.removeAllItems();
        showEmptyChart();
    }

    private String getUsername(int userId) throws SQLException {
        for (User u : DBHelper.listUsers()) {
            if (u.getId() == userId) return u.getUsername();
        }
        return "unknown";
    }
}