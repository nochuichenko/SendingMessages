package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 * Главный класс приложения, расширяющий {@link Application}. Запускает
 * графический интерфейс приложения.
 */
public class Main  extends Application {
    /**
     * Метод, вызывающийся при запуске приложения. Загружает графический интерфейс
     * из файла "MainWindow.fxml" и отображает его в окне приложения.
     *
     * @param primaryStage Стартовое окно приложения.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Информационная рассылка");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Главный метод, запускающий приложение.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile", "log4j2.xml");
        launch(args);
    }
}
