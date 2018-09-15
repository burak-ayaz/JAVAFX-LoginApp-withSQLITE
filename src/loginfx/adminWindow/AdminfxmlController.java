package loginfx.adminWindow;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import loginfx.Scenes;
import loginfx.database.Database;
import loginfx.database.DatabaseUtil;
import loginfx.detachedWindows.AlertBox;
import loginfx.loginWindow.LoginfxmlController;
import loginfx.detachedWindows.addUserWindow.AdduserfxmlController;
import loginfx.detachedWindows.editUserWindow.EdituserfxmlController;

/**
 * FXML Controller class
 *
 * @author BURAK
 */
public class AdminfxmlController implements Initializable {

    @FXML
    private Button logoutButtonAdmin;
    @FXML
    private TableView myTable;
    @FXML
    private Button addUserButton;
    @FXML
    private Button editUserButton;
    @FXML
    private Label myLabelAdmin;
    @FXML
    private Button refreshTableButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logoutButtonAdmin.setOnAction(e -> logout());
        addUserButton.setOnAction(e -> addUser());
        editUserButton.setOnAction(e -> editUser());
        refreshTableButton.setOnAction(e -> refreshTable());
        refreshTable();
        myTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        LoginfxmlController.setController(this);
    }
    
    private void refreshTable() {
        DatabaseUtil.fillTable(myTable);
    }

    public void youAreLoggedInAs(String name) {
        myLabelAdmin.setText("You are logged in as " + name);
    }

    private void addUser() {
        //returns false if you close window without adding anyone
        //if we close the window without using add button,
        //we dont need to update the tableview (optimisation)
        boolean madeChange = new AdduserfxmlController().display();
        if (madeChange) {
            DatabaseUtil.fillTable(myTable);
        }
    }

    private void editUser() {
        Object o = myTable.getSelectionModel().getSelectedItems().get(0);
        
        //if no user in table is selected
        if (o == null) {
            AlertBox.display("No user selected", "Select a user from the table for editing");
            return;
        }
        String[] info = o.toString()
                .replaceAll("\\[", "").replaceAll("\\]", "").split(", ");
        String selectedUsername = info[0];
        String currentStatus = info[2];
        
        if (selectedUsername.equals(Database.admin_username)) {
            AlertBox.display("Error", "Main admin can not be edited");
            return;
        }
        //same concept with addUser()
        //we pass in the selected username so the class knows which user to edit
        boolean madeChange = new EdituserfxmlController()
                .display(selectedUsername, currentStatus);
        if (madeChange) {
            refreshTable();
        }
    }

    private void logout() {
        Scenes.switchScene(Scenes.MainScenes.LOGIN_SCENE);
    }

}
