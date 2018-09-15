package loginfx.loginWindow;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import loginfx.Main;
import loginfx.Scenes;
import loginfx.adminWindow.AdminfxmlController;
import loginfx.userWindow.UserfxmlController;

/**
 * FXML Controller class
 *
 * @author BURAK
 */
public class LoginfxmlController implements Initializable {

    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private Button button;
    @FXML
    private TextField username, password;
    @FXML
    private Label info;
    
    //to be able to pass in the person's username logging in
    private static UserfxmlController usrfxml;
    private static AdminfxmlController adminfxml;
    
    //called from UserfxmlController.java
    public static void setController(UserfxmlController u) {
        usrfxml = u;
    }
    
    //called from AdminfxmlController.java
    public static void setController(AdminfxmlController a) {
        adminfxml = a;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choiceBox.getItems().removeAll(choiceBox.getItems());
        choiceBox.getItems().addAll("User", "Admin");
        choiceBox.getSelectionModel().select("User");

        button.setOnAction(e -> tryToLogin());
        username.setOnAction(e -> tryToLogin());
        password.setOnAction(e -> tryToLogin());
    }
    
    private void resetTextFields() {
        username.setText("");
        password.setText("");
        info.setText("");
    }
    
    private void toUserScreen() {
        Scenes.switchScene(Scenes.MainScenes.USER_SCENE);
        resetTextFields();
    }

    private void toAdminScreen() {
        Scenes.switchScene(Scenes.MainScenes.ADMIN_SCENE);
        resetTextFields();
    }
    
    private void tryToLogin() {
        String status = (String) choiceBox.getSelectionModel()
                .getSelectedItem();
        String usrname = username.getText();
        String passwd = password.getText();

        usrname = usrname.replaceAll("'", "");
        passwd = passwd.replaceAll("'", "");

        username.setText(usrname);
        password.setText(passwd);
        
        if (usrname.equals("") || passwd.equals("")) {
            info.setText("Don't leave fields empty");
            return;
        }

        boolean login;

        try {
            switch (status) {
                case "Admin":
                    login = Main.dbase.adminLogin(usrname, passwd);
                    if (login) {
                        //give the username of the person logging in
                        adminfxml.youAreLoggedInAs(usrname);
                        toAdminScreen();
                    } else {
                        info.setText("Wrong credentials");
                    }
                    break;
                case "User":
                    login = Main.dbase.userLogin(usrname, passwd);
                    if (login) {
                        //give the username of the person logging in
                        usrfxml.youAreLoggedInAs(usrname);
                        toUserScreen();
                    } else {
                        info.setText("Wrong credentials");
                    }
                    break;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
