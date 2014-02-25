/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coefficient;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author piotr
 */
public class TableData 
{
    private StringProperty name;
    private StringProperty d;
    private DoubleProperty lambda;
    private DoubleProperty r;

    public TableData(String name, String d, double lambda, double r) {
        this.name = new SimpleStringProperty(name);
        this.d = new SimpleStringProperty(d);
        this.lambda = new SimpleDoubleProperty(lambda);
        this.r = new SimpleDoubleProperty(r);
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public double getLambda() {
        return lambda.getValue();
    }

    public void setLambda(double lambda) {
        this.lambda.setValue(lambda);
    }
    
    public String getD() {
        return d.getValue();
    }

    public void setD(String d) {
        this.d.setValue(d);
    }
    
    public double getR() {
        return r.getValue();
    }

    public void setR(double r) {
        this.r.setValue(Math.round(r*1000)/1000.0);
    }
    
    public String toString()
    {
        return name.getValue();
    }
}
