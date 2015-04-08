package com.exlibris.dps.sdk.dgtconv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.dom4j.xpath.DefaultNamespaceContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.exlibris.core.sdk.formatting.DublinCore;
import com.exlibris.core.sdk.parser.IEParserException;
import com.exlibris.core.sdk.utils.FileUtil;
import com.exlibris.digitool.common.dnx.DNXConstants;
import com.exlibris.digitool.common.dnx.DnxDocument;
import com.exlibris.digitool.common.dnx.DnxDocumentHelper;
import com.exlibris.digitool.common.dnx.DnxDocumentHelper.Collection;
import com.exlibris.digitool.common.dnx.DnxDocumentHelper.ObjectCharacteristics;
import com.exlibris.digitool.common.dnx.DnxSection;
import com.exlibris.digitool.common.dnx.DnxSectionRecord;
import com.exlibris.dps.sdk.deposit.IEParser;
import com.exlibris.dps.sdk.deposit.IEParserFactory;
import com.exlibris.repository.persistence.HDeSet.SetType;
import com.exlibris.core.sdk.consts.Enum;
import com.sun.corba.se.impl.ior.NewObjectKeyTemplateBase;


import edu.harvard.hul.ois.mets.AmdSec;
import edu.harvard.hul.ois.mets.Div;
import edu.harvard.hul.ois.mets.DmdSec;
import edu.harvard.hul.ois.mets.FLocat;
import edu.harvard.hul.ois.mets.FileSec;
import edu.harvard.hul.ois.mets.Fptr;
import edu.harvard.hul.ois.mets.MdWrap;
import edu.harvard.hul.ois.mets.Mets;
import edu.harvard.hul.ois.mets.MetsHdr;
import edu.harvard.hul.ois.mets.RightsMD;
import edu.harvard.hul.ois.mets.StructMap;
import edu.harvard.hul.ois.mets.XmlData;
import edu.harvard.hul.ois.mets.helper.Any;
import edu.harvard.hul.ois.mets.helper.DateTime;
import edu.harvard.hul.ois.mets.helper.MetsElement;
import edu.harvard.hul.ois.mets.helper.MetsReader;
import edu.harvard.hul.ois.mets.helper.PCData;
import edu.harvard.hul.ois.mets.helper.parser.Attribute;
import edu.harvard.hul.ois.mets.helper.parser.Attributes;
import gov.loc.mets.FileType;
import gov.loc.mets.MetsType.FileSec.FileGrp;

//import javax.imageio.ImageWriter;

import excel.writer.MyExcel;

public class 	FlowExample {

