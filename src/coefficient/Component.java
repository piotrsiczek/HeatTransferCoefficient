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
public class Component 
{
    private StringProperty name;
    private DoubleProperty lambda;

    public Component(String name, double lambda) {
        this.name = new SimpleStringProperty(name);
        this.lambda = new SimpleDoubleProperty(lambda);
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
    
    public String toString()
    {
        return name.getValue();
    }

}
