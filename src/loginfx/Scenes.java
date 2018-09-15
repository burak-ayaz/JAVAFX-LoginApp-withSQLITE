package loginfx;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author BURAK
 */
//A class for managing scenes used in the whole app.
//Main purpose: setting different scenes to the main stage
public class Scenes {

    //main scenes
    private static Scene loginScene, userScene, adminScene;

    //detached windows you open from adminScene
    private static Scene addUserScene, editUserScene;
    
    //detached windows you open from userScene
    private static Scene changePassScene;

    //to be able to switch scenes using the reference
    private static Stage mainstage;

    //this method should be called with main scenes only
    //otherwise we could give the content of a detached window to
    //the main stage
    public static void switchScene(MainScenes scene) {
        mainstage.setScene(scene.getScene());
        mainstage.setTitle(scene.getTitle());
        mainstage.centerOnScreen();
    }

    public enum MainScenes {
        LOGIN_SCENE(loginScene, "Login"),
        USER_SCENE(userScene, "User"),
        ADMIN_SCENE(adminScene, "Admin");

        Scene scene;
        String title;

        MainScenes(Scene s, String t) {
            scene = s;
            title = t;
        }

        Scene getScene() {
            return scene;
        }

        String getTitle() {
            return title;
        }
    }

    //in order to give content to a detached stage
    public static void switchScene(DetachedScenes scene, Stage stage) {
        stage.setScene(scene.getScene());
        stage.setTitle(scene.getTitle());
        stage.centerOnScreen();
    }

    public enum DetachedScenes {
        ADD_USER_SCENE(addUserScene, "Add a new user"),
        EDIT_USER_SCENE(editUserScene, "Edit existing user"),
        CHANGE_PASS_SCENE(changePassScene, "Change your password");

        Scene scene;
        String title;

        DetachedScenes(Scene s, String t) {
            scene = s;
            title = t;
        }

        Scene getScene() {
            return scene;
        }

        String getTitle() {
            return title;
        }
    }

    private static Scene buildScene(String fxmlpath) {
        try {
            Parent root = (Parent) FXMLLoader
                    .load(Scenes.class.getResource(fxmlpath));
            return new Scene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    static {
        //load all fxml files and create scenes with them
        loginScene = buildScene("loginWindow/loginfxml.fxml");
        adminScene = buildScene("adminWindow/adminfxml.fxml");
        userScene = buildScene("userWindow/userfxml.fxml");
        addUserScene = buildScene("detachedWindows/addUserWindow/adduserfxml.fxml");
        editUserScene = buildScene("detachedWindows/editUserWindow/edituserfxml.fxml");
        changePassScene = buildScene("detachedWindows/changePassWindow/changepassfxml.fxml");

        //get the stage from the main class where it is created
        Scenes.mainstage = Main.getStage();
    }
}
