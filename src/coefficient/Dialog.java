/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coefficient;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 *
 * @author piotr
 */
public class Dialog 
{
    public enum Response { NO, YES, CANCEL };
    private static Response buttonSelected = Response.CANCEL;
    @FXML
    private static TableView table;
    @FXML
    private static ComboBox comboBox;
    private static double selectedValue = 0;
    private static double test = 0;
    private static Dial dialog;
    
    
    public static class Dial extends Stage 
    {
        public Dial(String title, Stage owner, String resource) 
        {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
                Parent root = (Parent)loader.load();
                setTitle(title);
                initStyle(StageStyle.UTILITY);
                initModality(Modality.APPLICATION_MODAL);
                initOwner(owner);
                setResizable(false);
                setScene(new Scene(root));
            } catch (IOException ex) {
                Logger.getLogger(Dialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public void showDialog() 
        {
            sizeToScene();
            centerOnScreen();
            showAndWait();
        }
    }  
    
    public static class TableModel
    {
        DoubleProperty up;
        DoubleProperty horizon;
        DoubleProperty down;

        public TableModel(double up, double horizon, double down) 
        {
            this.up = new SimpleDoubleProperty(up);
            this.horizon = new SimpleDoubleProperty(horizon);
            this.down = new SimpleDoubleProperty(down);
        }
        
        public double getUp() 
        {
            return up.getValue();
        }

        public void setUp(double up) 
        {
            this.up.setValue(up);
        }
        
        public double getHorizon() 
        {
            return horizon.getValue();
        }

        public void setHorizon(double horizon) 
        {
            this.horizon.setValue(horizon);
        }
        
        public double getDown() 
        {
            return down.getValue();
        }

        public void setDown(double down) 
        {
            this.down.setValue(down);
        }
        
    }
           
    public static double showChooseDialog(String title, Stage owner, String resource) 
    {       
        
            final ObservableList<TableModel> items = FXCollections.observableArrayList(
                    new TableModel(0.10, 0.13, 0.17));
            dialog = new Dial(title, owner, resource);
                       
            table.getColumns().addAll(createColumn("w górę", "up"), createColumn("poziomy", "horizon"), createColumn("w dół", "down"));    
            table.setItems(items);
            
            dialog.showDialog();
            
            return selectedValue;
    }
    
    public static double showInsulationDialog(String title, Stage owner, String resource, ObservableList<TableData> items) 
    {      
        /*
        ObservableList<TableData> data = FXCollections.observableArrayList();
        for (int i = 0; i < items.size(); i++)
        {
            data.add(items.get(i));
        }
        * */
        
            dialog = new Dial(title, owner, resource);
            comboBox.setItems(items);
            /*
            comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TableData>() {

                @Override
                public void changed(ObservableValue<? extends TableData> ov, TableData oldValue, TableData newValue) 
                {
                    //selectedValue = newValue.getR();
                    test = 88;
                    dialog.close();
                }
            });
            * */
            dialog.showDialog();
            return selectedValue;
    }
    
    private static TableColumn createColumn(String name, String title)
    {
        TableColumn c = new TableColumn(name);
        c.setPrefWidth(76);
        c.setMaxWidth(76);
        c.setMinWidth(76);
        c.setSortable(false);
        c.setCellValueFactory(new PropertyValueFactory<TableModel, Double>(title));
        
        c.setCellFactory(new Callback<TableColumn<TableModel, Double>, TableCell<TableModel, Double>>()
        {        
	    @Override
	    public TableCell<TableModel, Double> call(TableColumn<TableModel, Double> param) 
            {                
	        TableCell<TableModel, Double> cell = new TableCell<TableModel, Double>()
                {
	            @Override
	            public void updateItem(Double item, boolean empty) {
	                if(item!=null){
	 
	                   final Button b = new Button(item + "");
                           b.setOnAction(new EventHandler<ActionEvent>() 
                           {
                               @Override
                               public void handle(ActionEvent t) {
                                   selectedValue = Double.parseDouble(b.getText());
                                   dialog.close();
                               }
                           });
	                   setGraphic(b);
	                } 
	            }
	        };                           
	        return cell;
	    }   
	});
                
        return c;
    }
    
    
    @FXML
    public void cancelButtonOnClick(ActionEvent ev)
    {
        dialog.close();
    }
    
    @FXML
    public void okButtonOnClick(ActionEvent ev)
    {
        selectedValue = ((TableData)comboBox.getSelectionModel().getSelectedItem()).getR();
        dialog.close();
    }
}