	public static Integer dgtTypes;
	public static String dgtEntityTypes;
	private static String userid = "";
	public static MyExcel rosettaLink;
	public static int metsCounter=0;
	public static int metsFileCounter=1;
	private static Logger logger = Logger.getLogger(FlowExample.class);
	private static File inputFile;
	private static FileInputStream fis;
	private static IEToMets iEToMets;
	private static String complexElementThumbnail = "0";
	public static Properties prop; 
	private static String pid = null;
	private static String ip_low="",ip_high="";

	
	public static String convertStreamToString(java.io.InputStream is) {
	    try {
	        return new java.util.Scanner(is).useDelimiter("\\A").next();
	    } catch (java.util.NoSuchElementException e) {
	        return "";
	    }
	}

	
	public static boolean putFileToRootFolder(String filepath,String filename,boolean addEADHeader) throws Exception {
		
		if (prop.getProperty("filesRootFolder").contains("M")) filepath = filepath.replace("/nas", "M:");
        File file = new File(prop.getProperty("filesRootFolder") + "/" +filename);
		try {
			
	        inputFile = new File(filepath);
	        fis = new FileInputStream(inputFile);
	        if (!file.exists()) {  
	        	if (addEADHeader) {
	        	  // Get the object of DataInputStream
	        	  DataInputStream in = new DataInputStream(fis);
	        	  BufferedReader br = new BufferedReader(new InputStreamReader(in,"ISO-8859-1"));
//	        	  FileWriter fw = new FileWriter(file.getAbsoluteFile());
	        	  OutputStreamWriter outputStreamWriter = new
	        			  OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()), "UTF8");
	        	  BufferedWriter bw = new BufferedWriter(outputStreamWriter);
	        	  String strLine;
	        	  //Read File Line By Line
	        	  Boolean xml_version_found= false;
	        	  while ((strLine = br.readLine()) != null)
	        	  {
	        		  if (strLine.contains("xml version=")) {
	        			  xml_version_found = true;
	        			  bw.write(strLine);
	        		  } else if (xml_version_found) {
	        			  xml_version_found = false;
	        			  if (!strLine.contains("xml-stylesheet")) { 
	        				bw.write("<?xml-stylesheet type=\"text/xsl\" href=\"ead/scope_eadToHTML.xsl\"?><ead>");
	        			  }
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
			logger.info(prop.getProperty("filesRootFolder") + "/" +filename + "not found");
			logger.info(e.getMessage());
		}
        inputFile = null;
        fis = null;
        file = null;
		return false;
    }
	
	public static Mets readMetsDtlFile(String metsDtlFile) {
		try {
		  FileInputStream in = new FileInputStream (metsDtlFile);
		  Mets mets = Mets.reader (new MetsReader (in,false));
		  in.close ();
          return mets;
		} catch (Exception e) {
				e.printStackTrace();
		}
	  return null;
	}
	
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	 
	        Node nValue = (Node) nlList.item(0);
	 
		return nValue.getNodeValue();
	}
	
	public static FileGrp addFileGroup(String use, Integer dgtType,IEParser ie,String filenaam,ExtFileGrp fileGrp,String pid) {
		
		FileGrp fGrp = null;
		try {
			
		String extension = filenaam.substring(filenaam.indexOf('.')+1).toLowerCase();
		boolean watermark = filenaam.toUpperCase().contains("VIEW_MAIN");
			switch(dgtType) {
			case DGTConvConstants.DGT_WEBDOC:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.generatePreservationFgrp();
					
				}
				break;
			case DGTConvConstants.DGT_EAD:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW") && (extension.contains("pdf"))) {
					logger.info("VIEW pdf or watermark");					
					fGrp = fileGrp.generateDerivativeFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateModifiedFgrp();
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_DIG_POSTERS:
			case DGTConvConstants.DGT_DIG_FLAGS:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW") && watermark) {
					fGrp = fileGrp.generateDerivativeFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.getFileGrpModified();
					if (fGrp == null) {
						fGrp = fileGrp.generateModifiedFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCGUEST"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
					} 
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}				
				break;
			case DGTConvConstants.DGT_DIG_PHOTO_ALBUMS:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
					} 
				} else if (use.equals("VIEW") && (watermark)) {
					fGrp = fileGrp.generateDerivativeFgrp();
				} else if (use.equals("VIEW") && (extension.contains("pdf"))) {
					fGrp = fileGrp.generateDerivativePdfFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.getFileGrpModified();
					if (fGrp == null) {
						fGrp = fileGrp.generateModifiedFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCGUEST"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
					} 
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_ARCHIVAL_IMAGES:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.generatePreservationFgrp();
				} else if (use.equals("VIEW") && (watermark)) {
					fGrp = fileGrp.generateDerivativeFgrp();
				} else if (use.equals("VIEW") && (extension.contains("pdf"))) {
					fGrp = fileGrp.generateDerivativePdfFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateModifiedFgrp();
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_GBIB_OFMCapths:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_GBIBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}				
				} else if (use.equals("VIEW") && (extension.contains("pdf"))) {
					fGrp = fileGrp.generateDerivativePdfFgrp();
				} else if (use.equals("VIEW")  && (extension.contains("jp2"))) {
					fGrp = fileGrp.generateModifiedFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_BIBC_Manuscripts:
			case DGTConvConstants.DGT_BIBC_BOEK_17:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
						
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
				    		
					}
				} else if (use.equals("THUMBNAIL") && (pid.equals(complexElementThumbnail))) {
					fGrp = fileGrp.generateDerivativeThmbnlFgrp();
				} else if (use.equals("VIEW") && (extension.contains("jp2"))) {
					fGrp = fileGrp.generateModifiedFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_BIBC_BOEK_23:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
				    		
					}
				} else if (use.equals("THUMBNAIL") && (pid.equals(complexElementThumbnail))) {
					fGrp = fileGrp.generateDerivativeThmbnlFgrp();
				} else if (use.equals("VIEW") && (extension.contains("jp2"))) {
					fGrp = fileGrp.getFileGrpModified();
					if (fGrp == null) {
						fGrp = fileGrp.generateModifiedFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "","",  prop.getProperty("RST_AR_IP_RANGE_KUL"));
					    DnxSection dnxieS2 = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR2 = dnxieS2.createRecord();
				    	dnxieSR2.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBGUEST"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}					
					
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.getFileGrpDerivative();
					if (fGrp == null) {
						fGrp = fileGrp.generateDerivativeFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "","",  prop.getProperty("RST_AR_IP_RANGE_KUL"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}						
				}
				break;
			case DGTConvConstants.DGT_BIBC_ExCathedra:
			case DGTConvConstants.DGT_BIBC_LEUVEN:
			case DGTConvConstants.DGT_BIBC_Flandrica:
			case DGTConvConstants.DGT_KUL_EGYPTOLOGIE:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("THUMBNAIL") && (pid.equals(complexElementThumbnail))) {
					fGrp = fileGrp.generateDerivativeThmbnlFgrp();
				} else if (use.equals("VIEW") && (extension.contains("jp2"))) {
					fGrp = fileGrp.getFileGrpModified();
					if (fGrp == null) {
						fGrp = fileGrp.generateModifiedFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBGUEST"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
					
				} else if (use.equals("VIEW") || use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.getFileGrpDerivative();
					if (fGrp == null) {
						fGrp = fileGrp.generateDerivativeFgrp();
					}
				}
				break;
			case DGTConvConstants.DGT_ETD_TMK:
			case DGTConvConstants.DGT_ETD_KATHO:
			case DGTConvConstants.DGT_ETD_KHBO:
			case DGTConvConstants.DGT_ETD_KHL:
			case DGTConvConstants.DGT_ETD_KHLIM:
			case DGTConvConstants.DGT_ETD_TMM:
			case DGTConvConstants.DGT_ETD_TM_TEST:				
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.generatePreservationFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateModifiedFgrp();
				}
			    break;
			case DGTConvConstants.DGT_ETD_KUL:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.generatePreservationFgrp();
				} else if (use.equals("THUMBNAIL") && (pid.equals(complexElementThumbnail))) {
					fGrp = fileGrp.generateDerivativeThmbnlFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateModifiedFgrp();
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}				
				break;
			case DGTConvConstants.DGT_BIBC_ELPRENT:
			case DGTConvConstants.DGT_KUL_ILLUMINARE:
			case DGTConvConstants.DGT_UB_VERWILGHEN:
			case DGTConvConstants.DGT_BIBC_BRIEFKAART:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("THUMBNAIL") && (pid.equals(complexElementThumbnail))) {
					fGrp = fileGrp.generateDerivativeThmbnlFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateModifiedFgrp();
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_BIBC_OUDE_THESIS:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("THUMBNAIL") && (pid.equals(complexElementThumbnail))) {
					fGrp = fileGrp.generateDerivativeThmbnlFgrp();
				} else if (use.equals("VIEW")  && (extension.contains("jp2"))) {
					fGrp = fileGrp.generateModifiedFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_CBA_MILITAIRE_VERKENNING:
			case DGTConvConstants.DGT_2BERGEN_MOND:				
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("THUMBNAIL") && (pid.equals(complexElementThumbnail))) {
					fGrp = fileGrp.generateDerivativeThmbnlFgrp();
				} else if (use.equals("VIEW")  && (extension.contains("jp2"))) {
					fGrp = fileGrp.getFileGrpModified();
					if (fGrp == null) {
						fGrp = fileGrp.generateModifiedFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "","",  prop.getProperty("RST_AR_IP_RANGE_KUL"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}					
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.getFileGrpDerivative();
					if (fGrp == null) {
						fGrp = fileGrp.generateDerivativeFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "","",  prop.getProperty("RST_AR_IP_RANGE_KUL"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}					
				}
				break;
			case DGTConvConstants.DGT_BOEKEN:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
					} 
				} else if (use.equals("VIEW") && (watermark)) {
					fGrp = fileGrp.generateDerivativeFgrp();
			
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_KADOC_EPeriodieken:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCGUEST"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
					} 
				} else if ((use.equals("VIEW") && (watermark)) || (use.equals("VIEW_MAIN"))) {
					fGrp = fileGrp.generateDerivativeFgrp();
				} else if (use.equals("VIEW") && (extension.contains("pdf"))) {
					fGrp = fileGrp.generateDerivativePdfFgrp();
					if (fGrp == null) {
						fGrp = fileGrp.generateModifiedFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCGUEST"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
					} 
				}
				break;
			case DGTConvConstants.DGT_BIBC_Legerbode:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("THUMBNAIL") && (pid.equals(complexElementThumbnail))) {
					fGrp = fileGrp.generateDerivativeThmbnlFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateModifiedFgrp();
				}
				break;
			case DGTConvConstants.DGT_GBIB_Luther:
			case DGTConvConstants.DGT_GBIB_Augustinus:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_GBIBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("THUMBNAIL") && (pid.equals(complexElementThumbnail))) {
					fGrp = fileGrp.generateDerivativeThmbnlFgrp();
				} else if (use.equals("VIEW") && (extension.contains("jp2"))) {
					fGrp = fileGrp.generateModifiedFgrp();					
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_GBIB_Anjou:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_GBIBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("THUMBNAIL") && (pid.equals(complexElementThumbnail))) {
					fGrp = fileGrp.generateDerivativeThmbnlFgrp();
				} else if (use.equals("VIEW") && (extension.contains("jp2"))) {
					fGrp = fileGrp.getFileGrpModified();
					if (fGrp == null) {
						fGrp = fileGrp.generateModifiedFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_GBIBGUEST"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
					} 
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_GBIB_BIF:
				if (use.equals("reference") && (extension.contains("xml"))) {
					fGrp = fileGrp.generateDerivativeXMLMDFgrp();
				} else if (use.equals("reference") && (extension.contains("jp2"))) {
					fGrp = fileGrp.generateModifiedFgrp();
				} else if (use.equals("reference") && (extension.contains("gif"))) {
					fGrp = fileGrp.generatePreservationFgrp();
				} else if (use.equals("reference") && (extension.contains("html"))) {
					fGrp = fileGrp.generateDerivativeHtmlFgrp();
				}
				break;
			case DGTConvConstants.DGT_UBG_Flandrica:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBGADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_UA_Flandrica:
			case DGTConvConstants.DGT_EHC_Flandrica:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UAADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_PBL_Flandrica:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_PBLADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_OBB_Flandrica:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_OBBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;

			case DGTConvConstants.DGT_CRKC_STANDAARD:
				if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_CRKCADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				}
				break;
			case DGTConvConstants.DGT_CAG_STANDAARD:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_CAGADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_BIBC_POPP:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW") && (extension.contains("jp2"))) {
					fGrp = fileGrp.getFileGrpModified();
					if (fGrp == null) {
						fGrp = fileGrp.generateModifiedFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_UBGUEST"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
					} 
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_KADOC_SINT_LUCAS:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.getFileGrpModified();
					if (fGrp == null) {
						fGrp = fileGrp.generateModifiedFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCGUEST"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
					} 
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}				
				break;
			case DGTConvConstants.DGT_KADOC_Capronnier:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.getFileGrpModified();
					if (fGrp == null) {
						fGrp = fileGrp.generateModifiedFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOC_CAPRONNIER"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
					} 
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}				
				break;
			case DGTConvConstants.DGT_KADOC_Trajecta:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.generatePreservationFgrp();
				} else if (use.equals("INDEX"))  {
					fGrp = fileGrp.generateDerivativeIndexFgrp();
				}
				break;
			case DGTConvConstants.DGT_COLL_TEXTURA:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.getFileGrpModified();
					if (fGrp == null) {
						fGrp = fileGrp.generateModifiedFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCGUEST"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());
					} 
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}				
				break;
			case DGTConvConstants.DGT_STEPHAN_TILED:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
					}
				} else if (use.equals("VIEW") && (extension.contains("jp2"))) {
					fGrp = fileGrp.generateModifiedFgrp();
				} else if (use.equals("VIEW")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;	
			case DGTConvConstants.DGT_BIBC_MULTISPECTRAAL:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
					}
				} else if (use.equals("VIEW") && (extension.contains("tiff"))) {
					fGrp = fileGrp.generateModifiedFgrp();
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;
			case DGTConvConstants.DGT_RBINS_EUINSIDE:
				if (use.equals("ARCHIVE")) {
					fGrp = fileGrp.getFileGrpPreservation();
					if (fGrp == null) {
						fGrp = fileGrp.generatePreservationFgrp();
			            DnxDocument dnx = ie.getFileGrpDnx(fGrp.getID());
			            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);
					    DnxSection dnxieS = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    	dnxieSR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_RBINSADMIN"));
				    	ie.setFileGrpDnx(dnx, fGrp.getID());	
					}
				} else if (use.equals("VIEW_MAIN")) {
					fGrp = fileGrp.generateDerivativeFgrp();
				}
				break;				
			default : ;
			}
		} catch (IEParserException pe) {
			logger.info(pe.getStackTrace());
		}
		return fGrp;
	}
	
	public static String defineEntityType(String pB, String pC, String label, String ingest) {
		
		if (pC.contains("WebDoc/")) {
			dgtTypes = DGTConvConstants.DGT_WEBDOC;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_WEBDOC;
		} else if (pC.contains("KADOC, EAD")) {
			dgtTypes = DGTConvConstants.DGT_EAD;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_EAD;
		} else if (
				(pC.contains("KADOC, Affiche")) &&
				(
					(label.substring(0,2).contains("kc")) ||
					(label.substring(0,2).contains("kl")) ||
					(label.substring(0,2).contains("kp")) ||
					(label.substring(0,3).contains("kfa")) ||
					(label.substring(0,3).contains("kfb")) ||
					(label.substring(0,3).contains("kfc")) ||
					(label.substring(0,3).contains("kfd")) ||
					(label.substring(0,3).contains("kfe"))) 
				) {
			dgtTypes = DGTConvConstants.DGT_DIG_POSTERS;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_DIG_POSTERS;			
		} else if (
				(pC.contains("KADOC, Affiche")) &&	
				((label.substring(0,3).contains("kfh")) ||
				 (label.substring(0,2).contains("kk")))
				) {
			dgtTypes = DGTConvConstants.DGT_DIG_PHOTO_ALBUMS;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_DIG_PHOTO_ALBUMS;
		} else if ((pC.contains("KADOC, Affiche")) &&	(label.substring(0,3).contains("kva")))	 {
			dgtTypes = DGTConvConstants.DGT_DIG_FLAGS;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_DIG_FLAGS;
		} else if (((pC.contains("KADOC, Archief")) &&	(label.substring(0,9).contains("BE-942855"))) ||
					((pB != null && pB.contains("DO_MIGRATIE_DIGITIZED_IMAGE"))))	 {
			dgtTypes = DGTConvConstants.DGT_ARCHIVAL_IMAGES;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_ARCHIVAL_IMAGES;
		} else if (
				(pC.contains("KADOC, Boeken")) || 
				((pC.contains("KADOC, Affiche")) &&	(label.substring(0,3).contains("KBE")))
				) {
			dgtTypes = DGTConvConstants.DGT_BOEKEN;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BOEKEN;
		} else if (
				(pC.contains("KADOC, E-periodieken")) || 
				((pC.contains("KADOC, Affiche")) &&	(label.substring(0,3).contains("KYE")))
				) {
			dgtTypes = DGTConvConstants.DGT_KADOC_EPeriodieken;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_KADOC_EPeriodieken;
		} else if (pC.contains("GBIB, OFMCapths")) {
			dgtTypes = DGTConvConstants.DGT_GBIB_OFMCapths;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_GBIB_OFMCapths;
		} else if (pC.contains("BIBC, ELPRENT")) {
			dgtTypes = DGTConvConstants.DGT_BIBC_ELPRENT;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_ELPRENT;
		} else if (pC.contains("KUL, Illuminare"))  {
			dgtTypes = DGTConvConstants.DGT_KUL_ILLUMINARE;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_KUL_ILLUMINARE;
		} else if (pC.contains("BIBC, Oude thesissen")) {
			dgtTypes = DGTConvConstants.DGT_BIBC_OUDE_THESIS;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_OUDE_THESIS;
		} else if (pC.contains("BIBC, Venster op Leuven")) {
			dgtTypes = DGTConvConstants.DGT_BIBC_LEUVEN;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_LEUVEN;
		} else if ((pC.contains("BIBC, BoekInDeKijker")) &&	(label.substring(0,9).contains("DIGI_0017"))) {
			dgtTypes = DGTConvConstants.DGT_BIBC_BOEK_17;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_BOEK;
		} else if ((pC.contains("BIBC, BoekInDeKijker")) &&	(label.substring(0,9).contains("DIGI_0023"))) {
			dgtTypes = DGTConvConstants.DGT_BIBC_BOEK_23;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_BOEK;
		} else if (pC.contains("ETD KUL")) {
			dgtTypes = DGTConvConstants.DGT_ETD_KUL;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_ETD_KUL;
		} else if (pC.contains("ETD TMK")) {
			dgtTypes = DGTConvConstants.DGT_ETD_TMK;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_ETD_TMK;
		} else if (pC.contains("ETD TMM")) {
			dgtTypes = DGTConvConstants.DGT_ETD_TMM;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_ETD_TMM;
		} else if (pC.contains("ETD KHBO")) {
			dgtTypes = DGTConvConstants.DGT_ETD_KHBO;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_ETD_KHBO;
		} else if (pC.contains("ETD KATHO")) {
			dgtTypes = DGTConvConstants.DGT_ETD_KATHO;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_ETD_KATHO;
		} else if (pC.contains("ETD KHLIM")) {
			dgtTypes = DGTConvConstants.DGT_ETD_KHLIM;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_ETD_KHLIM;
		} else if (pC.contains("ETD KHL")) {
			dgtTypes = DGTConvConstants.DGT_ETD_KHL;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_ETD_KHL;
		} else if (pC.contains("BIBC, Ex Cathedra")) {
			dgtTypes = DGTConvConstants.DGT_BIBC_ExCathedra;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_ExCathedra;
		} else if (pC.contains("BIBC, Legerbode")) {
			dgtTypes = DGTConvConstants.DGT_BIBC_Legerbode;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_Legerbode;
		} else if (pC.contains("BIBC, BTABBIBCMS")) {
			dgtTypes = DGTConvConstants.DGT_BIBC_Manuscripts;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_Manuscripts;
		} else if (pC.contains("GBIB, Luther")) {
			dgtTypes = DGTConvConstants.DGT_GBIB_Luther;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_GBIB_Luther;
		} else if (pC.contains("GBIB, Augustinus 1640")) {
			dgtTypes = DGTConvConstants.DGT_GBIB_Augustinus;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_GBIB_Augustinus;
		} else if (pC.contains("GBIB, Anjou")) {
			dgtTypes = DGTConvConstants.DGT_GBIB_Anjou;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_GBIB_Anjou;
		} else if (pC.contains("UBG, Flandrica")) {
			dgtTypes = DGTConvConstants.DGT_UBG_Flandrica;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_UBG_Flandrica;
		} else if (pC.contains("UA, Flandrica")) {
			dgtTypes = DGTConvConstants.DGT_UA_Flandrica;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_UA_Flandrica;
		} else if (pC.contains("EHC, Flandrica")) {
			dgtTypes = DGTConvConstants.DGT_EHC_Flandrica;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_EHC_Flandrica;
		} else if (pC.contains("PBL, Flandrica")) {
			dgtTypes = DGTConvConstants.DGT_PBL_Flandrica;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_PBL_Flandrica;
		} else if (pC.contains("OBB, Flandrica")) {
			dgtTypes = DGTConvConstants.DGT_OBB_Flandrica;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_OBB_Flandrica;
		} else if (pC.contains("BIBC, Flandrica")) {
			dgtTypes = DGTConvConstants.DGT_BIBC_Flandrica;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_Flandrica;
		} else if (pC.contains("CRKC,standaardcollectie")) {
			dgtTypes = DGTConvConstants.DGT_CRKC_STANDAARD;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_CRKC_STANDAARD;
		} else if (pC.contains("CAG, standaardcollectie")) {
			dgtTypes = DGTConvConstants.DGT_CAG_STANDAARD;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_CAG_STANDAARD;
		} else if (pC.contains("KADOC,Sint-Lucas")) {
			dgtTypes = DGTConvConstants.DGT_KADOC_SINT_LUCAS;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_KADOC_Sint_Lucas;
		} else if (pC.contains("KADOC,Capronnier")) {
			dgtTypes = DGTConvConstants.DGT_KADOC_Capronnier;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_KADOC_Capronnier;
		} else if (pC.contains("ETD TM_TEST")) {
			dgtTypes = DGTConvConstants.DGT_ETD_TM_TEST;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_ETD_TM_TEST;
		} else if (pC.contains("TRAJECTA")) {
			dgtTypes = DGTConvConstants.DGT_KADOC_Trajecta;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_KADOC_Trajecta;
		} else if (pC.contains("BIBC, Popp")) {
			dgtTypes = DGTConvConstants.DGT_BIBC_POPP;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_POPP;
		} else if ((pB != null && pB.contains("DO_MIGRATIE_TEXTURA")))   {
			dgtTypes = DGTConvConstants.DGT_COLL_TEXTURA;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_COLL_TEXTURA;
		} else if ((pB != null && pB.contains("BIBLIOTHECA")))   {
			dgtTypes = DGTConvConstants.DGT_GBIB_BIF;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_GBIB_BIF;
		} else if ((pB != null && pB.contains("DO_MIGRATIE_STEPHAN_TILED")))   {
			dgtTypes = DGTConvConstants.DGT_STEPHAN_TILED;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_STEPHAN_TILED;
		} else if (pC.contains("UB, Verwilghen"))   {
			dgtTypes = DGTConvConstants.DGT_UB_VERWILGHEN;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_UB_VERWILGHEN;
		} else if (pC.contains("CBA, MilitaireVerkenningen")) {
			dgtTypes = DGTConvConstants.DGT_CBA_MILITAIRE_VERKENNING;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_CBA_MILITAIRE_VERKENNING;
		} else if ((pC.contains("BIBC, Prentbriefkaarten")) &&	(label.substring(0,9).contains("DIGI_0020")))  {
			dgtTypes = DGTConvConstants.DGT_BIBC_BRIEFKAART;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_BRIEFKAART;
		} else if (pC.contains("KUL, Egyptologie")) {
			dgtTypes = DGTConvConstants.DGT_KUL_EGYPTOLOGIE;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_KUL_EGYPTOLOGIE;
		} else if (pC.contains("RBINS, EUinside")) {
			dgtTypes = DGTConvConstants.DGT_RBINS_EUINSIDE;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_RBINS_EUINSIDE;
		} else if (pC.contains("BIBC, Multispectraal")) {
			dgtTypes = DGTConvConstants.DGT_BIBC_MULTISPECTRAAL;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_BIBC_MULTISPECTRAAL;
		} else if (pC.contains("2Bergen, Mondgezondheidswetenschappen")) {
			dgtTypes = DGTConvConstants.DGT_2BERGEN_MOND;
			dgtEntityTypes = DGTConvConstants.DGT_ENTITY_2BERGEN_MOND;
		} else {
			dgtEntityTypes = null;
		}
		return dgtEntityTypes;
	}


	private static boolean sortFileElm(edu.harvard.hul.ois.mets.FileGrp metsEl,IEParser ie,List<FileGrp> fGrpList,TestDBOracle db,ExtFileGrp extFileGrp) {
		
		FileGrp fGrp = null;
		boolean jmpToNextPID = false;
		Integer z = 0;
		List fileCnt = null;
		String filelabel = null;
		String pid_file = null;
		String filenaam="";
		String dmdId = null;
		Integer fileNr = null;


		
		try {
		fGrp = null;
		List cnt = metsEl.getContent();
		for (int k=0;k<cnt.size();k++){
			int l;
			Object object = cnt.get(k);
			HashMap<Integer, FileInfo> ordFileList = new HashMap();
			FileInfo fileInfo;
			edu.harvard.hul.ois.mets.File file;
			dmdId = null;
			if (object.getClass().equals(edu.harvard.hul.ois.mets.File.class)) {
				file = (edu.harvard.hul.ois.mets.File)object;
				if (((file.getUSE() != null && file.getUSE() != ""))) {
					Map hashMap = file.getDMDID();
					
					Set set = hashMap.keySet();
					if (!set.isEmpty()){ 
						dmdId = (String)set.toArray()[0].toString();
						set = null;
					}
					hashMap = null;
					fileCnt = file.getContent();
					
					for ( l=0;l< fileCnt.size();l++) {
						Object fileCntObj = fileCnt.get(l);
						if (fileCntObj.getClass().equals(FLocat.class)) {
							MetsElement fLocat = (FLocat)fileCntObj;
							Attributes attrs = fLocat.getAttributes();
							
							for (int m=0;m<attrs.size();m++){
								
								Attribute attr = attrs.get(m);
								String filelink = attr.getValue();
								Integer lastindex = filelink.lastIndexOf('=');
								if (lastindex != -1) {
									pid_file = filelink.substring(lastindex+1);
									
									//indien geen filenaam doorgaan met volgende file
									if ((filenaam = db.getFilename(pid_file)) == null) 
										{
											m=attrs.size();
											l = fileCnt.size();
											jmpToNextPID = true;
											continue;
										} else if (filenaam.equals("MA_proef_Geschiedenis_juni_2009_a_s0170128_Masterpaper_definitief.doc")) {
											m=attrs.size();
											continue;
										}
									String filepath = db.getFilepath(pid_file);
									if (prop.getProperty("filesRootFolder").contains("M")) filepath = filepath.replace("/nas", "M:");
						        	fis = new FileInputStream(new File(filepath));
							        byte buf[]=new byte[1];
							        if (fis.read(buf)>0) {
									
										filenaam = filenaam.replace(' ', '_');
										filenaam = filenaam.replace('[', '_');
										filenaam = filenaam.replace(']', '_');
										filenaam = filenaam.replace('/', '_');
										try {
											fileNr = Integer.valueOf(file.getID());
										} catch (Exception e) {
											m=attrs.size();
											jmpToNextPID = true;
											continue;										
										}
	
							            extFileGrp.setFileInfo(filenaam, pid_file, file, fileNr,dmdId,file.getUSE() );
							            
							            fGrp = addFileGroup(metsEl.getUSE(),dgtTypes,ie,filenaam,extFileGrp,pid_file);
							        } else {
										m=attrs.size();
							        }
								} else {
									filelink = null;
									continue;
								}
							}
							attrs = null;
						}
						fileCntObj = null;
					}
					file = null;
				}
			}
		}
		fileCnt = null;		
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return jmpToNextPID;
	}
				
	private static boolean handleFileElm (IEParser ie,FileGrp fGrp,TestDBOracle db,HashMap ordFileList) {
		boolean jmpToNextPID = false;
	
		try {

								String filepath="";
								String pid_file;
								String filenaam;
								String dmdId;
								FileInfo fileInfo;
								String filelabel;
								edu.harvard.hul.ois.mets.File file;
						        Map<Integer, FileInfo> map = new TreeMap<Integer, FileInfo>(ordFileList); 
						         Set set2 = map.entrySet();
						         Iterator iterator = set2.iterator();
						         Integer key;
						         while (iterator.hasNext()) {
						        	Map.Entry me = (Map.Entry)iterator.next();
									key = (Integer)me.getKey();
									fileInfo = (FileInfo)ordFileList.get(key);
						    		pid_file = fileInfo.getPID();
						    		file = fileInfo.getFile();
						    		filenaam = fileInfo.getNaam();
						    		dmdId = fileInfo.getDMDID();
						    		
									filepath = db.getFilepath(pid_file);
									
											if (fGrp != null ) {
												
												String nameNoExt = filenaam; 
												if (filenaam.contains(".")) {
													nameNoExt = filenaam.substring(0,filenaam.lastIndexOf('.'));
												}
							            	
												if (putFileToRootFolder(filepath,filenaam, ((dgtTypes.equals(DGTConvConstants.DGT_EAD)) && ("VIEW".equals(fileInfo.getUSE())))) == false ) {
													jmpToNextPID = true;
													continue;
												} 

								            	filelabel = db.getFilelabel(pid_file);
									            if (dgtTypes.equals(DGTConvConstants.DGT_EAD)) {
									            	if ("PRIVATE".equals(db.getStatus())) {
									            		filelabel = filelabel.concat("_oud");
									            	}
									            }
									            												
									            //add file and dnx metadata on file
									            FileType fileType = ie.addNewFile(fGrp, file.getMIMETYPE(), filenaam, nameNoExt + key);
									            fileType.setGROUPID(filelabel);
									            if (dmdId != null) {
													if (db.IsDMDIDOnFileLevel(pid,dmdId))	{
											            ArrayList al1 = new ArrayList();
										            	al1.add("file"+dmdId+"-dmd");
										            	fileType.setDMDID(al1);
													}
									            } 
									            
												String userid = "";
												String clob;
												if ((clob = db.getAccessRights(pid_file)) != "") {
													userid  = clob.substring((clob.indexOf("<val1>")+6),clob.indexOf("</val1>"));
												}
									            
									            // add dnx - A new DNX is constructed and added on the file level
									            DnxDocument dnx = ie.getFileDnx(fileType.getID());
									            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);

												fileDocumentHelper.getGeneralFileCharacteristics().setLabel(filelabel);
									            fileDocumentHelper.getGeneralFileCharacteristics().setFileOriginalPath(filepath);
									            fileDocumentHelper.getGeneralFileCharacteristics().setFileEntityType(dgtEntityTypes);

											    if ((userid.toLowerCase()).contains("dokskhlim")) {
												    DnxSection dnxieSAR2 = dnx.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
												    DnxSectionRecord dnxieSRAR2 = dnxieSAR2.createRecord();
											    	dnxieSRAR2.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_ETD_KHLIM"));
												    fileDocumentHelper.getGeneralFileCharacteristics().setRecord(dnxieSRAR2);
											    }
									            ie.setFileDnx(fileDocumentHelper.getDocument(), fileType.getID());

									            dnx = null;
									            fileDocumentHelper = null;
									            fileType = null;
											}
						    			}	
						         
						            	ie.generateChecksum(prop.getProperty("filesRootFolder"), Enum.FixityType.MD5.toString());
						            	ie.updateSize(prop.getProperty("filesRootFolder"));
						            	
						            	
	}
	catch (Exception e) {
		e.printStackTrace();
		return false;
	}				
		return true;
					}
	
	public static DublinCore generateDC(IEParser ie,List dcelements)
	{
		DublinCore dc = ie.getDublinCoreParser();
		
		for (int l=0;l<dcelements.size();l++) {
			if (dcelements.get(l).getClass().equals(Any.class)) {
				Any dcelm = (Any)dcelements.get(l);
				List<PCData> dcData = dcelm.getContent();
				for (int m=0;m<dcData.size();m++) {
					PCData dcDatum = dcData.get(m);
					dc.addElement(dcelm.getQName(), (String)dcData.get(m).getContent().get(0).toString());
				}
			}
		}
		dc.addElement("dc:type",dgtEntityTypes);
		if ((dc.getTitle() == null) || (dc.getTitle() == "")) {
			if ((dc.getDcValue("subject") == null) || (dc.getDcValue("subject") == "")) {
				dc.addElement("dc:title","Zonder titel");
			} else {
				dc.addElement("dc:title", dc.getDcValue("subject"));
			}
		}
		return dc;
	}	
	
	
	/**
	 * Full Flow Example with all stages to create and make a Deposit.
	 *
	 */
	public static void main(String[] args) {

		
    	prop = new Properties();
		HashMap<String,edu.harvard.hul.ois.mets.FileGrp> fileGroep = new HashMap();
		edu.harvard.hul.ois.mets.FileGrp metsElem = null;
    	
		DOMConfigurator.configure("log4j.xml");
		org.apache.log4j.helpers.LogLog.setQuietMode(true);
		String filelabel="";

		try {
    		prop.load(new FileInputStream("dgtconv.properties"));
			// clean rosetta input stream directory
			FileUtil.cleanDirectory(prop.getProperty("filesRootFolder"));
			
			File streamRosettaMetsDir = new File(prop.getProperty("folder_on_working_machine") + prop.getProperty("subDirectoryName") +"/content");
			File[] rosettaMetsfiles = streamRosettaMetsDir.listFiles();
			while (metsCounter < rosettaMetsfiles.length) {
				String ext = rosettaMetsfiles[metsCounter].getName();
				if (ext.contains("xml") == true) {
					FileUtil.forceDelete(new File(rosettaMetsfiles[metsCounter].getAbsolutePath()));
				}
				metsCounter++;
			}
			//create oracle connection 
			TestDBOracle db = new TestDBOracle();
			//create parser
			IEParser ie = null;//IEParserFactory.create();
			
			//create IEToMets
			iEToMets = new IEToMets();
			File streamMetsDir = new File(prop.getProperty("metsRootFolder"));
			File[] metsfiles = streamMetsDir.listFiles();
			
			// add fileGrp
			FileGrp fGrp = null;
			ExtFileGrp extFileGrp = null;			
			List<FileGrp> fGrpList = new ArrayList<FileGrp>();

		    String pattern = "MMddyyyy_HHmmss";
		    SimpleDateFormat format = new SimpleDateFormat(pattern);
			String df = format.format(new Date());
					 			
			rosettaLink = new MyExcel("dgt_rosetta"+df+".xls");
			rosettaLink.addCaption(0, 0, "PID");
			rosettaLink.addCaption(1, 0, "SIP");
			rosettaLink.addCaption(2, 0, "IE");

		// read mets file
 			Mets dtlmets=null;
			HashMap<String,HashSet<String>> fileLabel = new HashMap();
			HashMap<String,FileInfo> fileIdPID = new HashMap();
			
			metsCounter=0;

			boolean jmpToNextPID = false;

			while (metsCounter < metsfiles.length) {
				try {
			logger.info(metsfiles[metsCounter].getName());
			pid = metsfiles[metsCounter].getName().substring(0, metsfiles[metsCounter].getName().lastIndexOf("."));
			if (db.isChildObject(pid)) {
				logger.info(pid + " is een child object.");
				metsCounter++;
				continue;
			}
//			rosettaLink.addNumber(0, (metsFileCounter), Integer.valueOf(pid));
			rosettaLink.addNumber(0, (metsCounter+1), Integer.valueOf(pid));
			db.getFileParameters(pid);
			
			defineEntityType(db.getpB(),db.getpC(),db.getLabel(),db.getIngest());
			if (dgtEntityTypes ==  null) {
				logger.info(" Entity type undefined.");
				metsCounter++;
				continue;				
			} else {
				if (dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_BIBC_ELPRENT) ||
					dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_BIBC_Legerbode) || 
					dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_BIBC_Manuscripts) || 
					dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_BIBC_BOEK) || 
					dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_ETD_KUL) || 
					dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_BIBC_ExCathedra)) {
					complexElementThumbnail = db.getComplexElementThumbnail(pid);
				}
				logger.info("entity type : "+ dgtEntityTypes);
			}
			
			dtlmets = readMetsDtlFile(metsfiles[metsCounter].getAbsolutePath());
			logger.info(dtlmets.getLABEL());

			ie = null;
			ie = IEParserFactory.create();
			extFileGrp = new ExtFileGrp(ie,dtlmets.getLABEL());
			
			List contentList = dtlmets.getContent();
			boolean dcFlag = false;
			for (int i=0;i< contentList.size();i++) {
				
				Object obj = contentList.get(i).getClass();
				 
				if (obj.equals(MetsHdr.class)) {
					MetsHdr metsHdr = (MetsHdr)contentList.get(i); 
				}
				else if (obj.equals(FileSec.class)) {

					userid = "";
					String clob;
					if ((clob = db.getAccessRights(pid)) != "") {
						userid  = clob.substring((clob.indexOf("<val1>")+6),clob.indexOf("</val1>"));
						if (clob.contains("ip_range")) {
							ip_low = userid;
							ip_high = clob.substring((clob.indexOf("<val2>")+6),clob.indexOf("</val2>"));
						}
					}
					
				    DnxDocument dnxie = ie.getDnxParser();
				    DnxSection dnxieS = dnxie.createSection(DNXConstants.GENERALIECHARACTERISTICS.sectionId);
				    DnxSectionRecord dnxieSR = dnxieS.createRecord();
				    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.IEENTITYTYPE.sectionKeyId, "", "", dgtEntityTypes);
				    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.USERDEFINEDA.sectionKeyId, "", "", db.getpA());
				    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.USERDEFINEDB.sectionKeyId, "", "", db.getpB());
				    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.USERDEFINEDC.sectionKeyId, "", "", db.getpC());
				    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.STATUS.sectionKeyId, "", "", db.getStatus());

				    if (dgtEntityTypes == DGTConvConstants.DGT_ENTITY_COLL_TEXTURA) 
				    {
				    	DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnxie);
				    	String collectionId = db.getParentCollSip(pid);
				    	Collection coll =  fileDocumentHelper.new Collection();
				    	coll.setCollectionId(collectionId);
				    	List<Collection> collList = new ArrayList<Collection>();
				    	collList.add(coll);
			            fileDocumentHelper.setCollections(collList);
					    DnxSection dnxieSAR = dnxie.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSRAR = dnxieSAR.createRecord();
					    dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCADMIN"));
						if (dcFlag == false) { 
							DublinCore dc = ie.getDublinCoreParser();
							dc.addElement("dc:type",dgtEntityTypes);
							dc.addElement("dc:title",dtlmets.getLABEL());
							ie.setIEDublinCore(dc);
							dcFlag = true;
						}
				    }  						    
				    else if ((ip_low != "") && (ip_high != "")) {
					    DnxSection dnxieSAR = dnxie.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSRAR = dnxieSAR.createRecord();
					    if (DGTConvConstants.DGT_ENTITY_ETD_KUL.equals(dgtEntityTypes)) {
					    	dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "","", prop.getProperty("RST_ACCESS_RIGHTS_IP_RANGE_REGISTERED"));
					    } else if (DGTConvConstants.DGT_ENTITY_ETD_TMK.equals(dgtEntityTypes)) {
					    	if (userid != "") {
					    		dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "","", prop.getProperty("RST_AR_IP_RANGE_USER_TMK"));
					    	} else {
					    		dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "","", prop.getProperty("RST_ACCESS_RIGHTS_IP_RANGE_TMK"));
					    	}
					    } else if (DGTConvConstants.DGT_ENTITY_ETD_TMM.equals(dgtEntityTypes)) {
					    	if (userid != "") {
					    		dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "","", prop.getProperty("RST_AR_IP_RANGE_USER_TMM"));
					    	} else {
					    		dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "","", prop.getProperty("RST_ACCESS_RIGHTS_IP_RANGE_TMM"));
					    	}
					    } else if (DGTConvConstants.DGT_ENTITY_KADOC_Trajecta.equals(dgtEntityTypes)) {
					    	dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "","", prop.getProperty("RST_ACCESS_RIGHTS_TRAJECTA"));
					    } else {
					    	String acc = prop.getProperty("RST_AR_IP_RANGE_KUL");
					    	dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "","", acc);
					    }
				    } else if ((userid != "") 
				    		&& ((dgtEntityTypes == DGTConvConstants.DGT_ENTITY_WEBDOC) 
				    		|| (dgtEntityTypes == DGTConvConstants.DGT_ENTITY_ARCHIVAL_IMAGES)
				    		|| (dgtEntityTypes == DGTConvConstants.DGT_ENTITY_ETD_KUL)
				    		|| (dgtEntityTypes == DGTConvConstants.DGT_ENTITY_ETD_TMK)
				    		|| (dgtEntityTypes == DGTConvConstants.DGT_ENTITY_ETD_TMM)
				    		|| (dgtEntityTypes == DGTConvConstants.DGT_ENTITY_ETD_KHL)
				    		|| (dgtEntityTypes == DGTConvConstants.DGT_ENTITY_ETD_KHLIM)
				    		|| (dgtEntityTypes == DGTConvConstants.DGT_ENTITY_ETD_KHBO)
				    		|| (dgtEntityTypes == DGTConvConstants.DGT_ENTITY_ETD_KATHO)
				    		|| (dgtEntityTypes == DGTConvConstants.DGT_ENTITY_ETD_TM_TEST)
				    		|| (dgtEntityTypes == DGTConvConstants.DGT_ENTITY_CRKC_STANDAARD)
				    		)) {
					    DnxSection dnxieSAR2 = dnxie.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSRAR2 = dnxieSAR2.createRecord();
					    if ((userid.toLowerCase()).contains("cvbadmin")) {
					    	dnxieSRAR2.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_CVBADMIN"));
					    } else if ((userid.toLowerCase()).contains("kadocadmin")) {
					    	dnxieSRAR2.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_KADOCADMIN"));
					    } else if ((userid.toLowerCase()).contains("etdadmin")) {
					    	dnxieSRAR2.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_ETDADMIN"));
					    } else if ((userid.toLowerCase()).contains("doksadmin")) {
					    	dnxieSRAR2.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_ETD_NO_KUL_ADMIN"));
					    } else if ((userid.toLowerCase()).contains("dokskhlim")) {
					    	dnxieSRAR2.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_ETD_KHLIM"));
					    } else if ((userid.toLowerCase()).contains("crkcadmin")) {
					    	dnxieSRAR2.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_CRKCADMIN"));
					    }
				    } else {
					    DnxSection dnxieSAR = dnxie.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
					    DnxSectionRecord dnxieSRAR = dnxieSAR.createRecord();
					    if (
					    		("ETD_TMK".equals(dgtEntityTypes)) || 
					    		("ETD_KATHO".equals(dgtEntityTypes)) || 
					    		("ETD_KHBO".equals(dgtEntityTypes)) || 
					    		("ETD_TMM".equals(dgtEntityTypes)) || 
					    		("ETD_KHLIM".equals(dgtEntityTypes)) || 
					    		("ETD_KHL".equals(dgtEntityTypes)) ||
					    		("DGT_ETD_TM_TEST".equals(dgtEntityTypes)) 					    		
					    	)
					    {
					    	dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_EVERYONE_COPYRIGHT"));
				    	} else {
				    		dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", prop.getProperty("RST_ACCESS_RIGHTS_EVERYONE"));
				    	}
				    }
				    
				    ie.setIeDnx(dnxie);	
					
					List<String> insertedFiles = new ArrayList<String>();
					FileSec fileSec = (FileSec)contentList.get(i); 
					List el = fileSec.getContent();
					Integer z = 0;
					List fileCnt = null;
					edu.harvard.hul.ois.mets.FileGrp metsEl = null;
					for (Integer j=0;j<el.size();j++) {
						metsEl = (edu.harvard.hul.ois.mets.FileGrp)el.get(j);
						
						if ("VIEW".equals(metsEl.getUSE().toUpperCase())) { 
							fileGroep.put("VIEW"+z, metsEl);
							z++;
						} else if (!("THUMBNAIL".equals(metsEl.getUSE().toUpperCase()))) {
							fileGroep.put(metsEl.getUSE(), metsEl);
						}
					}
					if ((!fileGroep.containsKey("ARCHIVE")) ){//&& (fileGroep.size() == 1)) {
						if (fileGroep.containsKey("VIEW0")) {
							metsEl = fileGroep.get("VIEW0");
							metsEl.setUSE("ARCHIVE");
							fileGroep.put("ARCHIVE", metsEl);
							fileGroep.remove("VIEW0");
						} else if (fileGroep.containsKey("VIEW_MAIN")) {
							metsEl = fileGroep.get("VIEW_MAIN");
							metsEl.setUSE("ARCHIVE");
							fileGroep.put("ARCHIVE", metsEl);
							fileGroep.remove("VIEW_MAIN");
						} 
					}
					insertedFiles.clear();
				} else if (obj.equals(StructMap.class)) {
					
				} else if (obj.equals(AmdSec.class)) {

				}
			
			}
			
			//loop filegroep hashmap af in de juiste orde :
			//  VIEW_MAIN,view,(view,..),ARCHIVE,THUMBNAIL?
			int index =0;
			String pres;
			metsElem = fileGroep.get("VIEW_MAIN");
			if (metsElem != null) {
				jmpToNextPID = sortFileElm(metsElem,ie,fGrpList,db,extFileGrp);
			}
			pres = "VIEW" + index++;
			metsElem = fileGroep.get(pres);
			
			while (jmpToNextPID == false && metsElem != null) {
				jmpToNextPID = sortFileElm(metsElem,ie,fGrpList,db,extFileGrp);
				pres = "VIEW" + index++;
				metsElem = fileGroep.get(pres);
			}
			if (jmpToNextPID == false) {
				metsElem = fileGroep.get("THUMBNAIL");
				if (metsElem != null) {
					jmpToNextPID = sortFileElm(metsElem,ie,fGrpList,db,extFileGrp);
				}
			}			
			if (jmpToNextPID == false) {
				metsElem = fileGroep.get("INDEX");
				if (metsElem != null) {
					jmpToNextPID = sortFileElm(metsElem,ie,fGrpList,db,extFileGrp);
				}
			}
			if (jmpToNextPID == false) {
				metsElem = fileGroep.get("ARCHIVE");
				if (metsElem != null) {
					jmpToNextPID = sortFileElm(metsElem,ie,fGrpList,db,extFileGrp);
				}
			}
			
			if (jmpToNextPID == true) {
				jmpToNextPID = false;
				FileUtil.forceDelete(new File(metsfiles[metsCounter].getAbsolutePath()));
				FileUtil.cleanDirectory(prop.getProperty("filesRootFolder"));
				rosettaLink.SaveExcel();
			} else {

				// loop dmdsec gedeelte af voor dc data
				for (int i=0;i< contentList.size();i++) {
					Object obj = contentList.get(i).getClass();
					if (obj.equals(DmdSec.class)) {
						DmdSec dmdSec = (DmdSec)contentList.get(i);
						List<MdWrap> dmdConts = dmdSec.getContent();
						for (int j=0;j<dmdConts.size();j++) {
							MdWrap mdWrap = dmdConts.get(j);
							List<XmlData> xmlDatas = mdWrap.getContent();
							
							for (int k=0;k<xmlDatas.size();k++) {
								XmlData xmlData = xmlDatas.get(k); 
								List<Any> anies = xmlData.getContent();
								Any any = anies.get(0);
								List dcelements;
								if ("records".equals(any.getLocalName())) {
									List anies2 = any.getContent();
									int ind = 0;
									while (!(((anies2.get(ind)).getClass()).equals(Any.class))){ind++;};
									Any any2 = (Any)(anies2.get(ind));
									dcelements = any2.getContent();
								} else {
									dcelements = any.getContent();
								}
								
								DublinCore dc = generateDC(ie, dcelements);
								if (db.IsDMDIDOnFileLevel(pid,dmdSec.getID()))	{
									ie.setDublinCore(dc, "file"+dmdSec.getID());
								} else { 
									ie.setIEDublinCore(dc);
								}
								dcFlag = true;
							}
						}
					}
				}
				// end loop dmdsec
				extFileGrp.clearInfo();
				if (extFileGrp.isFileGrpDerivativeFlag()) {
					handleFileElm(ie, extFileGrp.getFileGrpDerivative(), db, extFileGrp.getOrdFileDerivativeList());
					ie.generateStructMap(extFileGrp.getFileGrpDerivative(),"Lage kwaliteit", "Inhoudsopgave");
				}
	    		if (extFileGrp.isFileGrpPreservationFlag()) {
					handleFileElm(ie, extFileGrp.getFileGrpPreservation(), db, extFileGrp.getOrdFilePreservationList());
	    			ie.generateStructMap(extFileGrp.getFileGrpPreservation(),"Archiefkopie", "Inhoudsopgave");
	    		}
	    		if (extFileGrp.isFileGrpModifiedFlag()) {
					handleFileElm(ie, extFileGrp.getFileGrpModified(), db, extFileGrp.getOrdFileModifiedList());
	    			ie.generateStructMap(extFileGrp.getFileGrpModified(),"Hoge kwaliteit", "Inhoudsopgave");
	    		}
	    		if (extFileGrp.isFileGrpDerivativeXMLMDFlag()) {
					handleFileElm(ie, extFileGrp.getFileGrpDerivativeXMLMD(), db, extFileGrp.getOrdFileDerivativeXMLMDList());
	    			ie.generateStructMap(extFileGrp.getFileGrpDerivativeXMLMD(),"Structuur", "Inhoudsopgave");
	    		}
	    		if (extFileGrp.isFileGrpDerivativePdfFlag()) {
					handleFileElm(ie, extFileGrp.getFileGrpDerivativePdf(), db, extFileGrp.getOrdFileDerivativePdfList());
	    			ie.generateStructMap(extFileGrp.getFileGrpDerivativePdf(),"PDF", "Inhoudsopgave");
	    		}
	    		if (extFileGrp.isFileGrpDerivativeThmbnlFlag()) {
					handleFileElm(ie, extFileGrp.getFileGrpDerivativeThmbnl(), db, extFileGrp.getOrdFileDerivativeThmbnlList());
	    			ie.generateStructMap(extFileGrp.getFileGrpDerivativeThmbnl(),"Miniatuurweergave", "Inhoudsopgave");
	    		}
	    		if (extFileGrp.isFileGrpDerivativeHtmlFlag()) {
					handleFileElm(ie, extFileGrp.getFileGrpDerivativeHtml(), db, extFileGrp.getOrdFileDerivativeHtmlList());
	    			ie.generateStructMap(extFileGrp.getFileGrpDerivativeHtml(),"Algemeen overzicht", "Inhoudsopgave");
	    		}
	    		if (extFileGrp.isFileGrpDerivativeIndexFlag()) {
					handleFileElm(ie, extFileGrp.getFileGrpDerivativeIndex(), db, extFileGrp.getOrdFileDerivativeIndexList());
	    			ie.generateStructMap(extFileGrp.getFileGrpDerivativeIndex(),"Index", "Inhoudsopgave");
	    		}
				
				iEToMets.setSubDirectoryName(prop.getProperty("subDirectoryName"));
				
				if (iEToMets.CreateAndIngestMets(fGrpList, ie,dtlmets.getLABEL()) == true) {
					FileUtil.forceDelete(new File(metsfiles[metsCounter].getAbsolutePath()));
					FileUtil.cleanDirectory(prop.getProperty("filesRootFolder"));
					extFileGrp.clean();
					fGrpList.clear();
					fileGroep.clear();					
					rosettaLink.SaveExcel();
					String partition = dgtTypes.equals(DGTConvConstants.DGT_ETD_KUL) ? "partitiona" : "partitionb";
					db.setDone(partition, pid);
				} else {
				
					FileUtil.cleanDirectory(prop.getProperty("filesRootFolder"));
					
					extFileGrp.clean();
					fGrpList.clear();
					fileGroep.clear();
					rosettaLink.SaveExcel();
				}
				
			}
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
			metsCounter++;
			}
			db.closeConn();
			rosettaLink.closeExcel();
			
		} catch (Exception e) {
			e.printStackTrace();
			try{
			rosettaLink.closeExcel();
			} catch (IOException ioe) {
				
			}
			return;
		}
	}
}