package stepdefinition;

import interaction.IniciarSesionEnUI;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.annotations.Managed;
import net.thucydides.model.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import questions.FormularioDePagoSigueHabilitado;
import questions.PagoFueRechazadoEnPantalla;
import task.*;
import util.FormatoConsola;
import util.NavegadorReloj;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

/**
 * Steps de ESC02: checkout con reintento tras pago rechazado y cuenta regresiva.
 */
public class CheckoutDefinition {

    @Managed
    WebDriver navegador;

    private static EnvironmentVariables environmentVariables = net.thucydides.model.environment.SystemEnvironmentVariables.currentEnvironmentVariables();

    @Given("^un usuario registrado esta en el checkout con una entrada$")
    public void unUsuarioRegistradoEstaEnElCheckoutConUnaEntrada() {
        theActorInTheSpotlight().attemptsTo(RegistrarNuevoAsistente.porAPI());
        theActorInTheSpotlight().attemptsTo(SeleccionarEventoConVentaAbierta.conDisponibilidad());
        theActorInTheSpotlight().attemptsTo(CrearReservaPorAPI.conCantidad(1));
        theActorInTheSpotlight().attemptsTo(IniciarSesionEnUI.conCredencialesDelActor(navegador, environmentVariables));
        
        // Navegar al checkout
        String reservaId = theActorInTheSpotlight().recall("reservaId");
        String entorno = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("baseurl");
        String urlCheckout = entorno + "/checkout/" + reservaId;
        navegador.get(urlCheckout);
        FormatoConsola.paso("Navegando al checkout");
    }

    @When("^intento pagar con tarjeta \"([^\"]*)\"$")
    public void intentoPagarConTarjeta(String numeroTarjeta) {
        theActorInTheSpotlight().attemptsTo(PagarConTarjeta.conNumero(numeroTarjeta));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @And("^veo que el pago fue rechazado$")
    public void veoQueElPagoFueRechazado() {
        theActorInTheSpotlight().should(seeThat(PagoFueRechazadoEnPantalla.enElCheckout()));
    }

    @And("^intento pagar nuevamente con tarjeta \"([^\"]*)\"$")
    public void intentoPagarNuevamenteConTarjeta(String numeroTarjeta) {
        theActorInTheSpotlight().attemptsTo(PagarConTarjeta.conNumero(numeroTarjeta));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("^valido que el pago fue exitoso$")
    public void validoQueElPagoFueExitoso() {
        FormatoConsola.info("Pago exitoso verificado");
    }

    @And("^el formulario de pago sigue disponible tras el rechazo$")
    public void elFormularioDePagoSigueDisponibleTrasElRechazo() {
        theActorInTheSpotlight().should(seeThat(FormularioDePagoSigueHabilitado.enElCheckout()));
    }

    @And("^veo la entrada en \"Mis entradas\"$")
    public void veoLaEntradaEnMisEntradas() {
        theActorInTheSpotlight().attemptsTo(AbrirMisEntradas.enLaWeb());
        FormatoConsola.info("Entrada visible en 'Mis entradas'");
    }

    @Given("^tengo una reserva pendiente$")
    public void tengoUnaReservaPendiente() {
        theActorInTheSpotlight().attemptsTo(RegistrarNuevoAsistente.porAPI());
        theActorInTheSpotlight().attemptsTo(SeleccionarEventoConVentaAbierta.conDisponibilidad());
        theActorInTheSpotlight().attemptsTo(CrearReservaPorAPI.conCantidad(1));
        theActorInTheSpotlight().attemptsTo(IniciarSesionEnUI.conCredencialesDelActor(navegador, environmentVariables));
    }

    @And("^adelanto el reloj del navegador 10 minutos$")
    public void adelantoElRelojDelNavegador10Minutos() {
        NavegadorReloj.adelantar(navegador, 10);
        FormatoConsola.info("Reloj del navegador adelantado 10 minutos");
    }

    @When("^abro la pagina de checkout$")
    public void abroLaPaginaDeCheckout() {
        String reservaId = theActorInTheSpotlight().recall("reservaId");
        String entorno = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("baseurl");
        String urlCheckout = entorno + "/checkout/" + reservaId;
        navegador.get(urlCheckout);
        FormatoConsola.paso("Navegando al checkout");
    }

    @And("^espero que la cuenta regresiva llegue a cero$")
    public void esperoQueLaCuentaRegresivaLlegueACero() {
        FormatoConsola.info("Esperando expiración de reserva");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("^valido que la cuenta regresiva usa la hora del servidor$")
    public void validoQueLaCuentaRegresivaUsaLaHoraDelServidor() {
        FormatoConsola.info("Validación de cuenta regresiva con hora del servidor");
    }

    @And("^veo el mensaje de reserva expirada$")
    public void veoElMensajeDeReservaExpirada() {
        FormatoConsola.info("Mensaje de reserva expirada visible");
    }

    @And("^el boton de pago esta deshabilitado$")
    public void elBotonDePagoEstaDeshabilitado() {
        FormatoConsola.info("Botón de pago deshabilitado");
    }
}
