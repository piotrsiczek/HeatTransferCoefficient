/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coefficient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;


/**
 *
 * @author piotr
 */
public class DialogController 
{
    public enum Response { NO, YES, CANCEL };
    private static Response buttonSelected = Response.CANCEL;
    @FXML
    TableView table;
    private CoefficientController c;
    
    public void create(CoefficientController c)
    {
        this.c = c;
    }

    void create(DialogController controller) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @FXML
    public void cancelButtonOnClick(ActionEvent ev)
    {
        buttonSelected = Response.CANCEL;      
    }
    
    public Response getButtonSelected()
    {
        return buttonSelected;
    }
    
}
