/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coefficient;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 *
 * @author piotr
 */
public class Spreadsheet 
{
    private String fileName;
    private File file;
    private DataAccessObject data;
    private WritableSheet sheet;
    private int row = 0;
   
    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat calibri;
    private WritableCellFormat backgroundRow0;
    private WritableCellFormat backgroundRow3;
    private WritableCellFormat backgroundRow4;
    private WritableCellFormat backgroundRow6;
    //private WritableCellFormat times;

    public Spreadsheet(File file) 
    {
        //this.fileName = fileName;
        this.file = file;
    }
    
    public void write(DataAccessObject data) throws IOException, WriteException 
    {
        this.data = data;
        //File file = new File(fileName);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("pl", "PL"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Współczynniki", 0);
        sheet = workbook.getSheet(0);
        
        //sheet.addCell(new Label(8, 8, "elo elo 5 2 0"));
        
        
        createStyles();
        createContent();

        workbook.write();
        workbook.close();
    }
    
    private void createStyles()
    {
        try {
            sheet.setColumnView(0, 325);
            sheet.setColumnView(1, 15);
            sheet.setColumnView(2, 15);
            sheet.setColumnView(3, 15);
            
            WritableFont font = new WritableFont(WritableFont.createFont("Calibri"), 11);
            calibri = new WritableCellFormat(font);
            backgroundRow0 = new WritableCellFormat(font);
            backgroundRow0.setWrap(true);
            backgroundRow0.setBackground(Colour.SEA_GREEN);
            backgroundRow3 = new WritableCellFormat(font);
            backgroundRow3.setBackground(Colour.GREEN);
            backgroundRow4 = new WritableCellFormat(font);
            backgroundRow4.setBackground(Colour.AQUA);
            backgroundRow6 = new WritableCellFormat(font);
            backgroundRow6.setBackground(Colour.ORANGE);
            
            calibri.setWrap(true);
            calibri.setShrinkToFit(false);
            
            // Create create a bold font with unterlines
            WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 11, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
            timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
            // Lets automatically wrap the cells
            timesBoldUnderline.setWrap(true);

            CellView cv = new CellView();
            cv.setFormat(calibri);
            cv.setFormat(backgroundRow0);
            cv.setFormat(backgroundRow3);
            cv.setFormat(backgroundRow4);
            cv.setFormat(backgroundRow6);
            cv.setFormat(timesBoldUnderline);
            cv.setAutosize(true);

        } catch (WriteException ex) {
            Logger.getLogger(Spreadsheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createContent()
    {
        for (int i = 0; i < data.getSize(); i++)
        {
            createComponentTable(data.getTableData(i), data.getLabel(i).getValue());
            createSumTable(data.getSumTableData(i));
        }



    }
    
    private void createComponentTable(ObservableList<TableData> items, String label)
    {
        addHeader(sheet, 0, row, label);
        row++;
        addHeader(sheet, 0, row, "WARSTWY KOMPONENTU");
        addHeader(sheet, 1, row, "d [m]");
        addHeader(sheet, 2, row, "λ [W/m*K]");
        addHeader(sheet, 3, row, "R [m2*K/W]");
        row++;
        for (int i = 0; i < items.size(); i++, row++)
        {
            addLabel(sheet, 0, row, items.get(i).getName());
            addLabel(sheet, 1, row, items.get(i).getD());
            addLabel(sheet, 2, row, items.get(i).getLambda()+"");
            addLabel(sheet, 3, row, items.get(i).getR()+"");
        }
        row++;
        
    }
    
    private void createSumTable(ObservableList<TableSumData> items)
    {
        CellFormat style = null;
        
        for (int i = 0; i < items.size(); i++, row++)
        {
            if (i == 0)
                style = backgroundRow0;
            else if (i == 3)
                style = backgroundRow3;
            else if (i == 4)
                style = backgroundRow4;
            else if (i == 6)
                style = backgroundRow6;
            else
                style = calibri;
                
            addLabel(sheet, 0, row, items.get(i).getName(), style);
            addLabel(sheet, 1, row, "", style);
            addLabel(sheet, 2, row, "", style);
            addLabel(sheet, 3, row, items.get(i).getR()+"", style);
        }
        row+=2;
    }
    
    private void addHeader(WritableSheet sheet, int column, int row, String s)
    {
        try {
            Label label;
            label = new Label(column, row, s, calibri);
            
            sheet.addCell(label);
        } catch (WriteException ex) {
            Logger.getLogger(Spreadsheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  private void addCaption(WritableSheet sheet, int column, int row, String s)
      throws RowsExceededException, WriteException {
    Label label;
    label = new Label(column, row, s, timesBoldUnderline);
    sheet.addCell(label);
  }

  private void addNumber(WritableSheet sheet, int column, int row,
      Integer integer) throws WriteException, RowsExceededException {
    Number number;
    number = new Number(column, row, integer, calibri);
    sheet.addCell(number);
  }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
    {
        try {
            Label label;
            label = new Label(column, row, s, calibri);
            sheet.addCell(label);
        } catch (WriteException ex) {
            Logger.getLogger(Spreadsheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addLabel(WritableSheet sheet, int column, int row, String s, CellFormat style)
    {
        try {
            Label label;
            label = new Label(column, row, s, style);
            sheet.addCell(label);
        } catch (WriteException ex) {
            Logger.getLogger(Spreadsheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

}
