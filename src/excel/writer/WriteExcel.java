package excel.writer;


import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.read.biff.SheetImpl;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


public class WriteExcel {

  private WritableCellFormat timesBoldUnderline;
  private WritableCellFormat times;
  private String inputFile;
  private WritableWorkbook workbook;
  private WritableSheet sheet;
  
  public WriteExcel(String inputFile) {
	  	this.inputFile = inputFile;	  
	    File file = new File(inputFile);
	    WorkbookSettings wbSettings = new WorkbookSettings();
	    wbSettings.setLocale(new Locale("en", "EN"));
	    try {
	    	workbook = Workbook.createWorkbook(file, wbSettings);
	    	workbook.createSheet("Link", 0);
	    	sheet = workbook.getSheet(0);
	    } catch (IOException e) {
	    	
	    }
	    try {
	    // Lets create a times font
	    WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
	    // Define the cell format
	    times = new WritableCellFormat(times10pt);
	    // Lets automatically wrap the cells
	    times.setWrap(true);

	    // Create create a bold font with unterlines
	    WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
	        UnderlineStyle.SINGLE);
	    timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
	    // Lets automatically wrap the cells
	    timesBoldUnderline.setWrap(true);

	    CellView cv = new CellView();
	    cv.setFormat(times);
	    cv.setFormat(timesBoldUnderline);
	    cv.setAutosize(true);	    
	    } catch (WriteException e) {
	    	
	    }
  }
  
  
  
  public void closeExcel() {
	    try {
	    	workbook.write();
	    } catch (IOException e) {
	    	
	    }
	    try {
	    	workbook.close();
	    } catch (Exception e) {
	    	
	    }
}  
  public void readExcel() throws IOException,BiffException {

	  
	Workbook w;
	File file = new File(inputFile);
    w = Workbook.getWorkbook(file);
    // Get the first sheet
    Sheet sht = w.getSheet(0);
    
    
    for (int j = 0; j < sht.getColumns(); j++) {
        for (int i = 0; i < sht.getRows(); i++) {
          Cell cell = sht.getCell(j, i);
          CellType type = cell.getType();
          if (type == CellType.LABEL) {
            System.out.println("I got a label "
                + cell.getContents());
          }

          if (type == CellType.NUMBER) {
            System.out.println("I got a number "
                + cell.getContents());
          }

        }
      }
    
    
  } 

  public void setOutputFile(String inputFile) {
  this.inputFile = inputFile;
  }

  public void write() throws IOException, WriteException {
    File file = new File(inputFile);
    WorkbookSettings wbSettings = new WorkbookSettings();

    wbSettings.setLocale(new Locale("en", "EN"));

    WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
    workbook.createSheet("Report", 0);
    WritableSheet excelSheet = workbook.getSheet(0);
    createLabel(excelSheet);
    createContent(excelSheet);

    workbook.write();
    workbook.close();
  }

  private void createLabel(WritableSheet sheet)
      throws WriteException {
    // Lets create a times font
    WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
    // Define the cell format
    times = new WritableCellFormat(times10pt);
    // Lets automatically wrap the cells
    times.setWrap(true);

    // Create create a bold font with unterlines
    WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
        UnderlineStyle.SINGLE);
    timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
    // Lets automatically wrap the cells
    timesBoldUnderline.setWrap(true);

    CellView cv = new CellView();
    cv.setFormat(times);
    cv.setFormat(timesBoldUnderline);
    cv.setAutosize(true);

    // Write a few headers
    addCaption(sheet, 0, 0, "Header 1");
    addCaption(sheet, 1, 0, "This is another header");
    

  }

  private void createContent(WritableSheet sheet) throws WriteException,
      RowsExceededException {
    // Write a few number
    for (int i = 1; i < 10; i++) {
      // First column
      addNumber(sheet, 0, i, i + 10);
      // Second column
      addNumber(sheet, 1, i, i * i);
    }
    // Lets calculate the sum of it
    StringBuffer buf = new StringBuffer();
    buf.append("SUM(A2:A10)");
    Formula f = new Formula(0, 10, buf.toString());
    sheet.addCell(f);
    buf = new StringBuffer();
    buf.append("SUM(B2:B10)");
    f = new Formula(1, 10, buf.toString());
    sheet.addCell(f);

    // Now a bit of text
    for (int i = 12; i < 20; i++) {
      // First column
      addLabel(sheet, 0, i, "Boring text " + i);
      // Second column
      addLabel(sheet, 1, i, "Another text");
    }
  }

  private void addCaption(WritableSheet sheet, int column, int row, String s)
	      throws RowsExceededException, WriteException {
	    Label label;
	    label = new Label(column, row, s, timesBoldUnderline);
	    sheet.addCell(label);
	  }
  
  public void addCaption(int column, int row, String s)
	      throws RowsExceededException, WriteException {
	    Label label;
	    label = new Label(column, row, s, timesBoldUnderline);
	    sheet.addCell(label);
	  }

  private void addNumber(WritableSheet sheet, int column, int row,
	      Integer integer) throws WriteException, RowsExceededException {
	    Number number;
	    number = new Number(column, row, integer, times);
	    sheet.addCell(number);
	  }

  public void addNumber(int column, int row,
	      Integer integer) throws WriteException, RowsExceededException {
	    Number number;
	    number = new Number(column, row, integer, times);
	    sheet.addCell(number);
	  }

  private void addLabel(WritableSheet sheet, int column, int row, String s)
	      throws WriteException, RowsExceededException {
	    Label label;
	    label = new Label(column, row, s, times);
	    sheet.addCell(label);
	  }
  
  public void addLabel(int column, int row, String s)
		      throws WriteException, RowsExceededException {
		    Label label;
		    label = new Label(column, row, s, times);
		    sheet.addCell(label);
	  }
/*
  public static void main(String[] args) throws WriteException, IOException {
    WriteExcel test = new WriteExcel();
    test.setOutputFile("c:/temp/lars.xls");
    test.write();
    System.out
        .println("Please check the result file under c:/temp/lars.xls ");
  }
*/  
} 