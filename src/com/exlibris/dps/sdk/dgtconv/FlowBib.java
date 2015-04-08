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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.exlibris.core.sdk.consts.Enum;
import com.exlibris.core.sdk.formatting.DublinCore;
import com.exlibris.core.sdk.parser.IEParserException;
import com.exlibris.core.sdk.utils.FileUtil;
import com.exlibris.digitool.common.dnx.DNXConstants;
import com.exlibris.digitool.common.dnx.DnxDocument;
import com.exlibris.digitool.common.dnx.DnxDocumentHelper;
import com.exlibris.digitool.common.dnx.DnxSection;
import com.exlibris.digitool.common.dnx.DnxSectionRecord;
import com.exlibris.digitool.common.dnx.DnxDocumentHelper.Collection;
import com.exlibris.dps.sdk.deposit.IEParser;
import com.exlibris.dps.sdk.deposit.IEParserFactory;

import edu.harvard.hul.ois.mets.AmdSec;
import edu.harvard.hul.ois.mets.DmdSec;
import edu.harvard.hul.ois.mets.FLocat;
import edu.harvard.hul.ois.mets.FileSec;
import edu.harvard.hul.ois.mets.MdWrap;
import edu.harvard.hul.ois.mets.Mets;
import edu.harvard.hul.ois.mets.MetsHdr;
import edu.harvard.hul.ois.mets.StructMap;
import edu.harvard.hul.ois.mets.XmlData;
import edu.harvard.hul.ois.mets.helper.Any;
import edu.harvard.hul.ois.mets.helper.MetsElement;
import edu.harvard.hul.ois.mets.helper.MetsReader;
import edu.harvard.hul.ois.mets.helper.PCData;
import edu.harvard.hul.ois.mets.helper.parser.Attribute;
import edu.harvard.hul.ois.mets.helper.parser.Attributes;
import excel.writer.MyExcel;
import gov.loc.mets.FileType;
import gov.loc.mets.MetsType.FileSec.FileGrp;

public class FlowBib {

	private static String userid = "";
	private static Logger logger = Logger.getLogger(FlowBib.class);
	private static File inputFile;
	private static FileInputStream fis;
	private static IEToMets iEToMets;
	private static String complexElementThumbnail = "0";
	private static String pid = null;
	private static String ip_low="",ip_high="";

	
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

