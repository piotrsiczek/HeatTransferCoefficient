/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coefficient;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import jxl.write.WriteException;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author piotr
 */
public class CoefficientController
{
    private DataAccessObject data;
    private int tableCount = 0;
    private TableView currentTable = null;
    private TableView currentSumTable = null;
    private TextField lastText = null;
    private double lastR = -1;
    @FXML
    ListView<Component> componentList;
    @FXML
    TilePane contentPane;
    @FXML
    Pane addPane;
    @FXML
    TextField nameText;
    @FXML
    TextField lambdaText;
    @FXML
    Label nameErrorText;
    @FXML
    Label lambdaErrorText;
    @FXML
    Label errorLabel;
    @FXML
    ScrollBar scrollBar;
    @FXML
    AnchorPane anchorPane;
    @FXML
    AnchorPane mainPane;
    @FXML
    Button cancelAddComponentButton;
    @FXML
    Button showAddPaneButton;
    @FXML
    Button addComponentButton;
    @FXML
    Button editComponentButton;
    @FXML
    AnchorPane test;
    @FXML
    TextField componentTextField;

    public void initGUI()
    {
        data = new DataAccessObject();
        createList();
        //createScrollBar();
        //createTable(data.createTableData());
        
        scrollBar.setMin(0);
        scrollBar.setVisible(false);
        //scrollBar.setMin(0);
        contentPane.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() 
        {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds t, Bounds t1) {
                System.out.println(t1.getHeight() + " " + t1.getWidth());
                if (tableCount>1)
                    scrollBar.setVisible(true);
               
                scrollBar.setMax(t1.getHeight()-contentPane.getPrefHeight()+15);
                if (t1.getWidth()>848)
                    scrollBar.setMax(scrollBar.getMax()/2);
            }
        });

        anchorPane.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() 
        {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds t, Bounds t1) 
            {
                if (t.getHeight() != t1.getHeight())
                {
                    scrollBar.setMax(scrollBar.getMax()+t.getHeight()-t1.getHeight());
                }
            }
        });
        
        scrollBar.setMax(contentPane.getMaxHeight());
        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                System.out.println(new_val.doubleValue());
                    contentPane.setLayoutY(-new_val.doubleValue());
            }
        });
        
        componentTextField.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent t) 
            {
                KeyCombination key = new KeyCodeCombination(KeyCode.ENTER);
                if (key.match(t))
                {
                    addLayerButtonOnClick();
                    componentTextField.getParent().requestFocus();
                }
            }
        });

        nameText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ev) {
                KeyCombination key = new KeyCodeCombination(KeyCode.ENTER);
                if (key.match(ev))
                {
                    addLayerButtonOnClick();
                    componentTextField.getParent().requestFocus();
                }
            }
        });

        lambdaText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ev) {
                KeyCombination key = new KeyCodeCombination(KeyCode.ENTER);
                if (key.match(ev))
                {
                    addLayerButtonOnClick();
                    componentTextField.getParent().requestFocus();
                }
            }
        });
        
        /*
        mainPane.setOnMouseClicked(new EventHandler<MouseEvent>() 
        {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 1 && t.getButton() == MouseButton.PRIMARY)
                {
                    if (lastText != null)
                    {
                        lastText.setVisible(false);
                        lastText.selectEnd();
                        //lastText.clear();
                    }
                }
            }
        });
        */

    }
          
    
    public void createScrollBar()
    {
        scrollBar.setLayoutX(800-scrollBar.getWidth());
        scrollBar.setMin(0);
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setPrefHeight(547);
        scrollBar.setMax(360);
        
        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    contentPane.setLayoutY(-new_val.doubleValue());
            }
        });
    }
    
    public void createList()
    {
        //####################### Menu items #######################
        MenuItem removeItem = new MenuItem("Usun");
        removeItem.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent t) {
                data.removeComponent(componentList.getSelectionModel().getSelectedIndex());
            }
        });
        MenuItem editItem = new MenuItem("Edytuj");
        editItem.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent t) {
                Component c = componentList.getSelectionModel().getSelectedItem();
                nameText.setText(c.getName());
                lambdaText.setText(c.getLambda()+"");
                editComponentButton.setVisible(true);
                addComponentButton.setVisible(false);
            }
        });
        ContextMenu contextMenu = new ContextMenu(editItem, removeItem);
        //####################### List data #######################
        ObservableList<Component> items = data.getComponents();
        FXCollections.sort(items, new Comparator<Component>() {
            @Override
            public int compare(Component c1, Component c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
        componentList.setItems(items);
        //####################### List config #######################
        componentList.setContextMenu(contextMenu);
        componentList.setCellFactory(new Callback<ListView<Component>, ListCell<Component>>() 
        {
            @Override
            public ListCell<Component> call(ListView<Component> p) 
            {
                return new ListCell<Component>()
                {
                    @Override
                    protected void updateItem(Component item, boolean bln) 
                    {
                        super.updateItem(item, bln); 
                        if (item!=null)
                        {
                            HBox box = new HBox();
                            Label left = new Label(item.getName());
                            left.setPrefWidth(170);
                            final Tooltip tooltip = new Tooltip(item.getName());
                            left.setTooltip(tooltip);
                            String lambda = item.getLambda()+"";
                            for (int i =lambda.length(); i < 5; i++)
                                lambda += "0";
                            Label right = new Label(lambda);
                            right.setPrefWidth(30);
                            right.setAlignment(Pos.CENTER_RIGHT);
                            box.getChildren().addAll(left, right);
                            
                            setGraphic(box);
                        }
                    }
                };
            }
        });

        componentList.setOnEditCommit(new EventHandler<ListView.EditEvent<Component>>() {
            @Override
            public void handle(ListView.EditEvent<Component> t) {

            }
        });

        componentList.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ev) {
                KeyCombination key = new KeyCodeCombination(KeyCode.DELETE);
                if (key.match(ev))
                    data.removeComponent(componentList.getSelectionModel().getSelectedIndex());
            }
        });
    }
    
    public void createTable(final int index)
    {
        TableColumn nameColumn = new TableColumn("WARSTWY KOMPONENTU");
        nameColumn.setMinWidth(265);
        nameColumn.setPrefWidth(265);
        nameColumn.setSortable(false);
        nameColumn.setCellValueFactory(new PropertyValueFactory<TableData, String>("name"));
        
        TableColumn dColumn = new TableColumn("d [m]");
        dColumn.setSortable(false);
        dColumn.setMinWidth(45);
        dColumn.setPrefWidth(45);
        dColumn.setCellValueFactory(new PropertyValueFactory<TableData, String>("d"));
        dColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableData, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableData, String> t) 
            {
                 int index = t.getTablePosition().getRow();
                 TableData row = t.getTableView().getItems().get(index);
                 row.setD(t.getNewValue());
                 double oldR = row.getR();

                 row.setR(Double.parseDouble(t.getNewValue()) / row.getLambda());
                 t.getTableView().getColumns().get(3).setVisible(false);
                 t.getTableView().getColumns().get(3).setVisible(true);
                 TableSumData sum = (TableSumData)currentSumTable.getItems().get(0); 
                 double oldFirstSum = sum.getR();
                 sum.setR(oldFirstSum - oldR + row.getR());

             }
        });
        //\n[W/m*K]
        TableColumn lambdaColumn = new TableColumn("λ [*]");
        lambdaColumn.setMinWidth(45);
        lambdaColumn.setPrefWidth(45);
        lambdaColumn.setSortable(false);
        lambdaColumn.setCellValueFactory(new PropertyValueFactory<TableData, Double>("lambda"));
        //\n[m2*K/W]
        TableColumn rColumn = new TableColumn("R [^]");
        rColumn.setMinWidth(45);
        rColumn.setPrefWidth(45);
        rColumn.setSortable(false);
        rColumn.setCellValueFactory(new PropertyValueFactory<TableData, Double>("r"));
        
        TableView table = new TableView();
        final TableView sumTable = createSumTable(index);
        table.setId(index+"");
        table.setMinWidth(401.1);
        table.setPrefWidth(401.1);
        table.setPrefHeight(200);
        table.setEditable(true);
        table.getColumns().addAll(nameColumn, dColumn, lambdaColumn, rColumn);
        table.setOnMouseClicked(new EventHandler<MouseEvent>() 
        {
            @Override
            public void handle(MouseEvent t) 
            {
                TableView test = (TableView)t.getSource();
                if (currentTable != test)
                {
                    //test.setStyle("-fx-border-color: #e22b2b; -fx-border-width: 2px;");
                    test.setStyle(  "    -fx-background-color: -fx-focus-color,-fx-box-border,-fx-control-inner-background;\n" +
                                    "    -fx-background-insets: -1.4, 0, 1;\n" +
                                    "    -fx-background-radius: 1.4, 0, 0;\n" +
                                    "\n" +
                                    "    /* There is some oddness if padding is in em values rather than pixels,\n" +
                                    "       in particular, the left border of the control doesn't show. */\n" +
                                    "    -fx-padding: 1; /* 0.083333em; */");
                    if (currentTable != null)
                        currentTable.setStyle("-fx-border-width: 0px;");
                    currentTable = test;
                    currentSumTable = sumTable;
                }
            }
        });
        
        table.setStyle( "    -fx-background-color: -fx-focus-color,-fx-box-border,-fx-control-inner-background;\n" +
                        "    -fx-background-insets: -1.4, 0, 1;\n" +
                        "    -fx-background-radius: 1.4, 0, 0;\n" +
                        "\n" +
                        "    /* There is some oddness if padding is in em values rather than pixels,\n" +
                        "       in particular, the left border of the control doesn't show. */\n" +
                        "    -fx-padding: 1; /* 0.083333em; */");
                    if (currentTable != null)
                        currentTable.setStyle("-fx-border-width: 0px;");
                    currentTable = table;
                    currentSumTable = sumTable;
                    
        table.setOnKeyReleased(new EventHandler<KeyEvent>() 
        {
            @Override
            public void handle(KeyEvent kv) 
            {
                final KeyCombination combo = new KeyCodeCombination(KeyCode.DELETE);
                if (combo.match(kv)) 
                {
                    TableView t = (TableView)kv.getSource();
                    int index = t.getSelectionModel().getSelectedIndex();
                    if (index != -1)
                    {
                        double r = ((TableData)t.getItems().get(index)).getR();
                        if (r != 0)
                        {
                            TableSumData sum = (TableSumData)sumTable.getItems().get(0);
                            sum.setR(sum.getR()-r);
                        }
                        t.getItems().remove(index);
                    }
                }
            }
        });

        table.setItems(data.getTableData(index)); 
        
        VBox p = new VBox();
        p.setPadding(new Insets(15, 0, 0, 15));
        
        HBox h = new HBox();
        final TextField labelEdit = new TextField();
        labelEdit.setVisible(false);
        labelEdit.setPrefWidth(0);
        
        final Label label = new Label("ASDFFFDASF ASDFASDF");
        label.textProperty().bind(data.getLabel(index));
        labelEdit.setOnKeyReleased(new EventHandler<KeyEvent>() 
        {
            @Override
            public void handle(KeyEvent t) 
            {
                KeyCodeCombination key = new KeyCodeCombination(KeyCode.ENTER);
                KeyCodeCombination key1 = new KeyCodeCombination(KeyCode.ESCAPE);
                if (t.getCode() == key.getCode())
                {
                    data.setLabel(index, labelEdit.getText());
                    labelEdit.setPrefWidth(0);
                    labelEdit.setVisible(false);
                    labelEdit.clear();
                    label.setVisible(true);
                }
                else if (t.getCode() == key1.getCode())
                {
                    labelEdit.setPrefWidth(0);
                    labelEdit.setVisible(false);
                    labelEdit.clear();
                    label.setVisible(true);
                }
            }
        });
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2 && t.getButton() == MouseButton.PRIMARY)
                {
                    lastText = labelEdit;
                    label.setVisible(false);
                    labelEdit.setPromptText(label.getText());
                    labelEdit.setPrefWidth(200);
                    labelEdit.setVisible(true);
                    
                }
            }
        });

        label.setFont(new Font("System", 16));
        h.getChildren().addAll(labelEdit, label);
        p.getChildren().addAll(h, table, sumTable);
         
        contentPane.getChildren().add(p);
    }
    
    public TableView createSumTable(final int index)
    {
        TableColumn nameColumn = new TableColumn("[*]=[W/m*K], [^]=[m2*K/W]");
        nameColumn.setMinWidth(355);
        nameColumn.setPrefWidth(355);
        nameColumn.setSortable(false);
        nameColumn.setCellValueFactory(new PropertyValueFactory<TableData, String>("name"));

        TableColumn rColumn = new TableColumn();
        
        rColumn.setOnEditStart(new EventHandler() 
        {
            @Override
            public void handle(Event t) 
            {
                TableColumn c = (TableColumn)t.getSource();
                int index = c.getTableView().getSelectionModel().getSelectedIndex();
                if (index == 1 || index == 5)
                {
                    ObservableList<TableSumData> data = c.getTableView().getItems();
                    if (index == 1)
                    {
                        data.get(1).setR(Dialog.showChooseDialog("asdf", new Stage(), "Dialog.fxml"));
                    }
                    else 
                    {
                        double warming = Dialog.showInsulationDialog("asdf", new Stage(), "InsulationDialog.fxml", currentTable.getItems());
                        lastR = warming;
                        double result = 0.01*(warming/data.get(3).getR())*(warming/data.get(3).getR());
                        data.get(5).setR(result);
                    }
                }
            }
        });
        rColumn.setMinWidth(45);
        rColumn.setPrefWidth(45);
        rColumn.setSortable(false);
        rColumn.setCellValueFactory(new PropertyValueFactory<TableData, Double>("r"));
        TableView table = new TableView();
        table.setId("sum"+index);
        table.setMinWidth(401.1);
        table.setPrefWidth(401.1);
        table.setPrefHeight(200);
        table.setEditable(true);
        table.getColumns().addAll(nameColumn, rColumn);
        final ObservableList<TableSumData> d = data.getSumTableData(index);
        d.get(0).rProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number oldVal, Number newVal) 
            {
                double column3 = d.get(3).getR()-oldVal.doubleValue()+newVal.doubleValue();
                d.get(3).setR(column3);
                double column4 = 1/column3;
                d.get(4).setR(column4);
                double warming = Math.sqrt(d.get(5).getR()*100)*column3;
                double column5 = 0.01*(warming/column3)*(warming/column3);
                d.get(5).setR(column5);
                d.get(6).setR(column4+column5);
            }
        });
        
        d.get(1).rProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number oldVal, Number newVal) 
            {
                double column3 = d.get(3).getR()-oldVal.doubleValue()+newVal.doubleValue();
                d.get(3).setR(column3);
                double column4 = 1/column3;
                d.get(4).setR(column4);
                double column5 = 0.01*(d.get(5).getR()/column3)*(d.get(5).getR()/column3);
                d.get(5).setR(column5);
                d.get(6).setR(column4+column5);
            }
        });
        
        d.get(5).rProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue ov, Number oldVal, Number newVal) 
            {
                d.get(6).setR(d.get(4).getR()+newVal.doubleValue());
            }
        });
        
        table.setItems(d); 
        table.getStyleClass().remove("table-row-cell:filled:focused:selected");
        
        table.setRowFactory(new Callback<TableView<TableSumData>, TableRow<TableSumData>>() 
        {
            @Override
            public TableRow<TableSumData> call(TableView<TableSumData> p) 
            {
                return new TableRow<TableSumData>()
                {
                    @Override
                    public void updateItem(TableSumData item, boolean empty) 
                    {
                        super.updateItem(item, empty);
                        if (item != null)
                        {
                            int index = this.getIndex();
                            if (index == 0)
                                this.getStyleClass().add("row0");
                            else if (index == 3)
                                this.getStyleClass().add("row3");
                            else if (index == 4)
                                this.getStyleClass().add("row4");
                            else if (index == 6)
                                this.getStyleClass().add("row6");                            
                        }
                    }
                };
            }
        });
        return table;
    }
    
    @FXML
    public void componentListOnClick(MouseEvent ev)
    {
        if(ev.getButton().equals(MouseButton.PRIMARY))
        {
            if(ev.getClickCount() == 1)
            {
                Component c = componentList.getSelectionModel().getSelectedItem();
                
                if (currentTable != null)
                {
                    //ObservableList items = currentTable.getItems();
                    //items.add(new TableData(c.getName(), "0", c.getLambda(), 0));
                    currentTable.getItems().add(new TableData(c.getName(), "0", c.getLambda(), 0));
                    errorLabel.setText("");

                }
                else
                    errorLabel.setText("Zaznacz aktywny komponent budowlany.");
            }
        }
    }
    
    @FXML
    public void addLayerButtonOnClick()
    {
        boolean nameState = false;
        boolean lambdaState = false;
        String name = nameText.getText();
        String lambda = lambdaText.getText();
        nameErrorText.setText("");
        lambdaErrorText.setText("");
        
        if ((name != null && !name.isEmpty())) 
            nameState = true;
        else
            nameErrorText.setText("Podaj nazwe komponentu.");
        
        if ((lambda != null && !lambda.isEmpty())) 
            lambdaState = true;
        else 
            lambdaErrorText.setText("Podaj wspolczynnik lambda.");

        if (nameState && lambdaState)
        {
            data.addComponent(name, Double.parseDouble(lambda));
            nameText.clear();
            lambdaText.clear();
        } 
    }
    
    @FXML
    public void addComponentButtonOnClick()
    {
            tableCount++;
            //if (tableCount > 2)
                //scrollBar.setMax(tableCount*82.5);
            createTable(data.createTableData(componentTextField.getText()));
            componentTextField.clear();
    }
    
    @FXML
    public void addComponentFieldEntered(KeyEvent kv)
    {
        final KeyCombination combo = new KeyCodeCombination(KeyCode.ENTER);
        if (combo.match(kv)) 
            addComponentButtonOnClick();
    }
    
    @FXML
    public void showAddPaneButtonOnClick()
    {
        //addPane.setVisible(true);
        /*
        Path path = new Path();
        path.getElements().add(new MoveTo(0, 0));
        path.getElements().add(new LineTo(0, 100));
        //path.getElements().add(new MoveTo(20, 100));
        //path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120));
        //path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(addPane);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
        * */
        showAddPaneButton.setVisible(false);
        cancelAddComponentButton.setVisible(true);
        addComponentButton.setVisible(true);
        addPane.setPrefHeight(98);
        addPane.setVisible(true);
        componentList.layoutYProperty().setValue(170);
        test.getChildren().remove(componentList);
        test.getChildren().add(componentList);
        //componentList.setMaxHeight(100);
        /*
        FadeTransition ft = new FadeTransition(Duration.millis(1000), cancelAddComponentButton);
        ft.setFromValue(0);
        ft.setToValue(1);
        //ft.setCycleCount(Timeline.);
        ft.setAutoReverse(true);
        ft.play();
        * */
    }
    
    @FXML
    public void cancelAddComponentButtonOnClick()
    {
        showAddPaneButton.setVisible(true);
        cancelAddComponentButton.setVisible(false);
        addComponentButton.setVisible(false);
        addPane.setVisible(false);
        componentList.layoutYProperty().setValue(49);
        /*
        
        Path path = new Path();
        path.getElements().add(new MoveTo(0, 0));
        path.getElements().add(new LineTo(0, 100));
        //path.getElements().add(new MoveTo(20, 100));
        //path.getElements().add(new CubicCurveTo(380, 0, 380, 120, 200, 120));
        //path.getElements().add(new CubicCurveTo(0, 120, 0, 240, 380, 240));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(addPane);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();

        FadeTransition ft = new FadeTransition(Duration.millis(1500), addPane);
        ft.setFromValue(0);
        ft.setToValue(1);
        //ft.setCycleCount(Timeline.);
        ft.setAutoReverse(true);
        ft.play();
        * 
        * */
    }
    
    @FXML
    public void saveButtonOnClick()
    {
        try
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Microsoft Excel", "*.xls"),
                                                     new ExtensionFilter("Microsoft Excel", "*.xlsx"));
            //Show save file dialog
            File file = fileChooser.showSaveDialog(new Stage());

            if (!file.getName().equals(""))
            {
                Spreadsheet s = new Spreadsheet(file);
                s.write(data);
            }

        } catch (IOException ex) {
            Logger.getLogger(CoefficientController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException ex) {
            Logger.getLogger(CoefficientController.class.getName()).log(Level.SEVERE, null, ex);
        }             
    }

    @FXML
    public void editLayerButtonOnClick()
    {
        int index = componentList.getSelectionModel().getSelectedIndex();
        if (index != -1)
        {
            data.editComponent(nameText.getText(), Double.parseDouble(lambdaText.getText()), index);
            editComponentButton.setVisible(false);
            addComponentButton.setVisible(true);
            nameText.clear();
            lambdaText.clear();
        }
    }
}
