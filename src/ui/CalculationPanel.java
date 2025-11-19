package ui;

import calc.Calculator;
import calc.LavalNozzleCalculator;
import calc.OxygenFurnaceCalculator;
import calc.StepResult;
import db.CalculationLogDAO;
import model.CalculationLog;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class CalculationPanel extends JPanel {
    private JComboBox<String> calcTypeBox;
    private JTextField inputField1, inputField2, inputField3, inputField4, inputField5;
    private JTextArea resultArea;
    private JButton btnCalculate;
    private JButton btnRandom;
    private JButton btnExport;
    private JPanel inputPanel; // Панель для полей ввода
    private MainFrame mainFrame;


    public CalculationPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.white);
// ===== Верхняя панель с выбором типа расчёта =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Параметры расчёта"));
// Панель для выбора типа расчета
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("Тип расчёта:"));
        calcTypeBox = new JComboBox<>(new String[]{
                "Расчёт кислородной фурмы",
                "Расчёт сопла Лаваля"
        });
        typePanel.add(calcTypeBox);
        topPanel.add(typePanel, BorderLayout.NORTH);
// Панель для полей ввода (будет меняться динамически)
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 10, 10));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
// Инициализируем поля ввода
        inputField1 = new JTextField();
        inputField2 = new JTextField();
        inputField3 = new JTextField();
        inputField4 = new JTextField();
        inputField5 = new JTextField();
// Добавляем слушатель для смены интерфейса при выборе типа расчета
        calcTypeBox.addActionListener(e -> updateInputFields());
// Инициализируем поля для первого выбранного типа
        updateInputFields();
// ===== Центральная панель для вывода =====
        resultArea = new JTextArea(12, 40);
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createTitledBorder("Результаты расчёта"));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
// ===== Нижняя панель с кнопками =====
        JPanel btnPanel = new JPanel();
        btnCalculate = new JButton("Выполнить расчёт");
        btnRandom = new JButton("Случайные данные");
        btnExport = new JButton("Экспорт в TXT");
        btnPanel.add(btnCalculate);
        btnPanel.add(btnRandom);
        btnPanel.add(btnExport);
        add(btnPanel, BorderLayout.SOUTH);
// ===== Обработка событий =====
        btnCalculate.addActionListener(e -> performCalculation());
        btnRandom.addActionListener(e -> generateRandom());
        btnExport.addActionListener(e -> exportToTXT());
    }

    private void updateInputFields() {
// Очищаем панель ввода
        inputPanel.removeAll();
        String type = (String) calcTypeBox.getSelectedItem();

        if (type.equals("Расчёт кислородной фурмы")) {
            inputPanel.add(new JLabel("Масса металла (кг):"));
            inputPanel.add(inputField1);
            inputPanel.add(new JLabel("Расход O₂ (м³/ч):"));
            inputPanel.add(inputField2);
            inputPanel.add(new JLabel("Начальная температура (°C):"));
            inputPanel.add(inputField3);
        } else {
            inputPanel.add(new JLabel("Давление p₀ (Па):"));
            inputPanel.add(inputField1);
            inputPanel.add(new JLabel("Температура T₀ (K):"));
            inputPanel.add(inputField2);
            inputPanel.add(new JLabel("Показатель адиабаты γ:"));
            inputPanel.add(inputField3);
            inputPanel.add(new JLabel("Газовая постоянная R:"));
            inputPanel.add(inputField4);
            inputPanel.add(new JLabel("Массовый расход (кг/с):"));
            inputPanel.add(inputField5);
        }
// Обновляем панель
        inputPanel.revalidate();
        inputPanel.repaint();
    }

    private void performCalculation() {
        try {
            String type = (String) calcTypeBox.getSelectedItem();
            Calculator calculator;
            Map<String, Double> params = new HashMap<>();
            if (type.equals("Расчёт кислородной фурмы")) {
                calculator = new OxygenFurnaceCalculator();
                params.put("mass", Double.parseDouble(inputField1.getText()));
                params.put("oxygenFlow", Double.parseDouble(inputField2.getText()));
                params.put("tInitial", Double.parseDouble(inputField3.getText()));
            } else {
                calculator = new LavalNozzleCalculator();
                params.put("p0", Double.parseDouble(inputField1.getText()));
                params.put("T0", Double.parseDouble(inputField2.getText()));
                params.put("gamma", Double.parseDouble(inputField3.getText()));
                params.put("R", Double.parseDouble(inputField4.getText()));
                params.put("massFlow", Double.parseDouble(inputField5.getText()));
            }
            StepResult result = calculator.calculate(params);
            StringBuilder sb = new StringBuilder();
            sb.append("Тип расчёта: ").append(type).append("\n\n");
            for (String step : result.getSteps()) {
                sb.append(step).append("\n");
            }
            sb.append("\nРезультаты:\n");
            for (Map.Entry<String, Double> entry : result.getResults().entrySet()) {
                sb.append("• ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            resultArea.setText(sb.toString());
            try {
                CalculationLog log = new CalculationLog(
                        mainFrame.getCurrentUser().getId(),   // ← вот здесь используется getCurrentUser()
                        type,
                        params.toString(),
                        result.getResults().toString(),
                        java.time.LocalDateTime.now().toString()
                );
                CalculationLogDAO.add(log);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Ошибка сохранения истории: " + ex.getMessage());
            }


        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Введите корректные числовые значения!",
                    "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка расчёта: " + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateRandom() {
        Random r = new Random();
        String type = (String) calcTypeBox.getSelectedItem();
        if (type.equals("Расчёт кислородной фурмы")) {
            inputField1.setText(String.format("%.2f", 500 + r.nextDouble() * 1500));
            inputField2.setText(String.format("%.2f", 100 + r.nextDouble() * 400));
            inputField3.setText(String.format("%.2f", 20 + r.nextDouble() * 30));
        } else {
            inputField1.setText(String.format("%.0f", 80000 + r.nextDouble() * 120000));
            inputField2.setText(String.format("%.0f", 250 + r.nextDouble() * 50));
            inputField3.setText(String.format("%.2f", 1.3 + r.nextDouble() * 0.3));
            inputField4.setText("287.0");
            inputField5.setText(String.format("%.2f", 0.5 + r.nextDouble() * 2.0));
        }
    }

    private void exportToTXT() {
        try (FileWriter fw = new FileWriter("result.txt")) {
            fw.write(resultArea.getText());
            JOptionPane.showMessageDialog(this, "Экспорт выполнен в result.txt");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Ошибка экспорта: " + ex.getMessage());
        }
    }
}