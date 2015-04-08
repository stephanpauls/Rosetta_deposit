package excel.writer;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

	/**
	 * A simple POI example of opening an Excel spreadsheet
	 * and writing its contents to the command line.
	 * @author  Tony Sintes, Stephan Pauls
	 */

public class MyExcel {

		private HSSFWorkbook myWorkBook; 
		private HSSFSheet mySheet;
		private FileOutputStream out;
		private String inputFile;

	    public  void SaveExcel() throws IOException{
	    	
	    	closeExcel();
	        try{
	        	POIFSFileSystem fs =  new POIFSFileSystem(new FileInputStream(inputFile));
	            myWorkBook = new HSSFWorkbook(fs);
	            mySheet = myWorkBook.getSheetAt(0);
	        }catch(Exception e){
	            e.printStackTrace();
	        }


	    }
	    public   MyExcel(String fileName) {
	        try {
	        	inputFile = fileName;
	            myWorkBook = new HSSFWorkbook();
	            mySheet = myWorkBook.createSheet();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public void closeExcel() throws IOException {
            out = new FileOutputStream(inputFile);
            myWorkBook.write(out);
            out.flush();
            out.close();
	    }

	    public void addCaption(int column, int row, String s) {
   	        HSSFRow myRow;
            HSSFCell myCell;
            myRow = mySheet.getRow(row);
            if (myRow == null ) myRow = mySheet.createRow(row);
            myCell = myRow.createCell(column);
            myCell.setCellValue(new HSSFRichTextString(s));
	  	}

	    public void addNumber(int column, int row, Integer integer) throws WriteException, RowsExceededException {
            HSSFRow myRow;
            HSSFCell myCell;
            myRow = mySheet.getRow(row);
            if (myRow == null ) myRow = mySheet.createRow(row);
            myCell = myRow.createCell(column);
            myCell.setCellValue(integer);
	  	}
	    
	    public void addLabel(int column, int row, String s) {
	        HSSFRow myRow;
	        HSSFCell myCell;
            myRow = mySheet.getRow(row);
            if (myRow == null ) myRow = mySheet.createRow(row);
            myCell = myRow.createCell(column);
	        myCell.setCellValue(s);
		  }
	}