	private static DublinCore  generateDC(IEParser ie,String pid,TestDBOracle db) {
		DublinCore dc = ie.getDublinCoreParser();
    	
    	
    	//get dublincore info
		int dc_index = 0;
		int dc_lastindex = 0;
		String clobRest;
		String element;
		String clob = null;
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
		}
		return dc;
	}
	

	private static boolean handleFileElement(edu.harvard.hul.ois.mets.FileGrp metsEl,IEParser ie,List<FileGrp> fGrpList,TestDBOracle db,ExtFileGrp extFileGrp) {
		
		FileGrp fGrp = null;
		boolean jmpToNextPID = false;
		Integer z = 0;
		List fileCnt = null;
		String filelabel = null;
		String pid_file = null;
		String dmdId = null;
		try {
		fGrp = null;
		List cnt = metsEl.getContent();
		for (int k=0;k<cnt.size();k++){
			Object object = cnt.get(k);
			if (object.getClass().equals(edu.harvard.hul.ois.mets.File.class)) {
				edu.harvard.hul.ois.mets.File file = (edu.harvard.hul.ois.mets.File)object;
				if (metsEl.getUSE().toLowerCase().equals("reference")) {
					Map hashMap = file.getDMDID();
					
					Set set = hashMap.keySet();
					if (!set.isEmpty()){ 
						dmdId = (String)set.toArray()[0].toString();
						set = null;
					}
					hashMap = null;
					fileCnt = file.getContent();
					for (int l=0;l< fileCnt.size();l++) {
						Object fileCntObj = fileCnt.get(l);
						if (fileCntObj.getClass().equals(FLocat.class)) {
							FLocat fLocat = (FLocat)fileCntObj;

							String filelink = fLocat.getXlinkHref();
							
								Integer lastindex = filelink.lastIndexOf("streams/") + ("streams/").length();
								if (lastindex != -1) {
									String filenaam=filelink.substring(lastindex);
									String filepath="";
									pid_file = db.getPIDOfFile(filenaam, pid);
									filenaam = filenaam.replace(' ', '_');
									filenaam = filenaam.replace('[', '_');
									filenaam = filenaam.replace(']', '_');
									filenaam = filenaam.replace('/', '_');

										filepath = db.getFilepath(pid_file);

									
							            	fGrp = FlowExample.addFileGroup(metsEl.getUSE(),FlowExample.dgtTypes,ie,filenaam,extFileGrp,pid_file);
											if (fGrp != null ) {
												
												String nameNoExt = filenaam; 
												if (filenaam.contains(".")) {
													nameNoExt = filenaam.substring(0,filenaam.lastIndexOf('.'));
												}
												String extension = filenaam.substring(filenaam.indexOf('.')+1).toLowerCase();
												fGrpList.add(fGrp);
								            	
												if (!putBibFileToRootFolder(filepath,filenaam, extension)) {
													l = fileCnt.size();
													jmpToNextPID = true;
													continue;
												} 

								            	filelabel = db.getFilelabel(pid_file);
									            												
									            //add file and dnx metadata on file
									            FileType fileType = ie.addNewFile(fGrp, file.getMIMETYPE(), filenaam, nameNoExt);
									            fileType.setGROUPID(String.valueOf(file.getSEQ()));

									            // add dnx - A new DNX is constructed and added on the file level
									            DnxDocument dnx = ie.getFileDnx(fileType.getID());
									            DnxDocumentHelper fileDocumentHelper = new DnxDocumentHelper(dnx);

												fileDocumentHelper.getGeneralFileCharacteristics().setLabel(filelabel);
									            fileDocumentHelper.getGeneralFileCharacteristics().setFileOriginalPath(filepath);
									            fileDocumentHelper.getGeneralFileCharacteristics().setFileEntityType(FlowExample.dgtEntityTypes);
									            ie.setFileDnx(fileDocumentHelper.getDocument(), fileType.getID());
												
									            dnx = null;
									            fileDocumentHelper = null;
									            fileType = null;
									            break;
											}
											
								}
								filelink = null;
						}
						fileCntObj = null;
					}
					fileCnt = null;
				}
			}
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return jmpToNextPID;
	}
	
	
	
	/**
	 * Full Flow Example with all stages to create and make a Deposit.
	 *
	 */
	public static void main(String[] args) {

		
    	FlowExample.prop = new Properties();
		HashMap<String,edu.harvard.hul.ois.mets.FileGrp> fileGroep = new HashMap();
		edu.harvard.hul.ois.mets.FileGrp metsElem = null;
    	
		DOMConfigurator.configure("log4j.xml");
		org.apache.log4j.helpers.LogLog.setQuietMode(true);
		String filelabel="";

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
			//create oracle connection 
			TestDBOracle db = new TestDBOracle();
			//create parser
			IEParser ie = null;//IEParserFactory.create();
			
			//create IEToMets
			iEToMets = new IEToMets();
			File streamMetsDir = new File(FlowExample.prop.getProperty("metsRootFolder"));
			File[] metsfiles = streamMetsDir.listFiles();
			
			// add fileGrp
			FileGrp fGrp = null;
			ExtFileGrp extFileGrp = null;			
			List<FileGrp> fGrpList = new ArrayList<FileGrp>();

		    String pattern = "MMddyyyy_HHmmss";
		    SimpleDateFormat format = new SimpleDateFormat(pattern);
			String df = format.format(new Date());
					 			
			FlowExample.rosettaLink = new MyExcel("dgt_rosetta"+df+".xls");
			FlowExample.rosettaLink.addCaption(0, 0, "PID");
			FlowExample.rosettaLink.addCaption(1, 0, "SIP");
			FlowExample.rosettaLink.addCaption(2, 0, "IE");

		// read mets file
 			Mets dtlmets=null;
			HashMap<String,HashSet<String>> fileLabel = new HashMap();
			HashMap<String,FileInfo> fileIdPID = new HashMap();
			
			FlowExample.metsCounter=0;

			boolean jmpToNextPID = false;

			while (FlowExample.metsCounter < metsfiles.length) {
				try {
			logger.info(metsfiles[FlowExample.metsCounter].getName());
			pid = metsfiles[FlowExample.metsCounter].getName().substring(0, metsfiles[FlowExample.metsCounter].getName().lastIndexOf("."));
			if (db.isChildObject(pid)) {
				logger.info(pid + " is een child object.");
				FlowExample.metsCounter++;
				continue;
			}
			FlowExample.rosettaLink.addNumber(0, (FlowExample.metsCounter+1), Integer.valueOf(pid));
			db.getFileParameters(pid);
			
			FlowExample.defineEntityType(db.getpB(),db.getpC(),db.getLabel(),db.getIngest());
			if (FlowExample.dgtEntityTypes ==  null) {
				logger.info(" Entity type undefined.");
				FlowExample.metsCounter++;
				continue;				
			} else {
				if (FlowExample.dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_BIBC_ELPRENT) ||
					FlowExample.dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_BIBC_Legerbode) || 
					FlowExample.dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_BIBC_Manuscripts) || 
					FlowExample.dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_BIBC_BOEK) || 
					FlowExample.dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_ETD_KUL) || 
					FlowExample.dgtEntityTypes.equals(DGTConvConstants.DGT_ENTITY_BIBC_ExCathedra)) {
					complexElementThumbnail = db.getComplexElementThumbnail(pid);
				}
				logger.info("entity type : "+ FlowExample.dgtEntityTypes);
			}
			
			dtlmets = FlowExample.readMetsDtlFile(metsfiles[FlowExample.metsCounter].getAbsolutePath());
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
				    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.IEENTITYTYPE.sectionKeyId, "", "", FlowExample.dgtEntityTypes);
				    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.USERDEFINEDA.sectionKeyId, "", "", db.getpA());
				    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.USERDEFINEDB.sectionKeyId, "", "", db.getpB());
				    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.USERDEFINEDC.sectionKeyId, "", "", db.getpC());
				    dnxieSR.createKey(DNXConstants.GENERALIECHARACTERISTICS.STATUS.sectionKeyId, "", "", db.getStatus());

				    DnxSection dnxieSAR = dnxie.createSection(DNXConstants.ACCESSRIGHTSPOLICY.sectionId);
				    DnxSectionRecord dnxieSRAR = dnxieSAR.createRecord();
				    dnxieSRAR.createKey(DNXConstants.ACCESSRIGHTSPOLICY.POLICYID.sectionKeyId, "", "", FlowExample.prop.getProperty("RST_ACCESS_RIGHTS_EVERYONE"));
				    
				    ie.setIeDnx(dnxie);	
					
					FileSec fileSec = (FileSec)contentList.get(i); 
					List el = fileSec.getContent();
					Integer z = 0;
					List fileCnt = null;
					edu.harvard.hul.ois.mets.FileGrp metsEl = null;
					
					for (Integer j=0;j<el.size();j++) {
						metsEl = (edu.harvard.hul.ois.mets.FileGrp)el.get(j);
						jmpToNextPID = handleFileElement(metsEl,ie,fGrpList,db,extFileGrp);
						if (jmpToNextPID == true) {
							FileUtil.forceDelete(new File(metsfiles[FlowExample.metsCounter].getAbsolutePath()));
							FileUtil.cleanDirectory(FlowExample.prop.getProperty("filesRootFolder"));
							FlowExample.rosettaLink.SaveExcel();
							j = el.size();
						}
					}
					if (jmpToNextPID == false) {
						DublinCore dc = generateDC(ie,pid,db);
						ie.setIEDublinCore(dc);
						dcFlag = true;
					} else {
						i = contentList.size();
					}
					
				} else if (obj.equals(StructMap.class)) {
					
				} else if (obj.equals(AmdSec.class)) {

				}
			}
			
			
				if (jmpToNextPID == false) {
	            	ie.generateChecksum(FlowExample.prop.getProperty("filesRootFolder"), Enum.FixityType.MD5.toString());
	            	ie.updateSize(FlowExample.prop.getProperty("filesRootFolder"));
					
		    		if (extFileGrp.isFileGrpDerivativeXMLMDFlag()) ie.generateStructMap(extFileGrp.getFileGrpDerivativeXMLMD(),"Structuur", "Inhoudsopgave");
		    		if (extFileGrp.isFileGrpDerivativeHtmlFlag()) ie.generateStructMap(extFileGrp.getFileGrpDerivativeHtml(),"Algemeen overzicht", "Inhoudsopgave");
		    		if (extFileGrp.isFileGrpModifiedFlag()) ie.generateStructMap(extFileGrp.getFileGrpModified(),"Hoge kwaliteit", "Inhoudsopgave");
		    		if (extFileGrp.isFileGrpPreservationFlag()) ie.generateStructMap(extFileGrp.getFileGrpPreservation(),"Archiefkopie", "Inhoudsopgave");
					
					iEToMets.setSubDirectoryName(FlowExample.prop.getProperty("subDirectoryName"));
					
					if (iEToMets.CreateAndIngestMets(fGrpList, ie,dtlmets.getLABEL()) == true) {
						FileUtil.forceDelete(new File(metsfiles[FlowExample.metsCounter].getAbsolutePath()));
						FileUtil.cleanDirectory(FlowExample.prop.getProperty("filesRootFolder"));
						extFileGrp.clean();
						fGrpList.clear();
						fileGroep.clear();					
						FlowExample.rosettaLink.SaveExcel();
						String partition = FlowExample.dgtTypes.equals(DGTConvConstants.DGT_ETD_KUL) ? "partitiona" : "partitionb";
						db.setDone(partition, pid);
					} else {
					
						FileUtil.cleanDirectory(FlowExample.prop.getProperty("filesRootFolder"));
						
						extFileGrp.clean();
						fGrpList.clear();
						fileGroep.clear();
						FlowExample.rosettaLink.SaveExcel();
					}
				}
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
			FlowExample.metsCounter++;
			}
			db.closeConn();
			FlowExample.rosettaLink.closeExcel();
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return;
	}
}
