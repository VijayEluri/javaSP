//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2-70- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.15 at 04:02:06 em CEST 
//


package sequenceplanner.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="geo">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="geometry" type="{}rectangle"/>
 *                   &lt;element name="alternateGeometry" type="{}rectangle" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *       &lt;attribute name="refId" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="previousCell" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="relation" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="lastInRelation" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="expanded" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "CellData")
public class CellData {

    @XmlElement(required = true)
    protected CellData.Geo geo;
    @XmlAttribute(name = "refId", required = true)
    protected int refId;
    @XmlAttribute(name = "previousCell", required = true)
    protected int previousCell;
    @XmlAttribute(name = "type", required = true)
    protected int type;
    @XmlAttribute(name = "relation", required = true)
    protected int relation;
    @XmlAttribute(name = "lastInRelation", required = true)
    protected boolean lastInRelation;
    @XmlAttribute(name = "expanded", required = true)
    protected boolean expanded;

    /**
     * Gets the value of the geo property.
     * 
     * @return
     *     possible object is
     *     {@link CellData.Geo }
     *     
     */
    public CellData.Geo getGeo() {
        return geo;
    }

    /**
     * Sets the value of the geo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CellData.Geo }
     *     
     */
    public void setGeo(CellData.Geo value) {
        this.geo = value;
    }

    /**
     * Gets the value of the refId property.
     * 
     */
    public int getRefId() {
        return refId;
    }

    /**
     * Sets the value of the refId property.
     * 
     */
    public void setRefId(int value) {
        this.refId = value;
    }

    /**
     * Gets the value of the previousCell property.
     * 
     */
    public int getPreviousCell() {
        return previousCell;
    }

    /**
     * Sets the value of the previousCell property.
     * 
     */
    public void setPreviousCell(int value) {
        this.previousCell = value;
    }

    /**
     * Gets the value of the type property.
     * 
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     */
    public void setType(int value) {
        this.type = value;
    }

    /**
     * Gets the value of the relation property.
     * 
     */
    public int getRelation() {
        return relation;
    }

    /**
     * Sets the value of the relation property.
     * 
     */
    public void setRelation(int value) {
        this.relation = value;
    }

    /**
     * Gets the value of the lastInRelation property.
     * 
     */
    public boolean isLastInRelation() {
        return lastInRelation;
    }

    /**
     * Sets the value of the lastInRelation property.
     * 
     */
    public void setLastInRelation(boolean value) {
        this.lastInRelation = value;
    }

    /**
     * Gets the value of the expanded property.
     * 
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * Sets the value of the expanded property.
     * 
     */
    public void setExpanded(boolean value) {
        this.expanded = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="geometry" type="{}rectangle"/>
     *         &lt;element name="alternateGeometry" type="{}rectangle" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "geometry",
        "alternateGeometry"
    })
    public static class Geo {

        @XmlElement(required = true)
        protected Rectangle geometry;
        protected Rectangle alternateGeometry;

        /**
         * Gets the value of the geometry property.
         * 
         * @return
         *     possible object is
         *     {@link Rectangle }
         *     
         */
        public Rectangle getGeometry() {
            return geometry;
        }

        /**
         * Sets the value of the geometry property.
         * 
         * @param value
         *     allowed object is
         *     {@link Rectangle }
         *     
         */
        public void setGeometry(Rectangle value) {
            this.geometry = value;
        }

        /**
         * Gets the value of the alternateGeometry property.
         * 
         * @return
         *     possible object is
         *     {@link Rectangle }
         *     
         */
        public Rectangle getAlternateGeometry() {
            return alternateGeometry;
        }

        /**
         * Sets the value of the alternateGeometry property.
         * 
         * @param value
         *     allowed object is
         *     {@link Rectangle }
         *     
         */
        public void setAlternateGeometry(Rectangle value) {
            this.alternateGeometry = value;
        }

    }

}
