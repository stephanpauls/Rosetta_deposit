
package com.exlibris.dps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCMSRecord complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCMSRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dvs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="iePID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCMSRecord", propOrder = {
    "dvs",
    "iePID"
})
public class GetCMSRecord {

    protected String dvs;
    protected String iePID;

    /**
     * Gets the value of the dvs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDvs() {
        return dvs;
    }

    /**
     * Sets the value of the dvs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDvs(String value) {
        this.dvs = value;
    }

    /**
     * Gets the value of the iePID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIePID() {
        return iePID;
    }

    /**
     * Sets the value of the iePID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIePID(String value) {
        this.iePID = value;
    }

}
