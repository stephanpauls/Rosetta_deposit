package com.exlibris.dps.sdk.dgtconv;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import excel.writer.MyExcel;

public class DigitoolRosettaXRef {

	private static Logger logger = Logger.getLogger(DigitoolRosettaXRef.class);
	
	public static void main(String[] args) {

	FileInputStream fis = null;
	String pid;
	int pidnr;
	String sip;
	MyExcel rosettaLink;
	int iexcel = 0;
		try {
			
			rosettaLink = new MyExcel("C:\\rosetta_log\\dgt_rosetta_posters.xls");
			rosettaLink.addCaption(0, 0, "PID");
			rosettaLink.addCaption(1, 0, "SIP");			
			
			File streamDir = new File("C:\\rosetta_log");
			File[] logfiles = streamDir.listFiles();
						
			
			for (int ilog = 0;ilog < logfiles.length;ilog++) {
			
	        fis = new FileInputStream(logfiles[ilog]);
	        	  // Get the object of DataInputStream
	        	  DataInputStream in = new DataInputStream(fis);
	        	  BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF8"));
	        	  String strLine;

	        	  //Read File Line By Line
	        	  while ((strLine = br.readLine()) != null)
	        	  {
	        		  if (strLine.contains(".xml")) {
	        			  pid = strLine.substring(strLine.indexOf("[FlowExample] ")+14,strLine.indexOf(".xml"));
	        			  
	        			  try {
	        				  pidnr = Integer.valueOf(pid);
	        				  iexcel++;
	        				  rosettaLink.addNumber(0, iexcel, pidnr);
	        			  } catch (Exception e) {
	        				  logger.info(pid +":" +e.getMessage());

	        			  }
	        		  } else if (strLine.contains("sip_id")) {
	        			  sip = strLine.substring(strLine.indexOf("sip_id")+7,strLine.indexOf("</ser:sip_id>"));
	        			  rosettaLink.addNumber(1, iexcel, Integer.valueOf(sip));
	        		  }
	        	  }
     	  
	        	  //Close the input stream
	        	  in.close();
	        	  in = null;
	        	  br = null;
	        	  rosettaLink.SaveExcel();
	        	}
	        fis.close();
	        fis = null;
	        rosettaLink.closeExcel();
	        
		} catch (Exception e) {
			logger.info("C:\\rosetta_log not found");
			logger.info(e.getMessage());
		}
     fis = null;
	}
}
