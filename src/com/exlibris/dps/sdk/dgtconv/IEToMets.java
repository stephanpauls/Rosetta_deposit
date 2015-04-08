package com.exlibris.dps.sdk.dgtconv;

import gov.loc.mets.DivType;
import gov.loc.mets.FileType;
import gov.loc.mets.MetsDocument;
import gov.loc.mets.StructMapType;
import gov.loc.mets.MetsDocument.Mets;
import gov.loc.mets.MetsType.FileSec.FileGrp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.exlibris.core.infra.svc.api.locator.WebServiceLocator;
import com.exlibris.core.sdk.formatting.DublinCore;
import com.exlibris.core.sdk.utils.FileUtil;
import com.exlibris.digitool.common.dnx.DnxDocument;
import com.exlibris.digitool.common.dnx.DnxDocumentHelper;
import com.exlibris.digitool.deposit.service.xmlbeans.DepData;
import com.exlibris.digitool.deposit.service.xmlbeans.DepositDataDocument;
import com.exlibris.digitool.deposit.service.xmlbeans.DepositResultDocument;
import com.exlibris.digitool.deposit.service.xmlbeans.DepositDataDocument.DepositData;
import com.exlibris.digitool.deposit.service.xmlbeans.DepositResultDocument.DepositResult;
import com.exlibris.dps.DepositWebServices_Service;
import com.exlibris.dps.ProducerWebServices;
import com.exlibris.dps.ProducerWebServices_Service;
import com.exlibris.dps.SipStatusInfo;
import com.exlibris.dps.SipWebServices_Service;
import com.exlibris.dps.sdk.deposit.DepositWebServices;
import com.exlibris.dps.sdk.deposit.IEParser;
import com.exlibris.dps.sdk.deposit.IEParserFactory;
import com.exlibris.dps.sdk.pds.PdsClient;
import com.exlibris.core.sdk.consts.Enum;

import com.exlibris.dps.sdk.dgtconv.DGTConvConstants;
import org.apache.log4j.Logger;

public class IEToMets {
	
	private String subDirectoryName;
	private static Logger logger = Logger.getLogger(IEToMets.class);
	private MetsDocument metsDoc;
	private Mets mets;
	private File ieXML=null;
	private XmlOptions opt;
	private PdsClient pds;
	private String pdsHandle;
	private ProducerWebServices producerWebServices;
	private String producerAgentId;
	private String xmlReply;
	private DepositDataDocument depositDataDocument;
	private DepositData depositData;
	private String producerId;
	private String retval;
	private DepositResultDocument depositResultDocument;
	private DepositResult depositResult;
	private int validationCtr = 0;
	
	public IEToMets() {
		try{
			producerWebServices = new ProducerWebServices_Service(new URL(FlowExample.prop.getProperty("PRODUCER_WSDL_URL")),new QName("http://dps.exlibris.com/", "ProducerWebServices")).getProducerWebServicesPort();
			producerAgentId = producerWebServices.getInternalUserIdByExternalId(FlowExample.prop.getProperty("userName"));
			logger.info("producerAgentId: " + producerAgentId);
			xmlReply = producerWebServices.getProducersOfProducerAgent(producerAgentId);
			depositDataDocument = DepositDataDocument.Factory.parse(xmlReply);
			depositData = depositDataDocument.getDepositData();
		} catch (MalformedURLException eurl) {
			logger.info(eurl.getMessage());
		} catch (XmlException ex ) {
			logger.info(ex.getMessage());
		}
	}
	
	public String getSubDirectoryName() {
		return subDirectoryName;
	}

	public void setSubDirectoryName(String subDirectoryName) {
		this.subDirectoryName = subDirectoryName;
	}

