package application;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Контроллер для главного окна приложения, реализующий логику взаимодействия пользователя с интерфейсом.
 */
public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button mailingButton;

    @FXML
    private TextField mailsFilePath;

    @FXML
    private TextField messageFilePath;

    @FXML
    private Label completedLabel;

    @FXML
    private Label errorLabel;

    /**
     * Инициализация контроллера. Скрывает метки `completedLabel` и `errorLabel` при старте приложения.
     */
    @FXML
    void initialize() {
        completedLabel.setVisible(false);
        errorLabel.setVisible(false);
        mailingButton.setOnAction(event -> {
            String mailsPath = mailsFilePath.getText();
            String messagePath = messageFilePath.getText();

            if (isValidFilePath(mailsPath) && isValidFilePath(messagePath)) {
                try {
                    mailingExecution(mailsPath, messagePath);
                } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                         BadPaddingException | InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Обработка случая, когда пути недействительны
                completedLabel.setVisible(false);
                errorLabel.setVisible(true);
            }
        });
    }

    /**
     * Проверяет, является ли путь к файлу действительным и доступным для чтения.
     *
     * @param filePath Путь к файлу для проверки.
     * @return true, если путь к файлу действителен и файл доступен для чтения, false в противном случае.
     */
    private boolean isValidFilePath(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path);
    }

    /**
     * Выполняет отправку писем с использованием введенных путей к файлам.
     *
     * @param mailsPath   Путь к файлу с адресами электронной почты.
     * @param messagePath Путь к файлу с текстом сообщения.
     * @throws NoSuchPaddingException    Если алгоритм заполнения не существует.
     * @throws IllegalBlockSizeException Если размер блока шифротекста неверен.
     * @throws NoSuchAlgorithmException  Если указанный алгоритм шифрования не существует.
     * @throws BadPaddingException        Если заполнение данных неверно.
     * @throws InvalidKeyException       Если ключ недействителен.
     */
    private void mailingExecution(String mailsPath, String messagePath) throws NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        boolean success = new Mailing(mailsPath, messagePath).startMailing();
        if (success) {
            // Выполните дополнительные действия после успешного выполнения
            completedLabel.setVisible(true);
            errorLabel.setVisible(false);
        } else {
            // Выполните дополнительные действия в случае неудачи
            completedLabel.setVisible(false);
            errorLabel.setVisible(true);
        }
    }
}
