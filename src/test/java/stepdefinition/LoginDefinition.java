package stepdefinition;

import interaction.CargarPaginaPrincipal;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import page.DashboardPage;
import page.Login;
import questions.ElementoEsVisible;
import task.EscribeTexto;
import task.clicks.DarClick;
import util.FormatoConsola;
import util.ObtenerCredenciales;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.*;

public class LoginDefinition {

    @Managed
    WebDriver navegador;

    @Steps(shared = true)
    Actor actor = Actor.named("Usuario");

    @Before
    public void configuracionInicial() {
        setTheStage(new OnlineCast());
    }

    private static EnvironmentVariables environmentVariables;

    @Given("usuario ingresa a la pagina de TicketPe")
    public void UsuarioIngresaPaginTicketPe() {
        String entorno=EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("baseurl");
        FormatoConsola.banner("ENTORNO DE PRUEBA : " + entorno);
        theActorCalled(actor.getName()).wasAbleTo(
                CargarPaginaPrincipal.EnLaUrl(
                        navegador,
                        entorno,
                        DashboardPage.BOTON_INGRESAR,
                        2,
                        1
                )
        );
    }

    @When("ingreso el correo electronico")
    public void ingresoCorreoElectronico() {
        theActorInTheSpotlight().attemptsTo(
                EscribeTexto.enElElemento(
                        Login.CAMPO_CORREO,
                        ObtenerCredenciales.ObtenerCorreo())
        );
    }

    @When("ingreso el password")
    public void ingresoPassword() {
        theActorInTheSpotlight().attemptsTo(
                EscribeTexto.enElElemento(
                        Login.CAMPO_CONTRASENA,
                        ObtenerCredenciales.ObtenerContrasena())
        );
    }

    @When("presiono el boton Ingresar")
    public void presionoBotonIngresar() {
        theActorInTheSpotlight().attemptsTo(
                DarClick.enElElemento(
                        DashboardPage.BOTON_INGRESAR)
        );
    }

    @Then("valido el Login correcto de la pagina")
    public void validoLoginCorrecto() {
        theActorInTheSpotlight().should(
                seeThat(
                        ElementoEsVisible.EnElPage(
                                DashboardPage.CUENTA_ACCESO,5,3
                        )));
    }
}