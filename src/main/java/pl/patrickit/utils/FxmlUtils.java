package pl.patrickit.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.util.ResourceBundle;

public class FxmlUtils {

    private static ResourceBundle bundle = FxmlUtils.getResourceBundle();

    public static Pane fxmlLoader(String fxmlPath){
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        loader.setResources(getResourceBundle());
        try{
            return loader.load();
        }catch (Exception e){
            DialogUtils.errorDialog(e.getMessage());
        }
        return null;
    }

    public static ResourceBundle getResourceBundle(){
        return ResourceBundle.getBundle("bundles.message");
    }



}
