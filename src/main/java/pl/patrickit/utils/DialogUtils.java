package pl.patrickit.utils;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.patrickit.Main;

import java.util.Optional;
import java.util.ResourceBundle;


public class DialogUtils {

    static ResourceBundle bundle = FxmlUtils.getResourceBundle();

    public static void dialogAbout(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(bundle.getString("about.title")+Main.version);
        info.setHeaderText(bundle.getString("about.header"));
        info.setContentText(bundle.getString("about.content"));
        info.initStyle(StageStyle.DECORATED);
        Stage stage = (Stage) info.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        info.showAndWait();
    }

    public static void dialogErrorBudget(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(bundle.getString("money.title"));
        info.setHeaderText(bundle.getString("money.header"));
        info.setContentText(bundle.getString("money.content.budget"));
        info.initStyle(StageStyle.DECORATED);
        Stage stage = (Stage) info.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        info.showAndWait();
    }

    public static void onlyPlusDialog(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(bundle.getString("plus.title"));
        info.setHeaderText(bundle.getString("plus.header"));
        info.setContentText(bundle.getString("plus.content"));
        info.initStyle(StageStyle.DECORATED);
        Stage stage = (Stage) info.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        info.showAndWait();
    }

    public static void wrongFormat(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(bundle.getString("wrong.title"));
        info.setHeaderText(bundle.getString("wrong.header"));
        info.setContentText(bundle.getString("wrong.content"));
        info.initStyle(StageStyle.DECORATED);
        Stage stage = (Stage) info.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        info.showAndWait();
    }

    public static void dialogErrorMoneyAside(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(bundle.getString("money.title"));
        info.setHeaderText(bundle.getString("money.header"));
        info.setContentText(bundle.getString("money.content.money.aside"));
        info.initStyle(StageStyle.DECORATED);
        Stage stage = (Stage) info.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        info.showAndWait();
    }

    public static void dialogEmptyList(){
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(bundle.getString("list.title"));
        info.setHeaderText(bundle.getString("list.header"));
        info.setContentText(bundle.getString("list.content"));
        info.initStyle(StageStyle.DECORATED);
        Stage stage = (Stage) info.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        info.showAndWait();
    }

    public static Optional<ButtonType> additionalSalary(){
        Alert info = new Alert(Alert.AlertType.CONFIRMATION);
        info.setTitle(bundle.getString("add.salary"));
        info.setHeaderText(bundle.getString("additional.salary.header"));
        info.initStyle(StageStyle.DECORATED);
        Stage stage = (Stage) info.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.toFront();
        Optional<ButtonType> result = info.showAndWait();
        return result;
    }

    public static Optional<ButtonType> closeDialog(){
        Alert info = new Alert(Alert.AlertType.CONFIRMATION);
        info.setTitle(bundle.getString("exit.title"));
        info.setHeaderText(bundle.getString("exit.header"));
        Stage stage = (Stage) info.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        Optional<ButtonType> result = info.showAndWait();
        return result;
    }

    public static void errorDialog(String error){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(bundle.getString("alert.title"));
        errorAlert.setHeaderText(bundle.getString("alert.header"));
        Label textArea = new Label(error);
        errorAlert.getDialogPane().setContent(textArea);
        errorAlert.showAndWait();
    }


    public static int addNewDialog(String bundleString, String fxmlPath){
        int returnResult=1;
        DialogPane dialogPane = new DialogPane();
        dialogPane.setContent(FxmlUtils.fxmlLoader(fxmlPath));
        ButtonType buttonOk = new ButtonType("OK");
        ButtonType buttonCancel = new ButtonType("Cancel");

        dialogPane.getButtonTypes().add(buttonOk);
        dialogPane.getButtonTypes().add(buttonCancel);
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setDialogPane(dialogPane);
        alert.setTitle(bundle.getString(bundleString));

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonOk){
            returnResult = 0;
        }else{
            alert.close();
        }
        return returnResult;
    }

    public static void dialogEmpty() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle(bundle.getString("empty.title"));
        info.setHeaderText(bundle.getString("empty.header"));
        info.setContentText(bundle.getString("empty.content"));
        Stage stage = (Stage) info.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        info.showAndWait();
    }
}
