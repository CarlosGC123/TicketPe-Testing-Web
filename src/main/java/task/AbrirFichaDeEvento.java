package task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import page.CatalogoPage;
import task.clicks.DarClick;
import util.FormatoConsola;

/**
 * Abre la ficha de un evento a partir del catálogo, buscándolo por su nombre
 * visible. Lógica de Negocio (CTAL-TAE v2.0): encapsula "catálogo -> tarjeta
 * -> ficha" como una sola acción de negocio, reutilizable entre TC-WEB-01,
 * TC-WEB-02 y cualquier otro caso que necesite abrir un evento concreto.
 */
public class AbrirFichaDeEvento implements Task {

    private final String nombreEvento;

    private AbrirFichaDeEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public static AbrirFichaDeEvento porNombre(String nombreEvento) {
        return new AbrirFichaDeEvento(nombreEvento);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(AbrirCatalogo.enLaWeb());
        actor.attemptsTo(DarClick.enElElemento(CatalogoPage.TARJETA_EVENTO.of(nombreEvento)));
        FormatoConsola.paso("Abriendo la ficha del evento: " + nombreEvento);
    }
}
