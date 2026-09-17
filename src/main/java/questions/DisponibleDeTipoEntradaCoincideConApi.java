package questions;

import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import page.EventoDetallePage;
import util.ClienteApiCore;
import util.FormatoConsola;

/**
 * TC-WEB-01: compara el disponible mostrado en la ficha del evento (para un
 * tipo de entrada dado) contra el valor real de
 * GET /api/core/eventos/:id/disponibilidad — consistencia Front/Back (RSK-17).
 */
public class DisponibleDeTipoEntradaCoincideConApi implements Question<Boolean> {

    private final String eventoId;
    private final String nombreTipoEntrada;

    private DisponibleDeTipoEntradaCoincideConApi(String eventoId, String nombreTipoEntrada) {
        this.eventoId = eventoId;
        this.nombreTipoEntrada = nombreTipoEntrada;
    }

    public static DisponibleDeTipoEntradaCoincideConApi paraElEvento(String eventoId, String nombreTipoEntrada) {
        return new DisponibleDeTipoEntradaCoincideConApi(eventoId, nombreTipoEntrada);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        Response respuesta = ClienteApiCore.disponibilidad(eventoId);
        if (respuesta.statusCode() != 200) {
            FormatoConsola.error("GET /disponibilidad respondió " + respuesta.statusCode() + " para el evento " + eventoId);
            return false;
        }

        Integer disponibleApi = respuesta.jsonPath()
                .get("disponibilidad.find { it.nombre.toString().contains('" + nombreTipoEntrada + "') }.disponible");
        if (disponibleApi == null) {
            FormatoConsola.error("La API no devolvió un tipo de entrada que contenga: " + nombreTipoEntrada);
            return false;
        }

        String textoEsperado = disponibleApi + " disp.";
        boolean visible = EventoDetallePage.DISPONIBLE_TIPO_ENTRADA.of(nombreTipoEntrada)
                .resolveFor(actor).getText().contains(textoEsperado);

        FormatoConsola.info("Disponible API: " + textoEsperado + " | visible: " + visible);
        return visible;
    }
}
