package ui;

import javax.swing.*;
import java.awt.*;

public class AlgorithmPanel extends JPanel {
    public AlgorithmPanel() {
        setLayout(new BorderLayout());

        JTextArea algorithmText = new JTextArea();
        algorithmText.setEditable(false);
        algorithmText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        algorithmText.setBorder(BorderFactory.createTitledBorder("Визуализация алгоритма расчета"));

        String text =
                "--- АЛГОРИТМ РАСЧЕТА СОПЛА ЛАВАЛЯ ---\n\n" +
                        "1. ВВОД ДАННЫХ:\n" +
                        "   - Давление p₀, Температура T₀, Показатель адиабаты γ, Массовый расход ṁ.\n\n" +
                        "2. РАСЧЕТЫ:\n" +
                        "   - 2.1. Скорость звука: a₀ = sqrt(γ R T₀)\n" +
                        "   - 2.2. Плотность при входе: ρ₀ = p₀ / (R T₀)\n" +
                        "   - 2.3. Критическое сечение: A* = ṁ / (ρ₀ a₀)\n" +
                        "   - 2.4. Выходное сечение: Aₑ = A* * f(Mₑ, γ)\n\n" +
                        "--- АЛГОРИТМ РАСЧЕТА КИСЛОРОДНОЙ ФУРМЫ ---\n\n" +
                        "1. ВВОД ДАННЫХ:\n" +
                        "   - Масса металла, Расход O₂, Начальная температура T₀.\n\n" +
                        "2. РАСЧЕТЫ:\n" +
                        "   - 2.1. Энергия нагрева: Q = k * mass\n" +
                        "   - 2.2. Конечная температура: T₁ = T₀ + Q / (mass * c)\n" +
                        "   - 2.3. Время продувки: t = mass / oxygenFlow\n" +
                        "   - 2.4. Общий расход O₂: V_O₂ = oxygenFlow * t\n";

        algorithmText.setText(text);

        JScrollPane scrollPane = new JScrollPane(algorithmText);
        add(scrollPane, BorderLayout.CENTER);
    }
}