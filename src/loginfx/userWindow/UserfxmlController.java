package loginfx.userWindow;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import loginfx.Scenes;
import loginfx.detachedWindows.changePassWindow.ChangepassfxmlController;
import loginfx.loginWindow.LoginfxmlController;

/**
 * FXML Controller class
 *
 * @author BURAK
 */
public class UserfxmlController implements Initializable {

    @FXML
    private Button logoutButtonUser;
    @FXML
    private Label myLabelUser;
    @FXML
    private Button changePassButton;
    
    private static String username;

    public void youAreLoggedInAs(String name) {
        username = name;
        myLabelUser.setText("You are logged in as " + name);
    }
    
    private void changePass() {
        new ChangepassfxmlController().display(username);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logoutButtonUser.setOnAction(e -> logout());
        changePassButton.setOnAction(e -> changePass());
        LoginfxmlController.setController(this);
    }

    private void logout() {
        Scenes.switchScene(Scenes.MainScenes.LOGIN_SCENE);
    }

}
