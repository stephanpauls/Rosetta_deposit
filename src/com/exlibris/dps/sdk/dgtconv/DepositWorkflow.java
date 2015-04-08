package com.exlibris.dps.sdk.dgtconv;

import java.net.URL;

import javax.xml.namespace.QName;

import com.exlibris.core.sdk.utils.FileUtil;
import com.exlibris.digitool.deposit.service.xmlbeans.DepositDataDocument;
import com.exlibris.digitool.deposit.service.xmlbeans.DepositResultDocument;
import com.exlibris.digitool.deposit.service.xmlbeans.DepositDataDocument.DepositData;
import com.exlibris.digitool.deposit.service.xmlbeans.DepositResultDocument.DepositResult;
import com.exlibris.dps.DepositWebServices_Service;
import com.exlibris.dps.ProducerWebServices;
import com.exlibris.dps.ProducerWebServices_Service;
import com.exlibris.dps.SipStatusInfo;
import com.exlibris.dps.SipWebServices_Service;
import com.exlibris.dps.sdk.pds.PdsClient;

public class DepositWorkflow {

	/**
	 * Example of a bulk deposit program using the API of DepositWebServices
	 *
	 * @param args
	 *            location of ingest.properties file.
	 */
	public static void main(String[] args) {

		org.apache.log4j.helpers.LogLog.setQuietMode(true);

		try {
			// 3. Place the SIP directory in a folder that can be accessed by the Rosetta application (using FTP is a valid approach)
			
			// Connecting to PDS
			PdsClient pds = PdsClient.getInstance();
			pds.init(DGTConvConstants.PDS_URL,false);
			String pdsHandle = pds.login(DGTConvConstants.institution, DGTConvConstants.userName, DGTConvConstants.password);
			System.out.println("pdsHandle: " + pdsHandle);

			// 5. Submit Deposit

			ProducerWebServices producerWebServices = new ProducerWebServices_Service(new URL(DGTConvConstants.PRODUCER_WSDL_URL),new QName("http://dps.exlibris.com/", "ProducerWebServices")).getProducerWebServicesPort();
			String producerAgentId = producerWebServices.getInternalUserIdByExternalId(DGTConvConstants.userName);
			String xmlReply = producerWebServices.getProducersOfProducerAgent(producerAgentId);
			DepositDataDocument depositDataDocument = DepositDataDocument.Factory.parse(xmlReply);
			DepositData depositData = depositDataDocument.getDepositData();
			
			
			String producerId = depositData.getDepDataArray(0).getId();
			System.out.println("Producer ID: " + producerId);

			//submit
			String retval = new DepositWebServices_Service(new URL(DGTConvConstants.DEPOSIT_WSDL_URL),new QName("http://dps.exlibris.com/", "DepositWebServices")).getDepositWebServicesPort().submitDepositActivity(pdsHandle,DGTConvConstants.materialflowId, DGTConvConstants.subDirectoryName, producerId, DGTConvConstants.depositSetId);
			System.out.println("Submit Deposit Result: " + retval);

			DepositResultDocument depositResultDocument = DepositResultDocument.Factory.parse(retval);
			DepositResult depositResult = depositResultDocument.getDepositResult();

			for(int i=0;i<450;i++){
				Thread.sleep(2000);//wait until deposit is in
				if(depositResult.getIsError()){
					System.out.println("Submit Deposit Failed");
				}else{
					SipStatusInfo status = new SipWebServices_Service(new URL(DGTConvConstants.SIP_STATUS_WSDL_URL),new QName("http://dps.exlibris.com/", "SipWebServices")).getSipWebServicesPort().getSIPStatusInfo(String.valueOf(depositResult.getSipId()));
					System.out.println("Submitted Deposit Status: " + status.getStatus());
					System.out.println("Submitted Deposit Stage: " + status.getStage());
					System.out.println("Submitted Deposit is in Module: " + status.getModule());
					if ("REJECTED".equals(status.getStatus().toUpperCase())) {
						System.out.println("Deposit rejected");
						return ;						
				}
//					if ("Assessor".equals(status.getStage())) {
					if ("IN_HUMAN_STAGE".equals(status.getStatus())) {
						System.out.println("Submit Deposit Succeeded,but manual assessment necessary");
						return ;
					} else if ("FINISHED".equals(status.getStatus())) {
						System.out.println("Submit Deposit Succeeded");
						return ;
					} 
					
				}
			}

			System.out.println("Deposit failed");
		
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