	public boolean CreateAndIngestMets(List<FileGrp> fGrpList,IEParser ie,String ieName ) {
		
		
		try {
		    metsDoc = MetsDocument.Factory.parse(ie.toXML());
		    mets = metsDoc.getMets();
		
		    //insert IE created in content directory
		    
		    ieName = ieName.replace(' ','_');
		    ieName = ieName.replace('/','_');
		    ieName = ieName.replace('[','_');
		    ieName = ieName.replace(']','_');
		    
		    ieXML = new File(FlowExample.prop.getProperty("IEfullFileName") + ieName + ".xml");
		    opt = new XmlOptions();
		    opt.setSavePrettyPrint();
		    FileUtil.writeFile(ieXML, metsDoc.xmlText(opt));
		    // 3. Place the SIP directory in a folder that can be accessed by the Rosetta application (using FTP is a valid approach)
			// Connecting to PDS
		    
			pds = PdsClient.getInstance();
			pds.init(FlowExample.prop.getProperty("PDS_URL"),false);
			
			pdsHandle = pds.login(FlowExample.prop.getProperty("institution"), FlowExample.prop.getProperty("userName"), FlowExample.prop.getProperty("password"));
			
			logger.debug("pdsHandle: " + pdsHandle);

			// 5. Submit Deposit

			logger.debug("Producer ID 1 : " + depositData.getDepDataArray(0).getDescription());
			producerId = depositData.getDepDataArray(0).getId();

			//submit
			retval = new DepositWebServices_Service(new URL(FlowExample.prop.getProperty("DEPOSIT_WSDL_URL")),new QName("http://dps.exlibris.com/", "DepositWebServices")).getDepositWebServicesPort().submitDepositActivity(pdsHandle,FlowExample.prop.getProperty("materialflowId"), subDirectoryName, producerId, FlowExample.prop.getProperty("depositSetId"));
			logger.info("Submit Deposit Result: " + retval);

			depositResultDocument = DepositResultDocument.Factory.parse(retval);
			depositResult = depositResultDocument.getDepositResult();

			// 6.check status of sip when deposit was successful
			validationCtr =0;
			String stage = null;
			for(int i=0;i<5000;i++){
				Thread.sleep(5000);//wait until deposit is in
				if(depositResult.getIsError()) {
					logger.info("Submit Deposit Failed");
					FileUtil.forceDelete(ieXML);
					return false;
				} else { 
					SipStatusInfo status = new SipWebServices_Service(new URL(FlowExample.prop.getProperty("SIP_STATUS_WSDL_URL")),new QName("http://dps.exlibris.com/", "SipWebServices")).getSipWebServicesPort().getSIPStatusInfo(String.valueOf(depositResult.getSipId()));
//					logger.info("Submitted Deposit Status: " + status.getStatus());
//					if (!("Loading".equals(stage = status.getStage())))  logger.info("Submitted Deposit Stage: " + stage);
					logger.info("Submitted Deposit Stage: " + status.getStage());
					if ("REJECTED".equals(status.getStatus().toUpperCase())) {
						FileUtil.forceDelete(ieXML);
						logger.info("Deposit rejected");
						return false;						
					}
					if ("IN_HUMAN_STAGE".equals(status.getStatus())) {
						logger.info("Submit Deposit Succeeded,but manual assessment necessary");
						String IEs = new SipWebServices_Service(new URL(FlowExample.prop.getProperty("SIP_STATUS_WSDL_URL")),new QName("http://dps.exlibris.com/", "SipWebServices")).getSipWebServicesPort().getSipIEs(String.valueOf(depositResult.getSipId()));
						FileUtil.forceDelete(ieXML);
						FlowExample.rosettaLink.addNumber(1,FlowExample.metsCounter+1,(int)depositResult.getSipId());
						FlowExample.rosettaLink.addLabel(2, FlowExample.metsCounter+1, IEs.replace(',',' ').trim());
						return true;
					} else if ("FINISHED".equals(status.getStatus())) {
						logger.info("Submit Deposit Succeeded");
						String IEs = new SipWebServices_Service(new URL(FlowExample.prop.getProperty("SIP_STATUS_WSDL_URL")),new QName("http://dps.exlibris.com/", "SipWebServices")).getSipWebServicesPort().getSipIEs(String.valueOf(depositResult.getSipId()));
						FileUtil.forceDelete(ieXML);
						FlowExample.rosettaLink.addNumber(1,FlowExample.metsCounter+1,(int)depositResult.getSipId());
						FlowExample.rosettaLink.addLabel(2, FlowExample.metsCounter+1, IEs.replace(',',' ').trim());
						return true;
					} else if (("IN_TA".equals(status.getStatus())) && ("Validation".equals(status.getStage()))) {
						validationCtr++;
						if (validationCtr >= 4) {
							FileUtil.forceDelete(ieXML);
							logger.info("Problems with validation of sip: "+depositResult.getSipId() + ". Check technical analist.");
							return false;
							
						}
					} else if (("IN_TA".equals(status.getStatus())) && ("System Errors".equals(status.getStage()))) {
						validationCtr++;
						if (validationCtr >= 4) {
							FileUtil.forceDelete(ieXML);
							logger.info("System Error for sip: "+depositResult.getSipId() + ". Check technical analist.");
							return false;
							
						}
					} else if (("IN_TA".equals(status.getStatus())) && ("Deposit".equals(status.getStage()))) {
						validationCtr++;
						if (validationCtr >= 4) {
							FileUtil.forceDelete(ieXML);
							logger.info("Problems with deposit of sip: "+depositResult.getSipId() + ". Check technical analist.");
							return false;
							
						}
					} 
				}
			}
		
			logger.info("Deposit failed");
			FileUtil.forceDelete(ieXML);
		    return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}