//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2026.03.06 a las 02:17:51 AM GMT-05:00 
//


package org.uteq.restsoapwebservices.soap.generated;

import java.math.BigDecimal;
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
 *         &lt;element name="monto" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="divisaOrigen" type="{http://www.uteq.org/soap/divisas}Divisa"/&gt;
 *         &lt;element name="divisaDestino" type="{http://www.uteq.org/soap/divisas}Divisa"/&gt;
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
    "monto",
    "divisaOrigen",
    "divisaDestino"
})
@XmlRootElement(name = "ConversionRequest")
public class ConversionRequest {

    @XmlElement(required = true)
    protected BigDecimal monto;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Divisa divisaOrigen;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected Divisa divisaDestino;

    /**
     * Obtiene el valor de la propiedad monto.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMonto() {
        return monto;
    }

    /**
     * Define el valor de la propiedad monto.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMonto(BigDecimal value) {
        this.monto = value;
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

}
