/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coefficient;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

/**
 *
 * @author piotr
 */
public class DialogTest extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException 
    {  
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dialog.fxml"));
        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        Stage s = new Stage();
        s.setScene(scene);
        s.show();
         */
        
        //factorDialogTest();
        factorDialogTest();
        insulationDialogTest();
        
    }
    
    public void factorDialogTest()
    {
        double result = Dialog.showChooseDialog("asdf", new Stage(), "Dialog.fxml");
        System.out.println(result + "");
    }
    
    public void insulationDialogTest()
    {
        ObservableList<TableData> items = FXCollections.observableArrayList(
                new TableData("a", "8" , 2, 4),
                new TableData("b", "6" , 3, 2),
                new TableData("c", "12" , 4, 3));
        
        double result = Dialog.showInsulationDialog("asdf", new Stage(), "InsulationDialog.fxml", items);
        System.out.println(result + "");
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
