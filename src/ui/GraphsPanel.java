package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class GraphsPanel extends JPanel {
    public GraphsPanel() {
        setLayout(new BorderLayout());

        // --- Панель для имитации графика (Здесь должен быть JFreeChart) ---
        JPanel chartPlaceholder = new JPanel(new GridBagLayout());
        chartPlaceholder.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Заглушка, имитирующая график для Сопла Лаваля
        JLabel label = new JLabel("График: Зависимость A/A* от числа Маха (JFreeChart)", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        chartPlaceholder.add(label);

        // --- Нижняя строка для экспорта ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton exportPdfButton = new JButton("Экспорт графика в PDF (Требует iText)");
        bottomPanel.add(exportPdfButton);

        add(chartPlaceholder, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Обработчик экспорта
        exportPdfButton.addActionListener(this::onExportPdf);
    }

    private void onExportPdf(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить график как PDF");
        fileChooser.setSelectedFile(new File("График_Сопла_Лаваля.pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Здесь должна быть логика:
            // 1. Создание объекта Document (iText)
            // 2. Рендеринг JFreeChart в Image/Graphics2D
            // 3. Добавление изображения в Document
            // 4. Сохранение Document в файл.

            JOptionPane.showMessageDialog(this,
                    "График сохранен в файл: " + file.getAbsolutePath() +
                            "\n(Требуется подключение библиотеки iText/PDFBox для фактического экспорта).",
                    "Экспорт завершен", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}