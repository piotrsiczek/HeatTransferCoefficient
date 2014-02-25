/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coefficient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 * @author piotr
 */
public class Coefficient extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException 
    {  
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Coefficient.fxml"));
        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        //primaryStage.getIcons().add(new Image("/path/to/stackoverflow.jpg"));
        primaryStage.setScene(scene);
        
        CoefficientController c  = loader.getController();
        c.initGUI();
        primaryStage.getScene().getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        primaryStage.show();
        
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
