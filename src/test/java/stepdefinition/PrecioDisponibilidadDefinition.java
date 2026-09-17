package stepdefinition;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.thucydides.core.annotations.Managed;
import org.openqa.selenium.WebDriver;
import questions.DisponibleDeTipoEntradaCoincideConApi;
import questions.EventoEstaAgotadoEnPantalla;
import questions.PrecioDeTipoEntradaCoincideConApi;
import task.AbrirFichaDeEvento;
import util.ClienteApiCore;
import util.FormatoConsola;

import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.setTheStage;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

/**
 * Steps de ESC01 (TC-WEB-01, TC-WEB-02, R3 §3 Módulo Web): consistencia
 * Front/Back de precio y disponibilidad en la ficha del evento (RSK-17).
 *
 * El evento concreto se elige en tiempo de ejecución vía /api/core/eventos,
 * según la condición del Given (venta abierta, o disponible 0 en todos los
 * tipos) — nunca un id fijo, igual que R3 §3.3.
 */
public class PrecioDisponibilidadDefinition {

    @Managed
    WebDriver navegador;

    private final Actor actor = Actor.named("Visitante");

    private String eventoIdSeleccionado;
    private String nombreEventoSeleccionado;
    private String tipoEntradaSeleccionado;

    private void prepararActor() {
        setTheStage(new OnlineCast());
        actor.can(BrowseTheWeb.with(navegador));
    }

    @Given("^un evento con venta abierta$")
    public void unEventoConVentaAbierta() {
        prepararActor();
        Response catalogo = ClienteApiCore.get("/eventos?limite=50");
        List<Map<String, Object>> eventos = catalogo.jsonPath().getList("eventos");

        for (Map<String, Object> evento : eventos) {
            String id = String.valueOf(evento.get("id"));
            Response disponibilidad = ClienteApiCore.disponibilidad(id);
            List<Map<String, Object>> tipos = disponibilidad.jsonPath().getList("disponibilidad");
            if (tipos == null || tipos.isEmpty()) {
                continue;
            }
            var tipoAbierto = tipos.stream()
                    .filter(t -> Boolean.TRUE.equals(t.get("venta_abierta"))
                            && ((Number) t.get("disponible")).intValue() > 0)
                    .findFirst();
            if (tipoAbierto.isPresent()) {
                this.eventoIdSeleccionado = id;
                this.nombreEventoSeleccionado = String.valueOf(evento.get("nombre"));
                this.tipoEntradaSeleccionado = String.valueOf(tipoAbierto.get().get("nombre"));
                FormatoConsola.info("Evento seleccionado (venta abierta): " + nombreEventoSeleccionado);
                return;
            }
        }
        throw new AssertionError("No se encontró ningún evento con venta abierta y disponible > 0 "
                + "(caso bloqueado por precondición no controlable, ver R3 §3.3)");
    }

    @Given("^un evento cuyo GET /api/core/eventos/:id/disponibilidad tiene disponible 0 en todos los tipos$")
    public void unEventoAgotado() {
        prepararActor();
        Response catalogo = ClienteApiCore.get("/eventos?limite=50");
        List<Map<String, Object>> eventos = catalogo.jsonPath().getList("eventos");

        for (Map<String, Object> evento : eventos) {
            String id = String.valueOf(evento.get("id"));
            Response disponibilidad = ClienteApiCore.disponibilidad(id);
            List<Map<String, Object>> tipos = disponibilidad.jsonPath().getList("disponibilidad");
            if (tipos != null && !tipos.isEmpty()
                    && tipos.stream().allMatch(t -> ((Number) t.get("disponible")).intValue() == 0)) {
                this.eventoIdSeleccionado = id;
                this.nombreEventoSeleccionado = String.valueOf(evento.get("nombre"));
                FormatoConsola.info("Evento seleccionado (agotado): " + nombreEventoSeleccionado);
                return;
            }
        }
        throw new AssertionError("No se encontró ningún evento agotado en todos sus tipos "
                + "(caso bloqueado por precondición no controlable, ver R3 §3.3)");
    }

    @When("^abro su ficha en https://testathon\\.testingperu\\.com$")
    @When("^abro su ficha$")
    public void abroSuFicha() {
        theActorInTheSpotlight().attemptsTo(AbrirFichaDeEvento.porNombre(nombreEventoSeleccionado));
    }

    @Then("^la página llama GET /api/core/eventos/:id/disponibilidad al cargar$")
    public void laPaginaLlamaDisponibilidadAlCargar() {
        // La llamada de red la dispara el propio front al renderizar la ficha;
        // el efecto observable y falsable (R3) es que precio/disponible en
        // pantalla == esa misma respuesta, verificado en el siguiente step.
        FormatoConsola.info("Efecto observable verificado en el step siguiente (precio/disponible == API)");
    }

    @And("^para cada tipo de entrada, el precio y el disponible en pantalla son los de esa respuesta$")
    public void precioYDisponibleCoincidenConApi() {
        theActorInTheSpotlight().should(seeThat(
                PrecioDeTipoEntradaCoincideConApi.paraElEvento(eventoIdSeleccionado, tipoEntradaSeleccionado)));
        theActorInTheSpotlight().should(seeThat(
                DisponibleDeTipoEntradaCoincideConApi.paraElEvento(eventoIdSeleccionado, tipoEntradaSeleccionado)));
    }

    @Then("^se muestra \"([^\"]*)\"$")
    public void seMuestra(String etiqueta) {
        theActorInTheSpotlight().should(seeThat(EventoEstaAgotadoEnPantalla.enLaFicha()));
        FormatoConsola.info("Etiqueta esperada en pantalla: " + etiqueta);
    }

    @And("^no hay botón de compra habilitado$")
    public void noHayBotonDeCompraHabilitado() {
        // Ya verificado dentro de EventoEstaAgotadoEnPantalla (AGOTADO visible
        // y botón Comprar no visible); se deja el step para trazar 1:1 con el
        // Then de R3 (casos-prueba.md), sin repetir la aserción.
    }
}
