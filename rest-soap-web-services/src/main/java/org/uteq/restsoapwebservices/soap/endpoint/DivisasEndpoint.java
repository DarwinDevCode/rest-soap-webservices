package org.uteq.restsoapwebservices.soap.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.uteq.restsoapwebservices.soap.generated.ConversionRequest;
import org.uteq.restsoapwebservices.soap.generated.ConversionResponse;
import org.uteq.restsoapwebservices.soap.generated.Divisa;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Endpoint
public class DivisasEndpoint {

    private static final String NAMESPACE_URI = "http://www.uteq.org/soap/divisas";
    private static final Map<String, BigDecimal> TASAS_CAMBIO = new HashMap<>();

    static {
        TASAS_CAMBIO.put("USD", BigDecimal.ONE);
        TASAS_CAMBIO.put("EUR", new BigDecimal("0.92"));
        TASAS_CAMBIO.put("GBP", new BigDecimal("0.79"));
        TASAS_CAMBIO.put("JPY", new BigDecimal("149.50"));
        TASAS_CAMBIO.put("MXN", new BigDecimal("17.05"));
        TASAS_CAMBIO.put("COP", new BigDecimal("4105.50"));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConversionRequest")
    @ResponsePayload
    public ConversionResponse convertir(@RequestPayload ConversionRequest request) {
        log.info("SOAP Request: Convertir {} {} a {}",
                request.getMonto(), request.getDivisaOrigen(), request.getDivisaDestino());

        try {
            if (request.getMonto() == null || request.getDivisaOrigen() == null || request.getDivisaDestino() == null) {
                throw new IllegalArgumentException("Parametros invalidos en la solicitud SOAP");
            }

            BigDecimal monto = request.getMonto();
            String divisaOrigen = request.getDivisaOrigen().value();
            String divisaDestino = request.getDivisaDestino().value();

            if (divisaOrigen.equals(divisaDestino)) {
                return crearResponse(monto, divisaOrigen, monto, divisaDestino, BigDecimal.ONE);
            }

            BigDecimal tasaOrigen = TASAS_CAMBIO.get(divisaOrigen);
            BigDecimal tasaDestino = TASAS_CAMBIO.get(divisaDestino);

            if (tasaOrigen == null || tasaDestino == null) {
                throw new IllegalArgumentException("Divisa no soportada en el sistema");
            }

            BigDecimal montoEnUSD = monto.divide(tasaOrigen, 4, RoundingMode.HALF_UP);
            BigDecimal montoConvertido = montoEnUSD.multiply(tasaDestino).setScale(2, RoundingMode.HALF_UP);
            BigDecimal tasaCambio = tasaDestino.divide(tasaOrigen, 4, RoundingMode.HALF_UP);

            log.debug("Conversion exitosa. Resultado: {}", montoConvertido);
            return crearResponse(monto, divisaOrigen, montoConvertido, divisaDestino, tasaCambio);

        } catch (Exception e) {
            log.error("Error procesando solicitud SOAP: {}", e.getMessage());
            throw new RuntimeException("Error en conversion: " + e.getMessage());
        }
    }

    private ConversionResponse crearResponse(BigDecimal montoOriginal, String divisaOrigen,
                                             BigDecimal montoConvertido, String divisaDestino,
                                             BigDecimal tasaCambio) throws DatatypeConfigurationException {
        ConversionResponse response = new ConversionResponse();
        response.setMontoOriginal(montoOriginal);
        response.setDivisaOrigen(Divisa.fromValue(divisaOrigen));
        response.setMontoConvertido(montoConvertido);
        response.setDivisaDestino(Divisa.fromValue(divisaDestino));
        response.setTasaCambio(tasaCambio);

        GregorianCalendar gcal = GregorianCalendar.from(LocalDateTime.now().atZone(ZoneId.systemDefault()));
        XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        response.setTimestamp(xmlCal);

        return response;
    }
}