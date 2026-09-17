package task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import page.CheckoutPage;
import task.clicks.DarClick;
import util.FormatoConsola;

import static net.serenitybdd.screenplay.actions.Enter.theValue;

/**
 * Ingresa el número de una tarjeta de prueba y confirma el pago del checkout
 * activo. Lógica de Negocio (CTAL-TAE v2.0): usada por TC-WEB-03 tanto para el
 * intento rechazado como para el reintento aprobado sobre la misma reserva.
 */
public class PagarConTarjeta implements Task {

    private final String numeroTarjeta;

    private PagarConTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public static PagarConTarjeta conNumero(String numeroTarjeta) {
        return new PagarConTarjeta(numeroTarjeta);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(theValue(numeroTarjeta).into(CheckoutPage.CAMPO_NUMERO_TARJETA));
        actor.attemptsTo(DarClick.enElElemento(CheckoutPage.BOTON_PAGAR));
        FormatoConsola.paso("Pago intentado con tarjeta terminada en "
                + numeroTarjeta.substring(numeroTarjeta.length() - 4));
    }
}
