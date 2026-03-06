//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2026.03.06 a las 02:17:51 AM GMT-05:00 
//


package org.uteq.restsoapwebservices.soap.generated;

import java.math.BigDecimal;
import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="montoOriginal" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="divisaOrigen" type="{http://www.uteq.org/soap/divisas}Divisa"/&gt;
 *         &lt;element name="montoConvertido" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="divisaDestino" type="{http://www.uteq.org/soap/divisas}Divisa"/&gt;
 *         &lt;element name="tasaCambio" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "montoOriginal",
    "divisaOrigen",
    "montoConvertido",
    "divisaDestino",
    "tasaCambio",
    "timestamp"
})
@XmlRootElement(name = "ConversionResponse")
public class ConversionResponse {

    @XmlElement(required = true)
    protected BigDecimal montoOriginal;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Divisa divisaOrigen;
    @XmlElement(required = true)
    protected BigDecimal montoConvertido;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Divisa divisaDestino;
    @XmlElement(required = true)
    protected BigDecimal tasaCambio;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timestamp;

    /**
     * Obtiene el valor de la propiedad montoOriginal.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontoOriginal() {
        return montoOriginal;
    }

    /**
     * Define el valor de la propiedad montoOriginal.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontoOriginal(BigDecimal value) {
        this.montoOriginal = value;
    }

    /**
     * Obtiene el valor de la propiedad divisaOrigen.
     * 
     * @return
     *     possible object is
     *     {@link Divisa }
     *     
     */
    public Divisa getDivisaOrigen() {
        return divisaOrigen;
    }

    /**
     * Define el valor de la propiedad divisaOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link Divisa }
     *     
     */
    public void setDivisaOrigen(Divisa value) {
        this.divisaOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad montoConvertido.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontoConvertido() {
        return montoConvertido;
    }

    /**
     * Define el valor de la propiedad montoConvertido.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontoConvertido(BigDecimal value) {
        this.montoConvertido = value;
    }

    /**
     * Obtiene el valor de la propiedad divisaDestino.
     * 
     * @return
     *     possible object is
     *     {@link Divisa }
     *     
     */
    public Divisa getDivisaDestino() {
        return divisaDestino;
    }

    /**
     * Define el valor de la propiedad divisaDestino.
     * 
     * @param value
     *     allowed object is
     *     {@link Divisa }
     *     
     */
    public void setDivisaDestino(Divisa value) {
        this.divisaDestino = value;
    }

    /**
     * Obtiene el valor de la propiedad tasaCambio.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTasaCambio() {
        return tasaCambio;
    }

    /**
     * Define el valor de la propiedad tasaCambio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTasaCambio(BigDecimal value) {
        this.tasaCambio = value;
    }

    /**
     * Obtiene el valor de la propiedad timestamp.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Define el valor de la propiedad timestamp.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimestamp(XMLGregorianCalendar value) {
        this.timestamp = value;
    }

}
