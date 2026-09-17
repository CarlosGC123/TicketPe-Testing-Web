package task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.questions.Text;
import page.CatalogoPage;
import util.FormatoConsola;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task para seleccionar un evento específico por nombre en el catálogo (UI),
 * extraer el precio y disponibilidad de la tarjeta, y navegar al detalle.
 * 
 * Esta Task almacena en el estado del actor:
 * - eventoNombre: nombre del evento
 * - precioTarjeta: precio mostrado en la tarjeta del catálogo (ej: "Gratis", "S/ 0")
 * - disponibilidadTarjeta: disponibilidad mostrada en la tarjeta (ej: "42 disp.")
 */
public class SeleccionarEventoEnCatalogo implements Task {

    private final String nombreEvento;

    public SeleccionarEventoEnCatalogo(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        FormatoConsola.info("Buscando evento en catálogo: " + nombreEvento);
        
        // Extraer precio de la tarjeta del catálogo
        String precioTarjeta = Text.of(CatalogoPage.PRECIO_DE.of(nombreEvento))
                .answeredBy(actor)
                .trim();
        
        // Extraer disponibilidad de la tarjeta del catálogo
        String disponibilidadTarjeta = Text.of(CatalogoPage.DISPONIBLE_DE.of(nombreEvento))
                .answeredBy(actor)
                .trim();
        
        // Almacenar datos en el estado del actor
        actor.remember("eventoNombre", nombreEvento);
        actor.remember("precioTarjeta", precioTarjeta);
        actor.remember("disponibilidadTarjeta", disponibilidadTarjeta);
        
        FormatoConsola.info("Datos extraídos de la tarjeta:");
        FormatoConsola.info("  - Precio: " + precioTarjeta);
        FormatoConsola.info("  - Disponibilidad: " + disponibilidadTarjeta);
        
        // Hacer clic en la tarjeta para navegar al detalle
        actor.attemptsTo(
                Click.on(CatalogoPage.TARJETA_EVENTO.of(nombreEvento))
        );
        
        FormatoConsola.paso("Navegando al detalle del evento: " + nombreEvento);
    }

    public static SeleccionarEventoEnCatalogo porNombre(String nombreEvento) {
        return instrumented(SeleccionarEventoEnCatalogo.class, nombreEvento);
    }
}
