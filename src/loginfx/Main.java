package loginfx;

import loginfx.database.Database;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author BURAK
 */
public class Main extends Application {
    
    //connect to the database
    public static final Database dbase = new Database(); 
    private static Stage stage;
    
    //called from Scenes class
    public static Stage getStage() {
        return stage;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        
        //autoclose the database connection before exiting program
        stage.setOnCloseRequest(e -> {
            dbase.close();
        });
        
        Scenes.switchScene(Scenes.MainScenes.LOGIN_SCENE);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
