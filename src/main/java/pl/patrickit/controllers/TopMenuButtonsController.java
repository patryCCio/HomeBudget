package pl.patrickit.controllers;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import pl.patrickit.Main;
import pl.patrickit.utils.DialogUtils;

import java.net.URL;
import java.util.Optional;

public class TopMenuButtonsController{

    @FXML
    private MainController mainController;

    @FXML
    private ToggleGroup toggleButtons;

    public static final String OPEN_FINANCE = "/fxml/layouts/finances.fxml";
    private static final int CHOICE = 1;

    //0 -> compile
    //1 -> jar

    private void init(){
        if(toggleButtons.getSelectedToggle()==null){
            if(CHOICE==0){
            String cssPath = "src/main/resources/fxml/style/styleInformation.css";
            mainController.setCss(cssPath);
            }else{
            String jarPath = "fxml/style/styleInformation.css";
            mainController.setCssJar(jarPath);
            }
            mainController.setCenterPane("/fxml/layouts/information.fxml");
        }
    }

    @FXML
    public void openFinance() {
        if(CHOICE==0){
            String cssPath = "src/main/resources/fxml/style/style.css";
            mainController.setCss(cssPath);
        }else{
            String jarPath = "fxml/style/style.css";
            mainController.setCssJar(jarPath);
        }
        mainController.setCenterPane(OPEN_FINANCE);
        init();
    }

    @FXML
    public void openPermanentSettlements() {
        if(CHOICE==0){
            String cssPath = "src/main/resources/fxml/style/stylePermanent.css";
            mainController.setCss(cssPath);
        }else{
            String jarPath = "fxml/style/stylePermanent.css";
            mainController.setCssJar(jarPath);
        }
        mainController.setCenterPane("/fxml/layouts/permanent.fxml");
        init();
    }

    @FXML
    public void openPlanning() {
        if(CHOICE==0){
            String cssPath = "src/main/resources/fxml/style/stylePlanning.css";
            mainController.setCss(cssPath);
            }else{
            String jarPath = "fxml/style/stylePlanning.css";
            mainController.setCssJar(jarPath);
            }
        mainController.setCenterPane("/fxml/layouts/planning.fxml");
        init();
    }

    @FXML
    public void openManagement() {
        if(CHOICE==0){
            String cssPath = "src/main/resources/fxml/style/styleManagement.css";
            mainController.setCss(cssPath);
        }else{
            String jarPath = "fxml/style/styleManagement.css";
            mainController.setCssJar(jarPath);
        }
        mainController.setCenterPane("/fxml/layouts/management.fxml");
        init();
    }

    @FXML
    public void openCredits() {
        if(CHOICE==0){
            String cssPath = "src/main/resources/fxml/style/styleCredit.css";
            mainController.setCss(cssPath);
        }else{
            String jarPath = "fxml/style/styleCredit.css";
            mainController.setCssJar(jarPath);
        }
        mainController.setCenterPane("/fxml/layouts/credit.fxml");
        init();
    }


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}
