package com.exlibris.dps.sdk.dgtconv;

import java.io.FileInputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.exlibris.digitool.deposit.service.xmlbeans.DepositDataDocument;
import com.exlibris.digitool.deposit.service.xmlbeans.DepositResultDocument;
import com.exlibris.digitool.deposit.service.xmlbeans.DepositDataDocument.DepositData;
import com.exlibris.digitool.deposit.service.xmlbeans.DepositResultDocument.DepositResult;
import com.exlibris.dps.Collection;
import com.exlibris.dps.CollectionWebServices;
import com.exlibris.dps.CollectionWebServices_Service;
import com.exlibris.dps.DepositWebServices_Service;
import com.exlibris.dps.MetaData;
import com.exlibris.dps.ProducerWebServices;
import com.exlibris.dps.ProducerWebServices_Service;
import com.exlibris.dps.SipStatusInfo;
import com.exlibris.dps.SipWebServices_Service;
import com.exlibris.dps.sdk.pds.PdsClient;

public class ConnectionWorkFlow {

	/**
	 * Example of a bulk deposit program using the API of DepositWebServices
	 *
	 * @param args
	 *            location of ingest.properties file.
	 */
	public static Properties prop; 
	private static Logger logger = Logger.getLogger(ConnectionWorkFlow.class);

	
	public static void main(String[] args) {

		org.apache.log4j.helpers.LogLog.setQuietMode(true);
    	prop = new Properties();		
		try {
			
			//create oracle connection 
			TestDBOracle db = new TestDBOracle();

			String pid;
			String label;
			String pdsHandle;
			prop.load(new FileInputStream("dgtconv.properties"));			
			// Connecting to PDS
			PdsClient pds = PdsClient.getInstance();
			pds.init(prop.getProperty("PDS_URL"),false);
			String ins = prop.getProperty("institution");
			pdsHandle = pds.login(prop.getProperty("institution"), prop.getProperty("userName"), prop.getProperty("password"));
			System.out.println("pdsHandle: " + pdsHandle);

			
			  Statement stmt = null;
			  ResultSet rset = null;
			  String filePath = "";
			  Long sip = Long.valueOf(0);
			  try{
				  
				CollectionWebServices collWebService = new CollectionWebServices_Service(new URL(prop.getProperty("COLLECTION_WSDL_URL")),new QName("http://dps.exlibris.com/", "CollectionWebServices")).getCollectionWebServicesPort();
				db.getConnection();
				
			    stmt = db.getConn().createStatement();
			    
			    String query =  "select c.label,c.pid,nvl(m.value,'empty') from hdecontrol c" +
						" left outer join hdepidmid pm on pm.hdecontrol = c.id left outer join hdemetadata m on m.id = pm.hdemetadata" +			    
			    		" where c.partitionb = 'DO_COLL_MIGRATIE_TEXTURA' or c.partitionb = 'DO_COLL_DO_MIGRATIE_TEXTURA'";
			    
			    rset = stmt.executeQuery(query);
			    while (rset.next()) {
			    	label = rset.getString(1);
			    	pid = rset.getString(2);
			    	String metadata = rset.getString(3);
			    	
					Collection collection1 = new Collection();
					collection1.setName(pid);
			    	if (!"empty".equals(metadata)) {
				    	MetaData md = new MetaData();
				    	md.setContent(metadata);
						collection1.setMdDc(md);
			    	}
					try {
						sip = collWebService.createCollection(pdsHandle, collection1);
						db.setDone("partitionb",pid,sip.toString());
					} catch (Exception e){
						logger.info(e.getMessage());
					}
			    	
			    }
			    db.getConn().commit();
			    rset.close();
			    stmt.close();


				ResultSet rset2 = null;
			    stmt = db.getConn().createStatement();
			    String query2 =  "select c.pid,SUBSTR(partitionb,instr(partitionb,'DONE ')+5),c.label from hdecontrol c where c.partitionb like 'DO_COLL_MIGRATIE_TEXTURA DONE%'";
			    rset2 = stmt.executeQuery(query2);
			    int i=0;
			    while (rset2.next()) {
			    	pid = rset2.getString(1);
			    	sip = Long.valueOf(rset2.getString(2));
			    	label = rset2.getString(3);
					Collection collection2 = null;
					
					try {
						collection2 = collWebService.getCollectionById(pdsHandle, sip);
						sip = Long.valueOf((db.getParentCollSip(pid)));
						collection2.setParentId(sip);
						collection2.setName(label);
						try{
							collWebService.updateCollection(pdsHandle, collection2);
						} catch (Exception e) {
								collection2.setName(label+"_"+(i++));
								collWebService.updateCollection(pdsHandle, collection2);
						}
					} catch (Exception e){
						logger.info(e.getMessage());

					}
			    }
			    rset2.close();

				ResultSet rset3 = null;
			    stmt = db.getConn().createStatement();
			    String query3 =  "select SUBSTR(partitionb,instr(partitionb,'DONE ')+5),c.label from hdecontrol c where c.partitionb like 'DO_COLL_DO_MIGRATIE_TEXTURA DONE%'";
			    rset3 = stmt.executeQuery(query3);
			    while (rset3.next()) {
			    	sip = Long.valueOf(rset3.getString(1));
			    	label = rset3.getString(2);
					Collection collection3 = null;
					
					try {
						collection3 = collWebService.getCollectionById(pdsHandle, sip);
						collection3.setName(label);
						collWebService.updateCollection(pdsHandle, collection3);
					} catch (Exception e){
						logger.info(e.getMessage());

					}
			    }
			    rset3.close();
			    
			    stmt.close();
			    
			  } catch (SQLException e){
				  logger.debug(e.getMessage());
			  }			
			

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
