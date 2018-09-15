package loginfx.detachedWindows;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author BURAK
 */
public class AlertBox {
    public static void display(String title, String message) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        
        Label label = new Label();
        label.setText(message);
        
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> stage.close());
        
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, closeButton);
        
        Scene scene = new Scene(layout, 250, 100);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
