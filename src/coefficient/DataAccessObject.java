/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coefficient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author piotr
 */
public class DataAccessObject
{
    private final String dataFile = "data.txt";
    private final ObservableList<Component> components = FXCollections.observableArrayList();
    private ObservableList<StringProperty> captions = FXCollections.observableArrayList();
    private List<ObservableList<TableData>> tableData = new ArrayList<ObservableList<TableData>>();
    private List<ObservableList<TableSumData>> sumData = new ArrayList<ObservableList<TableSumData>>();

    public DataAccessObject() {
        loadData(dataFile);
    }

    private void loadData(String file)
    {
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));

            while (in.ready()) {
                String line = in.readLine();
                String parts[] = line.split(";");
                components.add(new Component(parts[0], Double.parseDouble(parts[1])));
        }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Component> getComponents()
    {
        return components;
    }
    
    public ObservableList<TableData> getTableData(int tableIndex)
    {
        return tableData.get(tableIndex);
    }
    
    public int getSize()
    {
        return tableData.size();
    }
    
    public ObservableList<TableSumData> getSumTableData(int tableIndex)
    {
        return sumData.get(tableIndex);
    }
   
    public StringProperty getLabel(int tableIndex)
    {
        return captions.get(tableIndex);
    }
    
    public void setLabel(int index, String text)
    {
        captions.get(index).setValue(text);
    }
    
    public int createTableData(String label)
    {
        ObservableList<TableData> item = FXCollections.observableArrayList();
        ObservableList<TableSumData> sumItem = FXCollections.observableArrayList(
                new TableSumData("OPÓR CIEPLNY Rn=∑(d/λ) [m2*K/W]", 0),
                new TableSumData("opór przejmowania ciepła na wewnętrznej pow. Rsi [m2*K/W]", 0),
                new TableSumData("opór przejmowania ciepła na zewnetrznej pow. Rse [m2*K/W]", 0.04),
                new TableSumData("OPÓR CIEPLNY PRZEGRODY Rt=Tn+Rsi+Rse [m2*K/W]", 0.04),
                new TableSumData("WSPÓŁCZYNNIK PRZENIKANIA CIEPŁA U=1/Rt [W/m2*K]", 0),
                new TableSumData("poprawka na nieszczelności ΔU=0.01*(Rstyr/Rt)^2", 0),
                new TableSumData("SKORYGOWANY WSP. PRZENIKANIA CIEPŁA Uc=U+ΔU [W/m2*K]", 0));
        tableData.add(item);
        sumData.add(sumItem);
        captions.add(new SimpleStringProperty(label));
        
        return tableData.size()-1;
    }
    
    public void addTableData(Component c, int tableIndex)
    {
        //ObservableList<TableData> item = FXCollections.observableArrayList(
          //      new TableData(c.getName(), "", c.getLambda(), 0));
        ObservableList<TableData> item = tableData.get(tableIndex);
        item.add(new TableData(c.getName(), "", c.getLambda(), 0));           
    }
    
    public void addComponent(String name, double lambda)
    {
        try {
            Component c = new Component(name, lambda);
            components.add(c);

            FileWriter fw = new FileWriter(dataFile, true);//true appends text
            fw.write(name + ";" + lambda + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editComponent(String name, double lambda, int index)
    {
        removeComponent(index);
        addComponent(name, lambda);
    }
    
    public void removeComponent(int index)
    {
        Component c = components.get(index);
        final String compareString = c.getName() + ";" + c.getLambda();
        File input = new File(dataFile);
        File temp = new File("temp.txt");
        BufferedReader in = null;
        FileWriter fw = null;

        components.remove(index);
        try {
            in = new BufferedReader(new FileReader(dataFile));
            fw = new FileWriter(temp, true);//true appends text
            while (in.ready()) {
                String line = in.readLine();
                if (!line.equals(compareString))
                {
                    fw.write(line+"\n");
                }
            }
                in.close();
                fw.close();

            if (!input.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            if (!temp.renameTo(input))
                System.out.println("Could not rename file");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fw != null)
                    fw.close();
                if (in != null)
                    in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //################################ Test Methods ################################
    public static void main(String[] args) {
        System.out.println("################################ DAO test ################################");

        DataAccessObject d = new DataAccessObject();
        //d.displayComponents();
        /*
        d.addComponent("test", 1);
        d.addComponent("test", 2);
        d.addComponent("test", 3);
        d.addComponent("test", 4);
        d.addComponent("test", 5);
        d.addComponent("test", 6);

        */
        d.removeComponent(2);
        d.displayComponents();
    }

    private void displayComponents()
    {
        for(Component c : components)
        {
            System.out.println(c.getName() + " " + c.getLambda());
        }
    }
}