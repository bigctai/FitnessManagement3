package Project3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A GymManagerMain class that extends the Application class.
 * Provides methods to load the fxml file and launch the
 * application with the controller class.
 *
 * @author Chris Tai, Shreyank Yelagoila
 */
public class GymManagerMain extends Application {
    @Override
    /**
     * Main entry point for Gym Manager application. Loads fxml file
     * and set scene for application.
     */
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GymManagerMain.class.getResource("GymManagerView.fxml"));
        fxmlLoader.setController(new GymManagerController());
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Gym Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the main Gym Manager application
     * @param args array of strings from command line
     */
    public static void main(String[] args) {
        launch();
    }
}