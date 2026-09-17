package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import page.EventoDetallePage;
import util.FormatoConsola;

/**
 * TC-WEB-02: un evento sin cupo debe mostrar "AGOTADO" y no debe ofrecer un
 * botón de compra habilitado.
 */
public class EventoEstaAgotadoEnPantalla implements Question<Boolean> {

    public static EventoEstaAgotadoEnPantalla enLaFicha() {
        return new EventoEstaAgotadoEnPantalla();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        boolean muestraAgotado = ElementoEsVisible.EnElPage(EventoDetallePage.ETIQUETA_AGOTADO, 5, 2).answeredBy(actor);
        boolean botonCompraVisible = ElementoEsVisible.EnElPage(EventoDetallePage.BOTON_COMPRAR, 2, 1).answeredBy(actor);

        FormatoConsola.info("AGOTADO visible: " + muestraAgotado + " | boton Comprar visible: " + botonCompraVisible);
        return muestraAgotado && !botonCompraVisible;
    }
}
