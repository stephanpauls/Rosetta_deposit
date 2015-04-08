
package com.exlibris.dps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getIEByDVS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getIEByDVS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dvs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getIEByDVS", propOrder = {
    "dvs"
})
public class GetIEByDVS {

    protected String dvs;

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

}
