package application;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.apache.logging.log4j.*;

import static application.WorkingWithFiles.readingFiles;

/**
 * Класс для отправки писем с использованием протокола SMTP.
 * Использует класс User для получения учетных данных отправителя.
 */
public class Mailing {
    static User user = new User();
    String senderEmail = user.getLogin();
    String senderPassword = user.getPassword();
    Map<String, String> recipientEmails;
    String mailsFilePath, messageFilePath = null;
    private static final Logger logger = LogManager.getLogger(Mailing.class.getName());

    /**
     * Конструктор класса Mailing.
     *
     * @param mailsFilePath    Путь к файлу с адресами электронной почты получателей.
     * @param messageFilePath  Путь к файлу с текстом сообщения.
     * @throws NoSuchPaddingException     Если запрошенный padding не поддерживается.
     * @throws IllegalBlockSizeException   Если длина данных некорректна для алгоритма шифрования.
     * @throws NoSuchAlgorithmException   Если указанный алгоритм шифрования не поддерживается.
     * @throws BadPaddingException         Если возникает ошибка в процессе шифрования.
     * @throws InvalidKeyException        Если указанный ключ недействителен для шифрования.
     */
    public Mailing(String mailsFilePath, String messageFilePath) throws NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        this.mailsFilePath = mailsFilePath;
        this.messageFilePath = messageFilePath;
        this.recipientEmails = getRecipients();
    }

    /**
     * Начинает процесс рассылки писем.
     *
     * @return true, если рассылка завершилась успешно, false в противном случае.
     */
    public boolean startMailing() {
        try {
            // Отправка сообщений для каждого получателя
            for (Map.Entry<String, String> entry : recipientEmails.entrySet()) {
                Transport.send(makeMessage(entry.getKey(), entry.getValue()));
                logger.info("Письмо успешно отправлено для: " + entry.getKey());
            }
            return true; // Успешное выполнение
        } catch (MessagingException e) {
            logger.error("Ошибка при отправке письма", e);
            return false; // Неуспешное выполнение
        }
    }

    /**
     * Создает объект сообщения для заданного получателя.
     *
     * @param recipientEmail   Email получателя.
     * @param recipientName    Имя получателя.
     * @return Объект сообщения для отправки.
     * @throws MessagingException   Если возникает ошибка при создании или настройке сообщения.
     */
    private Message makeMessage(String recipientEmail, String recipientName) throws MessagingException {
        // Создание объекта сообщения
        Message message = new MimeMessage(makeSession());
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

        // Установка темы письма с именем получателя
        message.setSubject("Привет, " + recipientName + "!");

        // Установка текста письма
        message.setText(readingFiles(messageFilePath));

        return message;
    }

    /**
     * Создает сессию для отправки сообщений с использованием SMTP.
     *
     * @return Сессия для отправки сообщений.
     */
    private Session makeSession() {
        // Настройка свойств для соединения с SMTP-сервером
        Properties props = new Properties();
        connectionProperties(props);

        // Аутентификация отправителя
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        };

        return Session.getInstance(props, authenticator);
    }

    /**
     * Настраивает свойства для соединения с SMTP-сервером.
     *
     * @param props Свойства для настройки соединения.
     */
    private void connectionProperties(Properties props) {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    /**
     * Получает адреса и имена получателей из файла.
     *
     * @return Карта адресов и имен получателей.
     */
    private Map<String, String> getRecipients() {
        // Проверка на существование файла
        File file = new File(mailsFilePath);
        if (!file.exists() || !file.isFile()) {
            logger.error("Файл '" + mailsFilePath + "' не существует или не является файлом");
            return Collections.emptyMap();
        }

        // Чтение строк из файла
        List<String> lines = readLinesFromFile(file);

        Map<String, String> recipients = new HashMap<>();
        for (String line : lines) {
            // Проверка на непустую строку
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(" ");
                if (parts.length == 2 && isValidEmail(parts[0])) {
                    recipients.put(parts[0], parts[1]);
                } else {
                    logger.error("Некорректная строка в файле '" + mailsFilePath + "': " + line);
                }
            }
        }

        return recipients;
    }

    /**
     * Читает все строки из указанного файла.
     *
     * @param file Файл для чтения строк.
     * @return Список строк из файла.
     * @throws RuntimeException Если возникает ошибка при чтении файла, включая {@link IOException}.
     */
    private List<String> readLinesFromFile(File file) {
        try {
            return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Возникновение исключения при чтении файла '" + mailsFilePath + "'", e);
            return Collections.emptyList();
        }
    }

    /**
     * Проверяет, является ли переданный адрес электронной почты корректным.
     *
     * @param email Адрес электронной почты для проверки.
     * @return true, если адрес электронной почты корректен, false в противном случае.
     */
    private boolean isValidEmail(String email) {
        // Проверка почтового адреса с использованием регулярного выражения
        String emailRegex = "^[a-zA-Z0-9_\\-.]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }
}