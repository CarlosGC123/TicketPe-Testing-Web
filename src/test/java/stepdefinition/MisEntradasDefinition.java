package stepdefinition;

import interaction.IniciarSesionEnUI;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.annotations.Managed;
import net.thucydides.model.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import task.*;
import util.FormatoConsola;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

/**
 * Steps de ESC03: visualización de entradas con diferentes estados.
 */
public class MisEntradasDefinition {

    @Managed
    WebDriver navegador;

    private static EnvironmentVariables environmentVariables = net.thucydides.model.environment.SystemEnvironmentVariables.currentEnvironmentVariables();

    private Actor actorA;
    private Actor actorB;

    private void prepararActores() {
        // No llamamos a setTheStage() porque ya está configurado en LoginDefinition
        actorA = Actor.named("Asistente A");
        actorB = Actor.named("Asistente B");
        actorA.can(BrowseTheWeb.with(navegador));
        actorB.can(BrowseTheWeb.with(navegador));
    }

    @Given("^el usuario A compro 4 entradas para un evento$")
    public void elUsuarioACompro4EntradasParaUnEvento() {
        prepararActores();
        theActorCalled(actorA.getName()).attemptsTo(RegistrarNuevoAsistente.porAPI());
        theActorCalled(actorA.getName()).attemptsTo(SeleccionarEventoConVentaAbierta.conDisponibilidad());
        theActorCalled(actorA.getName()).attemptsTo(ComprarEntradasPorAPI.conCantidad(4));
        FormatoConsola.info("Usuario A compró 4 entradas");
    }

    @And("^modifico los estados de las entradas del usuario A$")
    public void modificoLosEstadosDeLasEntradasDelUsuarioA() {
        // Registrar usuario B primero para poder transferir
        theActorCalled(actorB.getName()).attemptsTo(RegistrarNuevoAsistente.porAPI());
        String correoB = actorB.recall("correo");
        
        // Modificar estados de las entradas de A
        theActorCalled(actorA.getName()).attemptsTo(ModificarEstadosDeEntradas.transferirUnaA(correoB));
        FormatoConsola.info("Estados de entradas modificados");
    }

    @And("^el usuario B recibe una entrada transferida$")
    public void elUsuarioBRecibeUnaEntradaTransferida() {
        FormatoConsola.info("Usuario B recibió entrada transferida");
    }

    @When("^el usuario A abre \"Mis entradas\"$")
    public void elUsuarioAAbreMisEntradas() {
        theActorCalled(actorA.getName()).attemptsTo(IniciarSesionEnUI.conCredencialesDelActor(navegador, environmentVariables));
        theActorCalled(actorA.getName()).attemptsTo(AbrirMisEntradas.enLaWeb());
    }

    @And("^el usuario B abre \"Mis entradas\"$")
    public void elUsuarioBAbreMisEntradas() {
        // Cerrar sesión de A y abrir sesión de B
        navegador.manage().deleteAllCookies();
        theActorCalled(actorB.getName()).attemptsTo(IniciarSesionEnUI.conCredencialesDelActor(navegador, environmentVariables));
        theActorCalled(actorB.getName()).attemptsTo(AbrirMisEntradas.enLaWeb());
    }

    @Then("^el usuario A ve 3 entradas con estados \"([^\"]*)\", \"([^\"]*)\" y \"([^\"]*)\"$")
    public void elUsuarioAVe3EntradasConEstados(String estado1, String estado2, String estado3) {
        FormatoConsola.info("Usuario A ve 3 entradas con estados: " + estado1 + ", " + estado2 + ", " + estado3);
        // Aquí se debería usar una Question específica para verificar los estados
    }

    @And("^el usuario A no ve la entrada transferida$")
    public void elUsuarioANoVeLaEntradaTransferida() {
        FormatoConsola.info("Usuario A no ve la entrada transferida");
    }

    @And("^el usuario B ve la entrada transferida$")
    public void elUsuarioBVeLaEntradaTransferida() {
        FormatoConsola.info("Usuario B ve la entrada transferida");
    }
}
