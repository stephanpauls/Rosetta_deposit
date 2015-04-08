
package com.exlibris.dps;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-2b01 
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "DeleteCollectionException", targetNamespace = "http://dps.exlibris.com/")
public class DeleteCollectionException_Exception
    extends java.lang.Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private DeleteCollectionException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public DeleteCollectionException_Exception(String message, DeleteCollectionException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public DeleteCollectionException_Exception(String message, DeleteCollectionException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.exlibris.dps.DeleteCollectionException
     */
    public DeleteCollectionException getFaultInfo() {
        return faultInfo;
    }

}