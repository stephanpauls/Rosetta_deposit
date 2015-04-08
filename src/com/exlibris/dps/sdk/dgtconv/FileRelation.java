package com.exlibris.dps.sdk.dgtconv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import edu.harvard.hul.ois.mets.Div;
import edu.harvard.hul.ois.mets.FLocat;
import edu.harvard.hul.ois.mets.FileSec;
import edu.harvard.hul.ois.mets.Fptr;
import edu.harvard.hul.ois.mets.Mets;
import edu.harvard.hul.ois.mets.StructMap;
import edu.harvard.hul.ois.mets.helper.MetsElement;
import edu.harvard.hul.ois.mets.helper.MetsReader;
import edu.harvard.hul.ois.mets.helper.parser.Attribute;
import edu.harvard.hul.ois.mets.helper.parser.Attributes;
import excel.writer.MyExcel;
import excel.writer.WriteExcel;

public class FileRelation {

	private static String dgtEntityTypes;
	public static MyExcel fileLink;
	public static int metsCounter=0;
	private static Logger logger = Logger.getLogger(FileRelation.class);
	public static Properties prop; 
	
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
	
	/**
	 * Full Flow Example with all stages to create and make a Deposit.
	 *
	 */
	public static void main(String[] args) {

    	Properties prop = new Properties();
		DOMConfigurator.configure("log4j.xml");
		org.apache.log4j.helpers.LogLog.setQuietMode(true);
		
		TestDBOracle db = null;
		
		boolean vwmn_fl = false;
		boolean vw_pdf_fl = false;
		boolean vw_fl = false;
		boolean thmbnl_fl = false;
		boolean arch_fl = false;
		
		int vwmn_idx = 0;
		int vw_pdf_idx = 0;
		int vw_idx = 0;
		int thmbnl_idx = 0;
		int arch_idx = 0;
		try {

            //load a properties file
    		prop.load(new FileInputStream("dgtconv.properties"));
			
			//create oracle connection 
			db = new TestDBOracle();
			
			File streamMetsDir = new File(prop.getProperty("metsRootFolderFileRelation"));
			File[] metsfiles = streamMetsDir.listFiles();
			
		    String pattern = "MMddyyyy_HHMMSS";
		    SimpleDateFormat format = new SimpleDateFormat(pattern);
			String df = format.format(new Date());
		 			
			fileLink = new MyExcel("dgt_rosetta_filelink"+df+".xls");
			fileLink.addCaption(0, 0, "PID");

			
			// read mets file
			Mets dtlmets=null;
			HashMap<String,HashSet<String>> fileLabel = new HashMap();
			HashMap<String,FileInfo> fileIdPID = new HashMap();
			
			metsCounter=0;

			int fileLinkCtr = 1;

			while (metsCounter < metsfiles.length) {
				try {
					logger.info(metsfiles[metsCounter].getName());
					String pid = metsfiles[metsCounter].getName().substring(0, metsfiles[metsCounter].getName().lastIndexOf("."));
					if (db.isChildObject(pid)) {
						logger.info(pid + " is een child object.");
						metsCounter++;
						continue;
					}
					db.getFileParameters(pid);
					dgtEntityTypes = FlowExample.defineEntityType(db.getpC(),db.getpB(),db.getLabel(),db.getIngest());
					if (dgtEntityTypes ==  null) {
						logger.info(" Entity type undefined.");
						metsCounter++;
						continue;				
					} else {
						logger.info("entity type : "+ dgtEntityTypes);
					}
					
					dtlmets = readMetsDtlFile(metsfiles[metsCounter].getAbsolutePath());
					logger.info(dtlmets.getLABEL());
					logger.info(dtlmets.getTYPE());
					
					if ((dtlmets.getTYPE() != null ) && (dtlmets.getTYPE().toUpperCase().equals("COMPLEX"))) {
						fileLabel.clear();
						fileIdPID.clear();
						List contentList = dtlmets.getContent();
			
						FileInfo fileInfo = new FileInfo();
						
						try {
							//eerst structmap lezen om juiste fileid's te verzamelen
							for (int i=0;i< contentList.size();i++) {
								Object obj = contentList.get(i).getClass();
								if (obj.equals(FileSec.class)) {
									FileSec fileSec = (FileSec)contentList.get(i); 
									List el = fileSec.getContent();
									Integer z = 0;
									String pidFile = "";
									String fileLinkNaam="";
									for (Integer j=0;j<el.size();j++) {
										edu.harvard.hul.ois.mets.FileGrp metsEl = (edu.harvard.hul.ois.mets.FileGrp)el.get(j);
										fileLink.addCaption(j+1, 0, "related PID "+metsEl.getUSE());
									}
									for (Integer j=0;j<el.size();j++) {
										edu.harvard.hul.ois.mets.FileGrp metsEl = (edu.harvard.hul.ois.mets.FileGrp)el.get(j);
										List cnt = metsEl.getContent();
										for (int k=0;k<cnt.size();k++){
											Object object = cnt.get(k);
											if (object.getClass().equals(edu.harvard.hul.ois.mets.File.class)) {
												edu.harvard.hul.ois.mets.File file = (edu.harvard.hul.ois.mets.File)object;
												fileInfo.clear();
												if ((file.getUSE() != null && file.getUSE() != "")) {
													List fileCnt = file.getContent();
													for (int l=0;l< fileCnt.size();l++) {
														Object fileCntObj = fileCnt.get(l);
														if (fileCntObj.getClass().equals(FLocat.class)) {
															MetsElement fLocat = (FLocat)fileCntObj;
															Attributes attrs = fLocat.getAttributes();
															for (int m=0;m<attrs.size();m++){
																Attribute attr = attrs.get(m);
																String filelink = attr.getValue();
																Integer lastindex = filelink.lastIndexOf('=');
																if (lastindex != -1) {
																	pidFile = filelink.substring(lastindex+1);
																	fileInfo.setPID(pidFile);
																	fileLinkNaam = db.getFilename(pidFile);
																}
															}
														}
													}
													if ("VIEW".equals(file.getUSE())) {
														if (fileLinkNaam.toUpperCase().contains("VIEW_MAIN")) {
															vwmn_fl = true;
															fileInfo.setUSE("VIEW_MAIN");
														} else if (fileLinkNaam.substring(fileLinkNaam.indexOf('.')+1).toLowerCase().contains("pdf")){
															vw_pdf_fl = true;
															fileInfo.setUSE("VIEW_PDF");
														} else {
															vw_fl = true;
															fileInfo.setUSE(file.getUSE());
														} 
													} else {
														if ("VIEW_MAIN".equals(file.getUSE())) {
															vwmn_fl = true;
														}
														else if ("THUMBNAIL".equals(file.getUSE())) {
															thmbnl_fl = true;
														} 
														else if ("ARCHIVE".equals(file.getUSE())) {
															arch_fl = true;
														} 
														fileInfo.setUSE(file.getUSE());
													} 
													
													fileIdPID.put(file.getID(), fileInfo);
													fileInfo = new FileInfo();
													
												}
											}
										}
									}
								}
							}
							int kol = 1;
							if(vw_fl) {
								fileLink.addCaption(kol, 0, "related PID VIEW");
								vw_idx = kol++;
							}
							if (vwmn_fl) {
								fileLink.addCaption(kol, 0, "related PID VIEW MAIN");
								vwmn_idx = kol++;
							}
							if (arch_fl) {
								fileLink.addCaption(kol, 0, "related PID ARCHIVE");
								arch_idx = kol++;
							}
							if (thmbnl_fl) {
								fileLink.addCaption(kol, 0, "related PID THUMBNAIL");
								thmbnl_idx = kol++;
							}
							if (vw_pdf_fl) {
								fileLink.addCaption(kol, 0, "related PID VIEW PDF");
								vw_pdf_idx = kol++;
							}
							
						} catch (ClassCastException e) {
							logger.info("Label info voor files in digitool mets niet correct");
							break;
						}			
						
						try {
							//eerst structmap lezen om juiste fileid's te verzamelen
							fileLink.addNumber(0, fileLinkCtr, Integer.valueOf(pid));
							for (int i=0;i< contentList.size();i++) {
								Object obj = contentList.get(i).getClass();
								if (obj.equals(StructMap.class)) {
									StructMap structMap = (StructMap)contentList.get(i);
									List<Div> divH = structMap.getContent();
									for (int j=0;j<divH.size();j++) {
										Div div = (Div) divH.get(j);
										List<Div> fPtr = div.getContent();
										for (int k=0;k<fPtr.size();k++) {
											Div fptr = fPtr.get(k);
											List<Fptr> files = fptr.getContent();
											for (int l=0;l<files.size();l++) {
												Fptr file = files.get(l);
												Map map = file.getFILEID();
												Set keys = map.keySet();
												for (Iterator<String>iter = keys.iterator();iter.hasNext();) {
													String iterFId = iter.next();
													FileInfo info = fileIdPID.get(iterFId);
													if (info == null) {
														info = fileIdPID.get("0");
													}
													if (info != null) {
														String filePID = info.getPID() == null ? pid : info.getPID();
														if ("VIEW".equals(info.getUSE())) {
															fileLink.addNumber(vw_idx, fileLinkCtr, Integer.valueOf(filePID));
														} else if ("VIEW_MAIN".equals(info.getUSE())) {
															fileLink.addNumber(vwmn_idx, fileLinkCtr, Integer.valueOf(filePID));
														} else if ("ARCHIVE".equals(info.getUSE())) {
															fileLink.addNumber(arch_idx, fileLinkCtr, Integer.valueOf(filePID));
														} else if ("VIEW_PDF".equals(info.getUSE())) {
															fileLink.addNumber(vw_pdf_idx, fileLinkCtr, Integer.valueOf(filePID));
														} else if ("THUMBNAIL".equals(info.getUSE())) {
															fileLink.addNumber(thmbnl_idx, fileLinkCtr, Integer.valueOf(filePID));
														} else if ("ARCHIVE".equals(info.getUSE())) {
															fileLink.addNumber(arch_idx, fileLinkCtr, Integer.valueOf(filePID));
														}
													} 
												}
											}
											fileLinkCtr++;
										}
									}
								}
							}
						} catch (ClassCastException e) {
							logger.info("Label info voor files in digitool mets niet correct");
							break;
						}
					}
					fileLink.SaveExcel();
					metsCounter++;
				} catch (Exception e) {
					e.printStackTrace();
					metsCounter++;
				}
			}
			db.closeConn();
			fileLink.closeExcel();
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				db.closeConn();
				fileLink.closeExcel();
			} catch (IOException ioe) {
				logger.debug(ioe.getStackTrace());
			}
			return;
		}		
	}
}
