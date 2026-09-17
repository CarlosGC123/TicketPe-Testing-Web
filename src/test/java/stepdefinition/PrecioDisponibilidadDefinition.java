package stepdefinition;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import net.thucydides.model.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import questions.DisponibilidadTarjetaCoincideConDetalle;
import questions.DisponibleDeTipoEntradaCoincideConApi;
import questions.EventoEstaAgotadoEnPantalla;
import questions.PrecioTarjetaCoincideConDetalle;
import questions.PrecioDeTipoEntradaCoincideConApi;
import questions.TodosLosEventosTienenDatosConsistentes;
import task.AbrirFichaDeEvento;
import task.ObtenerTodosLosEventosDelCatalogo;
import task.SeleccionarEventoConVentaAbierta;
import task.SeleccionarEventoEnCatalogo;
import task.ValidarTodosLosEventosDelCatalogo;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

/**
 * Steps de ESC01: consistencia Front/Back de precio y disponibilidad en la ficha del evento.
 */
public class PrecioDisponibilidadDefinition {

    @Managed
    WebDriver navegador;

    // Nuevos steps para validación catálogo vs detalle
    @Given("^selecciono el evento \"([^\"]*)\" en el catalogo$")
    public void seleccionoElEventoEnElCatalogo(String nombreEvento) {
        theActorInTheSpotlight().attemptsTo(SeleccionarEventoEnCatalogo.porNombre(nombreEvento));
    }

    @Then("^valido que el precio de la tarjeta coincide con el precio en el detalle$")
    public void validoQueElPrecioDeLaTarjetaCoincideConElPrecioEnElDetalle() {
        // Por defecto usamos "General" como nombre de zona
        // TODO: Si hay múltiples zonas, extraer el nombre de la zona de la tarjeta
        theActorInTheSpotlight().should(seeThat(
                PrecioTarjetaCoincideConDetalle.paraLaZona("General")));
    }

    @And("^valido que la disponibilidad de la tarjeta coincide con la disponibilidad en el detalle$")
    public void validoQueLaDisponibilidadDeLaTarjetaCoincideConLaDisponibilidadEnElDetalle() {
        // Por defecto usamos "General" como nombre de zona
        // TODO: Si hay múltiples zonas, extraer el nombre de la zona de la tarjeta
        theActorInTheSpotlight().should(seeThat(
                DisponibilidadTarjetaCoincideConDetalle.paraLaZona("General")));
    }

    // Steps originales para validación con API
    @Given("^selecciono un evento disponible para compra$")
    public void seleccionoUnEventoDisponibleParaCompra() {
        theActorInTheSpotlight().attemptsTo(SeleccionarEventoConVentaAbierta.conDisponibilidad());
    }

    @Given("^selecciono un evento agotado$")
    public void seleccionoUnEventoAgotado() {
        // Para evento agotado, necesitamos buscar uno con disponible = 0
        // Por ahora usamos la misma task, pero esto debería ser una task específica
        theActorInTheSpotlight().attemptsTo(SeleccionarEventoConVentaAbierta.sinRequerirDisponibilidad());
    }

    @When("^abro la ficha del evento$")
    public void abroLaFichaDelEvento() {
        String nombreEvento = theActorInTheSpotlight().recall("eventoNombre");
        theActorInTheSpotlight().attemptsTo(AbrirFichaDeEvento.porNombre(nombreEvento));
    }

    @Then("^valido que el precio mostrado coincide con el sistema$")
    public void validoQueElPrecioMostradoCoincideConElSistema() {
        String eventoId = theActorInTheSpotlight().recall("eventoId");
        String tipoEntradaNombre = theActorInTheSpotlight().recall("tipoEntradaNombre");
        theActorInTheSpotlight().should(seeThat(
                PrecioDeTipoEntradaCoincideConApi.paraElEvento(eventoId, tipoEntradaNombre)));
    }

    @And("^valido que la disponibilidad mostrada coincide con el sistema$")
    public void validoQueLaDisponibilidadMostradaCoincideConElSistema() {
        String eventoId = theActorInTheSpotlight().recall("eventoId");
        String tipoEntradaNombre = theActorInTheSpotlight().recall("tipoEntradaNombre");
        theActorInTheSpotlight().should(seeThat(
                DisponibleDeTipoEntradaCoincideConApi.paraElEvento(eventoId, tipoEntradaNombre)));
    }

    @Then("^veo el mensaje \"([^\"]*)\"$")
    public void veoElMensaje(String mensaje) {
        theActorInTheSpotlight().should(seeThat(EventoEstaAgotadoEnPantalla.enLaFicha()));
    }

    @And("^el boton de compra no esta disponible$")
    public void elBotonDeCompraNoEstaDisponible() {
        // Ya verificado dentro de EventoEstaAgotadoEnPantalla
    }

    // Nuevos steps para validación de todos los eventos del catálogo (CP03)
    private static EnvironmentVariables environmentVariables = net.thucydides.model.environment.SystemEnvironmentVariables.currentEnvironmentVariables();

    @Given("^obtengo la lista de todos los eventos del catalogo$")
    public void obtengoLaListaDeTodosLosEventosDelCatalogo() {
        theActorInTheSpotlight().attemptsTo(
                ObtenerTodosLosEventosDelCatalogo.delSitio(environmentVariables)
        );
    }

    @When("^valido precio y disponibilidad para cada evento del catalogo$")
    public void validoPrecioYDisponibilidadParaCadaEventoDelCatalogo() {
        theActorInTheSpotlight().attemptsTo(
                ValidarTodosLosEventosDelCatalogo.conPrecioYDisponibilidad()
        );
    }

    @Then("^todos los eventos deben tener datos consistentes entre catalogo y detalle$")
    public void todosLosEventosDebenTenerDatosConsistentesEntreCatalogoYDetalle() {
        theActorInTheSpotlight().should(seeThat(
                TodosLosEventosTienenDatosConsistentes.entreCatalogoYDetalle()
        ));
    }
}
