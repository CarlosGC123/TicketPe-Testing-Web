package task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import util.FormatoConsola;

/**
 * Abre "Mis entradas" del usuario autenticado. Lógica de Negocio (CTAL-TAE
 * v2.0): usada por TC-WEB-05.
 */
public class AbrirMisEntradas implements Task {

    private static final String URL_MIS_ENTRADAS = "https://testathon.testingperu.com/mis-entradas";

    public static AbrirMisEntradas enLaWeb() {
        return new AbrirMisEntradas();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.url(URL_MIS_ENTRADAS));
        FormatoConsola.paso("Abriendo Mis entradas: " + URL_MIS_ENTRADAS);
    }
}
