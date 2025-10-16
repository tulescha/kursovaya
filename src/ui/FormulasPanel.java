package ui;

import javax.swing.*;
import java.awt.*;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class FormulasPanel extends JPanel {
    public FormulasPanel() {
        setLayout(new BorderLayout(10, 10));

        // Панель для размещения формул
        JPanel formulasContainer = new JPanel();
        formulasContainer.setLayout(new BoxLayout(formulasContainer, BoxLayout.Y_AXIS));
        formulasContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formulasContainer.add(createTitleLabel("Основные формулы расчета"));
        formulasContainer.add(Box.createVerticalStrut(20));

        // 1. Сопло Лаваля
        formulasContainer.add(createSubtitleLabel("1. Расчет Сопла Лаваля (Газодинамика)"));
        formulasContainer.add(Box.createVerticalStrut(10));
        formulasContainer.add(createFormulaBlock("Скорость звука:", "a_0 = \\sqrt{\\gamma R T_0}", 18));
        formulasContainer.add(createFormulaBlock("Критическое сечение (A^*):", "A^* = \\frac{\\dot{m}}{\\rho_0 a_0}", 18));
        formulasContainer.add(createFormulaBlock("Выходное сечение (A_e):", "A_e = A^* \\cdot \\frac{1}{M_e} \\cdot \\left[ \\frac{1 + \\frac{\\gamma - 1}{2} M_e^2}{1 + \\frac{\\gamma - 1}{2}} \\right]^{\\frac{\\gamma + 1}{2(\\gamma - 1)}}", 18));

        formulasContainer.add(Box.createVerticalStrut(30));

        // 2. Кислородная Фурма
        formulasContainer.add(createSubtitleLabel("2. Расчет Кислородной Фурмы (Тепловой баланс)"));
        formulasContainer.add(Box.createVerticalStrut(10));
        formulasContainer.add(createFormulaBlock("Конечная температура (T_1):", "T_1 = T_0 + \\frac{Q}{\\text{mass} \\cdot c}", 18));
        formulasContainer.add(createFormulaBlock("Общий расход кислорода (V_{O_2}):", "V_{O_2} = Q_{\\text{flow}} \\cdot t", 18));

        formulasContainer.add(Box.createVerticalGlue()); // Заполнитель для выравнивания

        JScrollPane scrollPane = new JScrollPane(formulasContainer);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Вспомогательный метод для создания заголовка
    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Serif", Font.BOLD, 24));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // Вспомогательный метод для создания подзаголовка
    private JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Serif", Font.BOLD, 18));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // Вспомогательный метод для рендеринга формулы с помощью JLaTeXMath
    private JPanel createFormulaBlock(String description, String latex, int size) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        panel.add(descLabel);

        try {
            // Создание иконки из LaTeX-строки
            TeXFormula formula = new TeXFormula(latex);
            TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, size); // Исправленная строка

            // Создание метки с иконкой
            JLabel formulaLabel = new JLabel(icon);
            panel.add(formulaLabel);
        } catch (Exception e) {
            // В случае ошибки (например, если библиотека не подключена)
            JLabel errorLabel = new JLabel("Ошибка рендеринга: " + latex);
            errorLabel.setForeground(Color.RED);
            panel.add(errorLabel);
            e.printStackTrace(); // Для отладки
        }
        return panel;
    }
}