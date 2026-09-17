package task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import util.FormatoConsola;

/**
 * Abre el catálogo de eventos. Lógica de Negocio (CTAL-TAE v2.0): una sola
 * acción reutilizable en vez de repetir la URL en cada step definition.
 */
public class AbrirCatalogo implements Task {

    private static final String URL_CATALOGO = "https://testathon.testingperu.com/catalogo";

    public static AbrirCatalogo enLaWeb() {
        return new AbrirCatalogo();
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Open.url(URL_CATALOGO));
        FormatoConsola.paso("Abriendo el catalogo: " + URL_CATALOGO);
    }
}
