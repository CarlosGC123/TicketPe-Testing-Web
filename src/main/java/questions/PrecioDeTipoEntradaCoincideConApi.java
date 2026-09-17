package questions;

import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import page.EventoDetallePage;
import util.ClienteApiCore;
import util.FormatoConsola;

/**
 * TC-WEB-01: compara el precio mostrado en la ficha del evento (para un tipo
 * de entrada dado) contra el precio real que devuelve
 * GET /api/core/eventos/:id/disponibilidad — consistencia Front/Back (RSK-17).
 *
 * Simplificación deliberada: toma el primer tipo de entrada cuyo nombre
 * contiene {@code nombreTipoEntrada} en la respuesta JSON. Si el contrato
 * cambia de forma (otro nombre de campo), este mapeo debe actualizarse — ver
 * README, "Mantenimiento continuo".
 */
public class PrecioDeTipoEntradaCoincideConApi implements Question<Boolean> {

    private final String eventoId;
    private final String nombreTipoEntrada;

    private PrecioDeTipoEntradaCoincideConApi(String eventoId, String nombreTipoEntrada) {
        this.eventoId = eventoId;
        this.nombreTipoEntrada = nombreTipoEntrada;
    }

    public static PrecioDeTipoEntradaCoincideConApi paraElEvento(String eventoId, String nombreTipoEntrada) {
        return new PrecioDeTipoEntradaCoincideConApi(eventoId, nombreTipoEntrada);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        Response respuesta = ClienteApiCore.disponibilidad(eventoId);
        if (respuesta.statusCode() != 200) {
            FormatoConsola.error("GET /disponibilidad respondió " + respuesta.statusCode() + " para el evento " + eventoId);
            return false;
        }

        Number precioApi = respuesta.jsonPath()
                .get("disponibilidad.find { it.nombre.toString().contains('" + nombreTipoEntrada + "') }.precio");
        if (precioApi == null) {
            FormatoConsola.error("La API no devolvió un tipo de entrada que contenga: " + nombreTipoEntrada);
            return false;
        }

        String precioEsperadoTexto = precioApi.doubleValue() == 0
                ? "Gratis"
                : "S/ " + precioApi;

        boolean visible = EventoDetallePage.PRECIO_TIPO_ENTRADA.of(nombreTipoEntrada)
                .resolveFor(actor).getText().contains(precioEsperadoTexto.replace("S/ ", "S/"))
                || EventoDetallePage.PRECIO_TIPO_ENTRADA.of(nombreTipoEntrada)
                        .resolveFor(actor).getText().contains(precioEsperadoTexto);

        FormatoConsola.info("Precio API: " + precioEsperadoTexto + " | visible: " + visible);
        return visible;
    }
}
