package loginfx.detachedWindows.addUserWindow;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
public class AdduserfxmlController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button addButton;
    @FXML
    private Label label;
    @FXML
    private CheckBox isAdmin;
    
    //need these references to be able to pass resetTextFields() to
    //on close request in method display().
    //if you try to use @FXML variables instead, it gives a nullpointer ex??
    private static TextField u;
    private static TextField p;
    private static Label l;
    private static CheckBox i;

    private static boolean add;
    
    //returns false if window gets closed without adding anyone to database
    public boolean display() {
        add = false;
        Stage stage = new Stage();
        stage.setResizable(false);
        Scenes.switchScene(Scenes.DetachedScenes.ADD_USER_SCENE, stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOnCloseRequest(e -> {
            resetTextFields();
        });
        stage.showAndWait();
        return add;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addButton.setOnAction(e -> {
            add();
        });
        username.setOnAction(e -> {
            add();
        });
        password.setOnAction(e -> {
            add();
        });
        
        u = username;
        p = password;
        l = label;
        i = isAdmin;
    }

    private void resetTextFields() {
        u.setText("");
        p.setText("");
        l.setText("");
        i.setSelected(false);
    }

    private void add() {
        String usrnm = username.getText();
        String passwd = password.getText();

        usrnm = usrnm.replaceAll("'", "");
        passwd = passwd.replaceAll("'", "");
        
        username.setText(usrnm);
        password.setText(passwd);
        
        if (usrnm.equals("") || passwd.equals("")) {
            label.setText("Username or password field can not be empty");
            return;
        }
        
        if (usrnm.contains("[") || usrnm.contains("]") || 
                passwd.contains("[") || passwd.contains("]")) {
            label.setText("Username or password can not contain '[' or ']'");
            return;
        }

        try {
            if (Main.dbase.usernameExists(usrnm)) {
                label.setText("A user with given username already exists");
                return;
            }
            Main.dbase.addUser(usrnm, passwd);
            add = true;
            if (isAdmin.isSelected()) {
                Main.dbase.giveAdminStatus(usrnm);
            }
            resetTextFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //close the stage
        Stage s = (Stage) addButton.getScene().getWindow();
        s.close();
    }

}
