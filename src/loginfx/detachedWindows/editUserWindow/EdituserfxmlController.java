package loginfx.detachedWindows.editUserWindow;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import loginfx.Main;
import loginfx.Scenes;

/**
 * FXML Controller class
 *
 * @author BURAK
 */
public class EdituserfxmlController implements Initializable {

    @FXML
    private Label selectedUser;
    @FXML
    private Button removeButton;
    @FXML
    private CheckBox makeAdminCheckbox;
    @FXML
    private Button doneButton;

    private static String username;
    private static String status;
    private static boolean madeChange;
    private static boolean isAdmin;

    //because display method cant see javafx objects
    private static Label s;
    private static CheckBox admn;

    public boolean display(String username, String status) {
        EdituserfxmlController.username = username;
        EdituserfxmlController.status = status;

        madeChange = false;
        Stage stage = new Stage();
        stage.setResizable(false);
        Scenes.switchScene(Scenes.DetachedScenes.EDIT_USER_SCENE, stage);
        s.setText(String.format("You are editing the user: %s (%s)",
                username, status));
        
        try {
            boolean isAdmin = Main.dbase.isAdmin(username);
            admn.setSelected(isAdmin);
            EdituserfxmlController.isAdmin = isAdmin;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        
        stage.setOnCloseRequest(e -> {
            refreshStatus();
        });

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return madeChange;
    }
    
    private void refreshStatus() {
        boolean admin = admn.isSelected();
        if (admin == isAdmin) {
            return;
        }
        if (!admin && isAdmin) {
            giveUserStatus();
            madeChange = true;
            return;
        }
        if (admin && !isAdmin) {
            giveAdminStatus();
            madeChange = true;
            return;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = selectedUser;
        admn = makeAdminCheckbox;

        removeButton.setOnAction(e -> {
            removeUser();
            Stage s = (Stage) removeButton.getScene().getWindow();
            s.close();
        });
        doneButton.setOnAction(e -> {
            refreshStatus();
            Stage window = (Stage) doneButton.getScene().getWindow();
            window.close();
        });
    }

    private void removeUser() {
        try {
            Main.dbase.deleteUser(this.username);
            madeChange = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void giveAdminStatus() {
        try {
            Main.dbase.giveAdminStatus(username);
            madeChange = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void giveUserStatus() {
        try {
            Main.dbase.giveUserStatus(username);
            madeChange = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
