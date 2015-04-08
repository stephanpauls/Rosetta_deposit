package com.exlibris.dps.sdk.dgtconv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.exlibris.core.sdk.consts.Enum;
import com.exlibris.core.sdk.formatting.DublinCore;
import com.exlibris.core.sdk.utils.FileUtil;
import com.exlibris.digitool.common.dnx.DNXConstants;
import com.exlibris.digitool.common.dnx.DnxDocument;
import com.exlibris.digitool.common.dnx.DnxDocumentHelper;
import com.exlibris.digitool.common.dnx.DnxSection;
import com.exlibris.digitool.common.dnx.DnxSectionRecord;
import com.exlibris.dps.sdk.deposit.IEParser;
import com.exlibris.dps.sdk.deposit.IEParserFactory;

import excel.writer.MyExcel;
import gov.loc.mets.FileType;
import gov.loc.mets.MetsType.FileSec.FileGrp;

public class Bibliotheca {

	private static String pid = null;
	private static Logger logger = Logger.getLogger(Bibliotheca.class);
	private static Integer dgtTypes;
	private static String dgtEntityTypes;
	private static IEToMets iEToMets;
	private static File inputFile;
	private static FileInputStream fis;
	

	public static  boolean putBibFileToRootFolder(String filepath,String filename,String extension) throws Exception {
		filepath = filepath.replace("/nas", "M:");
		filepath = filepath.replace("/exlibris", "O:");
        File file = new File(FlowExample.prop.getProperty("filesRootFolder") + "/" +filename);
		try {
			
	        inputFile = new File(filepath);
	        fis = new FileInputStream(inputFile);
	        if (!file.exists()) {
	        	if ("xml".equals(extension)) {
	        	  // Get the object of DataInputStream
	        	  DataInputStream in = new DataInputStream(fis);
	        	  BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF8"));
	        	  OutputStreamWriter outputStreamWriter = new
	        	  OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()), "UTF8");
	        	  BufferedWriter bw = new BufferedWriter(outputStreamWriter);
	        	  String strLine;
	        	  //Read File Line By Line
	        	  while ((strLine = br.readLine()) != null)
	        	  {
	        		  if (strLine.contains("xml-stylesheet")) {
	        			  bw.write("<?xml-stylesheet type=\"text/xsl\" href=\"ead/imagopage.xsl\"?>");
	        		  } else {
	        			  bw.write(strLine);
	        		  }
	        	  }
        	  
	        	  //Close the input stream
	        	  in.close();
	        	  bw.close();
	        	  in = null;
	        	  br = null;
	        	  //fw = null;
	        	} else {
			        OutputStream out=new FileOutputStream(file);
			        byte buf[]=new byte[1024*256];
			        int len;
			        while ((len=fis.read(buf))>0) {
			        	out.write(buf,0,len);
			        }
			        out.close();
			        out = null;
	        	}
	        }
	        fis.close();
	        inputFile = null;
	        fis = null;
	        file = null;
	        return true;  
	        
		} catch (Exception e) {
			logger.info(FlowExample.prop.getProperty("filesRootFolder") + "/" +filename + "not found");
			logger.info(e.getMessage());
		}
        inputFile = null;
        fis = null;
        file = null;
		return false;
    }
	
	
	public static void main(String[] args) {
	
	Statement stmt = null;
	ResultSet rset = null;
	Statement stmt2 = null;
	ResultSet rset2 = null;
	String filelabel = null;
	String pid_file = null;
	String filenaam = null;
	String filepath = null;
	String usagetype = null;
	String mimeType = null;
	Integer m = 0;
	String label = null;
	String clob = null;
	boolean jmpToNextPID = false;
	
	
	FileGrp fGrp = null;
	ExtFileGrp extFileGrp = null;			
	List<FileGrp> fGrpList = new ArrayList<FileGrp>();

	dgtTypes = DGTConvConstants.DGT_GBIB_BIF;
	dgtEntityTypes = DGTConvConstants.DGT_ENTITY_GBIB_BIF;
	
	FlowExample.prop = new Properties();
	FlowExample.metsCounter = 0;
	
	DOMConfigurator.configure("log4j.xml");
	org.apache.log4j.helpers.LogLog.setQuietMode(true);		//create oracle connection 
	TestDBOracle db = new TestDBOracle();
	
	try {
		FlowExample.prop.load(new FileInputStream("dgtconv.properties"));
		// clean rosetta input stream directory
		FileUtil.cleanDirectory(FlowExample.prop.getProperty("filesRootFolder"));
		File streamRosettaMetsDir = new File(FlowExample.prop.getProperty("folder_on_working_machine") + FlowExample.prop.getProperty("subDirectoryName") +"/content");
		File[] rosettaMetsfiles = streamRosettaMetsDir.listFiles();
		while (FlowExample.metsCounter < rosettaMetsfiles.length) {
			String ext = rosettaMetsfiles[FlowExample.metsCounter].getName();
			if (ext.contains("xml") == true) {
				FileUtil.forceDelete(new File(rosettaMetsfiles[FlowExample.metsCounter].getAbsolutePath()));
			}
			FlowExample.metsCounter++;
		}

	    String pattern = "MMddyyyy_HHmmss";
	    SimpleDateFormat format = new SimpleDateFormat(pattern);
		String df = format.format(new Date());
			
    	FlowExample.metsCounter = 0;
		FlowExample.rosettaLink = new MyExcel("dgt_rosetta"+df+".xls");
		FlowExample.rosettaLink.addCaption(0, 0, "PID");
		FlowExample.rosettaLink.addCaption(1, 0, "SIP");
		FlowExample.rosettaLink.addCaption(2, 0, "IE");				
		FlowExample.rosettaLink.SaveExcel();
		
		//create IEToMets
		iEToMets = new IEToMets();
		  
		//create parser
		IEParser ie = null;
		
		stmt = db.getConn().createStatement();
	    String query =  "select pid,substr(label,instr(label,'text: ')+6) as label from hdecontrol where ingestname like 'dk-gbib%' and entitytype = 'METS' and partitionb is null order by pid";	    
	    
	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
	    	jmpToNextPID = false;
	    	m=0;
	    	pid = rset.getString(1);
	    	label = rset.getString(2);
	    	
			ie = null;
			ie = IEParserFactory.create();
			DublinCore dc = ie.getDublinCoreParser();
	    	
	    	
	    	//get dublincore info
			int dc_index = 0;
			int dc_lastindex = 0;
			String clobRest;
			String element;
			if ((clob = db.getDublinCore(pid)) != "") {
				clobRest = clob;
				while((dc_index = clobRest.indexOf("<dc:type>")) > 0) {
					dc_index = dc_index+9;
					dc_lastindex = clobRest.indexOf("</dc:type>");
					element  = clobRest.substring(dc_index,dc_lastindex);
					dc.addElement("dc:type",element);
					clobRest = clobRest.substring(dc_lastindex+1);
				}
				clobRest = clob;
				while((dc_index = clobRest.indexOf("<dc:identifier>")) > 0) {
					dc_index = dc_index+15;
					dc_lastindex = clobRest.indexOf("</dc:identifier>");
					element  = clobRest.substring(dc_index,dc_lastindex);
					dc.addElement("dc:identifier",element);
					clobRest = clobRest.substring(dc_lastindex+1);
				}
				clobRest = clob;
				while((dc_index = clobRest.indexOf("<dc:subject>")) > 0) {
					dc_index = dc_index+12;
					dc_lastindex = clobRest.indexOf("</dc:subject>");
					element  = clobRest.substring(dc_index,dc_lastindex);
					dc.addElement("dc:subject",element);
					clobRest = clobRest.substring(dc_lastindex+1);
				}
				clobRest = clob;
				while((dc_index = clobRest.indexOf("<dc:description>")) > 0) {
					dc_index = dc_index+16;
					dc_lastindex = clobRest.indexOf("</dc:description>");
					element  = clobRest.substring(dc_index,dc_lastindex);
					dc.addElement("dc:description",element);
					clobRest = clobRest.substring(dc_lastindex+1);
				}
				clobRest = clob;
				while((dc_index = clobRest.indexOf("<dc:title>")) > 0) {
					dc_index = dc_index+10;
					dc_lastindex = clobRest.indexOf("</dc:title>");
					element  = clobRest.substring(dc_index,dc_lastindex);
					dc.addElement("dc:title",element);
					clobRest = clobRest.substring(dc_lastindex+1);
				}				
				ie.setIEDublinCore(dc);
			}	    	

			extFileGrp = new ExtFileGrp(ie,label);
/*			
			fGrp = extFileGrp.generateDerivativeHtmlFgrp();
			fGrp = extFileGrp.generateDerivativeXMLMDFgrp();
			fGrp = extFileGrp.generateModifiedFgrp();
			fGrp = extFileGrp.generatePreservationFgrp();
*/
			
			
		    stmt2 = db.getConn().createStatement();
	    	String query2 = "select pid,usagetype from hdecontrol where id in (select distinct r.targetcontrol from hdecontrol c inner join hderelation r on r.control = c.id  where c.pid = '"+pid+"') and lower(usagetype) != 'thumbnail'";
		    rset2 = stmt2.executeQuery(query2);
		    while (rset2.next() && jmpToNextPID == false) {

		    	pid_file = rset2.getString(1);
		    	usagetype = rset2.getString(2);
		    	// get fileinfo and filename
				if ((filenaam = db.getFilename(pid_file)) == null) 
				{
					continue;
				} 
				String extension = filenaam.substring(filenaam.indexOf('.')+1).toLowerCase();
				
			    mimeType= URLConnection.guessContentTypeFromName(filenaam);

				filenaam = filenaam.replace(' ', '_');
				filenaam = filenaam.replace('[', '_');
				filenaam = filenaam.replace(']', '_');
				filenaam = filenaam.replace('/', '_');

				filepath = db.getFilepath(pid_file);
		    	
		    	// define filegroup

				
				// add fileGrp

		
				
				try{
					FlowExample.rosettaLink.addNumber(0, (FlowExample.metsCounter+1), Integer.valueOf(pid));
				} catch (RowsExceededException e) {
					
				}
				catch (WriteException we){
					
				}
				db.getFileParameters(pid);
				

			    DnxDocument dnxie = ie.getDnxParser();
			    DnxSection dnxieS = dnxie.createSection(DNXConstants.GENERALIECHARACTERISTICS.sectionId);
			    DnxSectionRecord dnxieSR = dnxieS.createRecord();
			    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.IEENTITYTYPE.sectionKeyId, "", "", dgtEntityTypes);
			    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.USERDEFINEDA.sectionKeyId, "", "", db.getpA());
			    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.USERDEFINEDB.sectionKeyId, "", "", db.getpB());
			    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.USERDEFINEDC.sectionKeyId, "", "", db.getpC());
			    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.STATUS.sectionKeyId, "", "", db.getStatus());				
				
			    DnxSection dnxieSAR = dnxie.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
			    DnxSectionRecord dnxieSRAR = dnxieSAR.createRecord();
			    dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", FlowExample.prop.getProperty("RST_ACCESS_RIGHTS_EVERYONE"));
			    
			    ie.setIeDnx(dnxie);	
			    
            	fGrp = FlowExample.addFileGroup(usagetype,dgtTypes,ie,filenaam,extFileGrp,pid_file);
				if (fGrp != null ) {
					String nameNoExt = filenaam.substring(0,filenaam.lastIndexOf('.'));
					fGrpList.add(fGrp);
					if (putBibFileToRootFolder(filepath,filenaam, extension) == false ) {
						jmpToNextPID = true;
						continue;
					} 
				
					filelabel = db.getFilelabel(pid_file);
					
		            //add file and dnx metadata on file
		            FileType fileType = ie.addNewFile(fGrp, mimeType, filenaam, nameNoExt + m++);
		            fileType.setGROUPID(filelabel);
		            
		            // add dnx - A new DNX is constructed and added on the file level
		            DnxDocument dnx = ie.getFileDnx(fileType.getID());
		            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
			    
					fileDocumentHelper.getGeneralFileCharacteristics().setLabel(filelabel);
		            fileDocumentHelper.getGeneralFileCharacteristics().setFileOriginalPath(filepath);
		            fileDocumentHelper.getGeneralFileCharacteristics().setFileEntityType(dgtEntityTypes);
//		            fileDocumentHelper.setObjectCharacteristics(fileDocumentHelper.new ObjectCharacteristics());
//		            fileDocumentHelper.getObjectCharacteristics().setGroupID(filelabel);
		            
		            ie.setFileDnx(fileDocumentHelper.getDocument(), fileType.getID());
		            
				} else {
					jmpToNextPID = true;
				}
			
		    }
		    if (jmpToNextPID == false) {
	        	ie.generateChecksum(FlowExample.prop.getProperty("filesRootFolder"), Enum.FixityType.MD5.toString());
	        	ie.updateSize(FlowExample.prop.getProperty("filesRootFolder"));
	
	    		if (extFileGrp.isFileGrpPreservationFlag()) ie.generateStructMap(extFileGrp.getFileGrpPreservation(),"Archiefkopie", "Inhoudsopgave");
	    		if (extFileGrp.isFileGrpModifiedFlag()) ie.generateStructMap(extFileGrp.getFileGrpModified(),"Hoge kwaliteit", "Inhoudsopgave");
	    		if (extFileGrp.isFileGrpDerivativeXMLMDFlag()) ie.generateStructMap(extFileGrp.getFileGrpDerivativeXMLMD(),"Structuur", "Inhoudsopgave");
	    		if (extFileGrp.isFileGrpDerivativeHtmlFlag()) ie.generateStructMap(extFileGrp.getFileGrpDerivativeHtml(),"Algemeen overzicht", "Inhoudsopgave");
	    		
	    		iEToMets.setSubDirectoryName(FlowExample.prop.getProperty("subDirectoryName"));
	    		
	    		if (iEToMets.CreateAndIngestMets(fGrpList, ie,label) == true) {
	    			FileUtil.cleanDirectory(FlowExample.prop.getProperty("filesRootFolder"));
	    			extFileGrp.clean();
	    			fGrpList.clear();
	    			FlowExample.rosettaLink.SaveExcel();
	    			String partition = dgtTypes.equals(DGTConvConstants.DGT_ETD_KUL) ? "partitiona" : "partitionb";
	    			db.setDone(partition, pid);
	    		} else {
	    			FileUtil.cleanDirectory(FlowExample.prop.getProperty("filesRootFolder"));
	    			extFileGrp.clean();
	    			fGrpList.clear();
	    			FlowExample.rosettaLink.SaveExcel();
	    		}			 		  
		    }
		    FlowExample.metsCounter++;
        	
		    rset2.close();
		    stmt2.close();	

	    }
	    rset.close();
	    stmt.close();
	    FlowExample.rosettaLink.closeExcel();
	  } catch (Exception e) {
		  logger.info(e.getMessage());
		  try{
			  FlowExample.rosettaLink.closeExcel();
		  	}  catch (IOException ioe) {
		  	}
	  }
	}
}
