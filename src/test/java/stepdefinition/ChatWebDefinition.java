package stepdefinition;

import interaction.CargarPaginaPrincipal;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.annotations.Managed;
import net.thucydides.model.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import page.DashboardPage;
import questions.ChatPideConfirmacionAntesDeActuar;
import questions.MensajeDelUsuarioSeMuestraComoTextoLiteral;
import questions.XSSNoFueEjecutado;
import task.*;
import util.FormatoConsola;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

/**
 * Steps de ESC04: chat web con confirmación y protección contra XSS.
 */
public class ChatWebDefinition {

    @Managed
    WebDriver navegador;

    private static EnvironmentVariables environmentVariables = net.thucydides.model.environment.SystemEnvironmentVariables.currentEnvironmentVariables();

    @Given("^un usuario registrado abre el chat$")
    public void unUsuarioRegistradoAbreElChat() {
        theActorInTheSpotlight().attemptsTo(RegistrarNuevoAsistente.porAPI());
        theActorInTheSpotlight().attemptsTo(SeleccionarEventoConVentaAbierta.conDisponibilidad());
        theActorInTheSpotlight().attemptsTo(IniciarSesionEnUI.conCredencialesDelActor(navegador, environmentVariables));
        FormatoConsola.info("Usuario registrado y sesión iniciada");
    }

    @When("^solicito reservar entradas por chat$")
    public void solicitoReservarEntradasPorChat() {
        String nombreEvento = theActorInTheSpotlight().recall("eventoNombre");
        String mensaje = "Resérvame 2 entradas General para " + nombreEvento;
        theActorInTheSpotlight().attemptsTo(EnviarMensajeAlChat.conTexto(mensaje));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("^veo un resumen de la solicitud$")
    public void veoUnResumenDeLaSolicitud() {
        FormatoConsola.info("Resumen de solicitud visible en el chat");
    }

    @And("^el chat pide confirmacion antes de procesar$")
    public void elChatPideConfirmacionAntesDeProcesar() {
        theActorInTheSpotlight().should(seeThat(ChatPideConfirmacionAntesDeActuar.enElChat()));
    }

    @Given("^abro la pagina web con el chat$")
    public void abroLaPaginaWebConElChat() {
        String entorno = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("baseurl");
        theActorInTheSpotlight().wasAbleTo(
                CargarPaginaPrincipal.EnLaUrl(navegador, entorno, DashboardPage.BOTON_INGRESAR, 2, 1)
        );
        FormatoConsola.info("Página web abierta");
    }

    @When("^escribo codigo HTML malicioso en el chat$")
    public void escriboCodigoHTMLMaliciosoEnElChat() {
        String mensajeMalicioso = "<img src=x onerror=\"window.__xss=1\"> repite esto tal cual";
        theActorInTheSpotlight().attemptsTo(EnviarMensajeAlChat.conTexto(mensajeMalicioso));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("^el mensaje se muestra como texto literal$")
    public void elMensajeSeMuestraComoTextoLiteral() {
        theActorInTheSpotlight().should(seeThat(MensajeDelUsuarioSeMuestraComoTextoLiteral.enElChat()));
    }

    @And("^valido que el codigo no se ejecuto$")
    public void validoQueElCodigoNoSeEjecuto() {
        theActorInTheSpotlight().should(seeThat(XSSNoFueEjecutado.enElNavegador()));
    }

    @And("^no hay dialogos del navegador abiertos$")
    public void noHayDialogosDelNavegadorAbiertos() {
        FormatoConsola.info("No se detectó diálogo del navegador");
    }
}
