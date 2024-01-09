package application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Утилитарный класс для работы с файлами.
 * Предоставляет методы для чтения содержимого текстового файла.
 */
public class WorkingWithFiles {
    private static final Logger logger = LogManager.getLogger(WorkingWithFiles.class.getName());

    /**
     * Читает содержимое текстового файла по указанному пути.
     *
     * @param path Путь к текстовому файлу.
     * @return Строка, содержащая полное содержимое файла.
     *         Пустая строка, если файл не существует или не является файлом.
     */
    public static String readingFiles(String path) {
        StringBuilder content = new StringBuilder();

        // Проверка на существование файла
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            logger.error("Файл '" + path + "' не существует или не является файлом");
            return "";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

            logger.info("Файл '" + path + "' успешно прочитан");
        } catch (IOException e) {
            logger.error("Возникновение исключения при чтении файла '" + path + "'", e);
        }

        return content.toString();
    }
}
