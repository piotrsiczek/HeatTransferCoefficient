/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coefficient;

import java.text.DecimalFormat;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author piotr
 */
public class TableSumData 
{
    private StringProperty name;
    private DoubleProperty r;

    public TableSumData(String name, double r) {
        this.name = new SimpleStringProperty(name);
        this.r = new SimpleDoubleProperty(r);
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }
    
    public double getR() {
        return r.getValue();
    }
    
    public DoubleProperty rProperty()
    {
        return r;
    }

    public void setR(double r) {
        this.r.setValue(Math.round(r*1000)/1000.0);
    }
    
    public String toString()
    {
        return name.getValue();
    }
}
