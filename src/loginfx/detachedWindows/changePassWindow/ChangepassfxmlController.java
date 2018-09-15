package loginfx.detachedWindows.changePassWindow;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import loginfx.Main;
import loginfx.Scenes;

/**
 * FXML Controller class
 *
 * @author BURAK
 */
public class ChangepassfxmlController implements Initializable {
    
    @FXML
    private TextField newPassField ;
    @FXML
    private Button changePassButton;
    
    private static String username;
    private static TextField p;
    
    public void display(String username) {
        ChangepassfxmlController.username = username;
        Stage stage = new Stage();
        stage.setResizable(false);
        Scenes.switchScene(Scenes.DetachedScenes.CHANGE_PASS_SCENE, stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnCloseRequest(e -> {
            resetTextField();
        });
        stage.showAndWait();
    }
    
    private void resetTextField() {
        p.setText("");
    }
    
    private void changePass() {
        String newpass = newPassField.getText();
        try {
            Main.dbase.changePassword(username, newpass);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        Stage stage = (Stage) changePassButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        changePassButton.setOnAction(e -> changePass());
        p = newPassField;
    }    
    
}
