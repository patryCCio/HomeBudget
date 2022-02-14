package pl.patrickit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pl.patrickit.database.dbutils.DbManager;
import pl.patrickit.utils.FxmlUtils;

import java.io.File;
import java.util.Locale;

public class Main extends Application {

    public static final String MAIN_PANE = "/fxml/layouts/mainPane.fxml";
    public static String version = "1.0.3";
    public static void main(String[] args) {
        launch();
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public Stage mainStage = new Stage();

    @Override
    public void start(Stage stage) throws Exception {

        //Locale.setDefault(Locale.ENGLISH);
        Pane mainPane = FxmlUtils.fxmlLoader(MAIN_PANE);
        Scene scene = new Scene(mainPane);

        scene.getStylesheets().clear();
        scene.getStylesheets().add("fxml/style/styleInformation.css");

        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setTitle(FxmlUtils.getResourceBundle().getString("title.application") + " v." + version);
        Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
        stage.alwaysOnTopProperty();
        stage.show();
        setMainStage(stage);
        DbManager.initDatabase();
    }

}
