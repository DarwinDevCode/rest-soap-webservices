//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2026.03.06 a las 02:17:51 AM GMT-05:00 
//


package org.uteq.restsoapwebservices.soap.generated;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Divisa.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <pre>
 * &lt;simpleType name="Divisa"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="USD"/&gt;
 *     &lt;enumeration value="EUR"/&gt;
 *     &lt;enumeration value="GBP"/&gt;
 *     &lt;enumeration value="JPY"/&gt;
 *     &lt;enumeration value="MXN"/&gt;
 *     &lt;enumeration value="COP"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Divisa")
@XmlEnum
public enum Divisa {

    USD,
    EUR,
    GBP,
    JPY,
    MXN,
    COP;

    public String value() {
        return name();
    }

    public static Divisa fromValue(String v) {
        return valueOf(v);
    }

}
