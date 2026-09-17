package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import page.CheckoutPage;

/**
 * TC-WEB-03: tras un pago rechazado, el checkout debe informar el rechazo y
 * el formulario debe seguir habilitado para reintentar sobre la misma
 * reserva (no debe cancelarla, RSK-07).
 */
public class PagoFueRechazadoEnPantalla implements Question<Boolean> {

    public static PagoFueRechazadoEnPantalla enElCheckout() {
        return new PagoFueRechazadoEnPantalla();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        return ElementoEsVisible.EnElPage(CheckoutPage.MENSAJE_PAGO_RECHAZADO, 10, 2).answeredBy(actor);
    }
}
