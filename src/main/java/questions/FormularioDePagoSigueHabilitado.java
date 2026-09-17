package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import page.CheckoutPage;

/**
 * TC-WEB-03: el formulario de pago debe seguir habilitado para reintentar
 * (el botón "Pagar" y el campo de tarjeta siguen presentes y son clicables).
 */
public class FormularioDePagoSigueHabilitado implements Question<Boolean> {

    public static FormularioDePagoSigueHabilitado enElCheckout() {
        return new FormularioDePagoSigueHabilitado();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        boolean campoVisible = ElementoEsVisible.EnElPage(CheckoutPage.CAMPO_NUMERO_TARJETA, 5, 2).answeredBy(actor);
        boolean botonClicable = ElementoEsClickable.EnElPage(CheckoutPage.BOTON_PAGAR, 5, 2).answeredBy(actor);
        return campoVisible && botonClicable;
    }
}
