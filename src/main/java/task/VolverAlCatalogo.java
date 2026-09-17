package task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.waits.WaitUntil;
import page.CatalogoPage;
import page.EventoDetallePage;
import util.FormatoConsola;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isVisible;

/**
 * Task para navegar de vuelta al catálogo desde la página de detalle de un evento.
 * Hace clic en el botón "← volver al catálogo" y espera a que el catálogo se cargue.
 */
public class VolverAlCatalogo implements Task {

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Hacer clic en el botón "volver al catálogo"
        actor.attemptsTo(
                Click.on(EventoDetallePage.BOTON_VOLVER_AL_CATALOGO)
        );
        
        // Esperar a que el catálogo se cargue (verificar que las tarjetas estén visibles)
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        FormatoConsola.info("Navegando de vuelta al catálogo");
    }

    public static VolverAlCatalogo desdeLaFichaDelEvento() {
        return instrumented(VolverAlCatalogo.class);
    }
}
