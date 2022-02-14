package pl.patrickit.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.w3c.dom.Text;
import pl.patrickit.Main;
import pl.patrickit.utils.DialogUtils;
import pl.patrickit.utils.FxmlUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;


public class MainController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private TopMenuButtonsController topMenuButtonsController;

    @FXML
    private Label versionLabel;

    public static int ACTUAL_MONTH;

    public void setCss(String css){
        File file = new File(css);
        Scene scene = mainPane.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + file.getAbsolutePath().replace("\\", "/"));
    }

    public void setCssJar(String css){
        Scene scene = mainPane.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
    }

    public void setCenterPane(String fxmlPath){
        mainPane.setCenter(FxmlUtils.fxmlLoader(fxmlPath));
    }

    public void initialize() {
        this.topMenuButtonsController.setMainController(this);
        versionLabel.setText("v. " + Main.version);
        getMonth();
    }

    private void getMonth(){
        Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        ACTUAL_MONTH = cal.get(Calendar.MONTH);
    }



    public void signOut(MouseEvent mouseEvent) {
        Optional<ButtonType> result = DialogUtils.closeDialog();
        if(result.get()==ButtonType.OK){
            Platform.exit();
            System.exit(0);
        }
    }
}
