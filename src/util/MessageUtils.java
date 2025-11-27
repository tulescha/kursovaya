package util;

import javax.swing.*;

public class MessageUtils {

    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarning(String message) {
        JOptionPane.showMessageDialog(null, message, "Предупреждение", JOptionPane.WARNING_MESSAGE);
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean showConfirm(String message) {
        int result = JOptionPane.showConfirmDialog(null, message, "Подтверждение",
                JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    // Специфичные сообщения для приложения
    public static void showCalculationSuccess() {
        showInfo("Расчет успешно выполнен и сохранен в историю!");
    }

    public static void showDataExported(String filename) {
        showInfo("Данные успешно экспортированы в файл: " + filename);
    }

    public static void showLoginRequired() {
        showWarning("Для выполнения этой операции необходимо войти в систему!");
    }
}