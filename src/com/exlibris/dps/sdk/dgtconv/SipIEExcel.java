package com.exlibris.dps.sdk.dgtconv;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.biff.EmptyCell;
import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;

import excel.writer.MyExcel;

public class SipIEExcel {

	/**
	 * @param args
	 * 
	 */
	private static Logger logger = Logger.getLogger(SipIEExcel.class);
	
	private static Connection conn = null;
    private static final String url = "jdbc:oracle:thin:@aleph08.libis.kuleuven.be:1521:dtl3";

	private static final Class<? extends Cell> EmptyCell = null;

    public static String getPID(int sip) {
  	  
  	  Statement stmt = null;
  	  ResultSet rset = null;
  	  String EntityId = "";
  	  try{
  	    stmt = conn.createStatement();
  	    
  	    String query =  
  	    		"select pid from sipxpid where sip ="+sip+"";
  	    
  	    rset = stmt.executeQuery(query);
  	    while (rset.next()) {
  	    	EntityId = rset.getString(1);
  		    break;
  	    }
  	    rset.close();
  	    stmt.close();
  	    
  	  } catch (SQLException e){
  		  logger.debug(e.getMessage());
  		  try{
  			  conn.close();
  		  } catch (SQLException ee) {
  			  logger.debug(ee.getMessage());
  		  }
  	  }
      return EntityId;

    }
    
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		String pid;
		String iepid;
		int pidnr;
		int sipnr;
		String sip;
		MyExcel rosettaLink;
		int iexcel = 0;
		
		  try {
			    conn = DriverManager.getConnection(url,"D31_rep00","D31_rep00");
			    conn.setAutoCommit(false);
			  } catch (SQLException e){
				  logger.debug(e.getMessage());
				  return;
			  }		
		
			try {
				
				rosettaLink = new MyExcel("C:\\rosetta_log\\posters\\dgt_rosetta_posters_upd.xls");
				rosettaLink.addCaption(0, 0, "PID");
				rosettaLink.addCaption(1, 0, "SIP");			
				rosettaLink.addCaption(2, 0, "IE");
				
				
				
			    File inputWorkbook = new File("C:\\rosetta_log\\posters\\dgt_rosetta_posters.xls");
			    Workbook w;
			    try {
			      w = Workbook.getWorkbook(inputWorkbook);
			      // Get the first sheet
			      Sheet sheet = w.getSheet(0);
			      // Loop over first 10 column and lines

			      for (int i = 1; i < sheet.getRows(); i++) {
			    	  Cell cellPID = sheet.getCell(0, i);
			    	  Cell cellSIP = sheet.getCell(1, i);
				    	  try {
					    	  pidnr = Integer.valueOf(cellPID.getContents());
				    		  sipnr = Integer.valueOf(cellSIP.getContents());  
					    	  
					    	  iepid = getPID(sipnr);
					    	  rosettaLink.addNumber(0, i, pidnr);
					    	  rosettaLink.addNumber(1, i, sipnr);        		
					    	  rosettaLink.addLabel(2, i, iepid);        		
					    	  
				    	  } catch (Exception e) {
				    		  continue;
				    	  }
        				  
			       }
			       rosettaLink.SaveExcel();
			       rosettaLink.closeExcel();
			    } catch (BiffException e) {
			      e.printStackTrace();
			    }				
				
			} catch (Exception e) {
				
			}
			try{
				conn.close();
			} catch (SQLException e) {
				logger.debug(e.getMessage());
			}			
	}
}
