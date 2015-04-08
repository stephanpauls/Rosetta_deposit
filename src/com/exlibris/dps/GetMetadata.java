
package com.exlibris.dps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getMetadata complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getMetadata">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dvs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mdType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mdSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMetadata", propOrder = {
    "dvs",
    "pid",
    "mdType",
    "mdSubType"
})
public class GetMetadata {

    protected String dvs;
    protected String pid;
    protected String mdType;
    protected String mdSubType;

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
     * Gets the value of the pid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPid() {
        return pid;
    }

    /**
     * Sets the value of the pid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPid(String value) {
        this.pid = value;
    }

    /**
     * Gets the value of the mdType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMdType() {
        return mdType;
    }

    /**
     * Sets the value of the mdType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMdType(String value) {
        this.mdType = value;
    }

    /**
     * Gets the value of the mdSubType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMdSubType() {
        return mdSubType;
    }

    /**
     * Sets the value of the mdSubType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMdSubType(String value) {
        this.mdSubType = value;
    }

}
