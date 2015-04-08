package com.exlibris.dps.sdk.jsonldfactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.exlibris.dps.DeliveryAccessWS;
import com.exlibris.dps.DeliveryAccessWS_Service;
import com.exlibris.dps.sdk.dgtconv.FlowExample;

public class jsonld {

	private static Logger logger = Logger.getLogger(jsonld.class);	
	public static Properties prop; 	
	private static DeliveryAccessWS deliveryAccessWS;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			deliveryAccessWS = new DeliveryAccessWS_Service(new URL(FlowExample.prop.getProperty("DELIVERY_ACCESS_WSDL_URL")),new QName("http://dps.exlibris.com/", "DeliveryAccessWS")).getDeliveryAccessWSPort();
		} catch (MalformedURLException eurl) {
			logger.info(eurl.getMessage());
		}

	}
}
