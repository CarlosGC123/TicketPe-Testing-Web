package stepdefinition;

import interaction.CargarPaginaPrincipal;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.annotations.Steps;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
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

        if(environmentVariables == null) {
            environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        }

        String entorno = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("baseurl");
        FormatoConsola.banner("ENTORNO DE PRUEBA : " + entorno);
        
        // Asignar la habilidad BrowseTheWeb al actor
        theActorCalled(actor.getName()).can(BrowseTheWeb.with(navegador));
        
        theActorInTheSpotlight().wasAbleTo(
                CargarPaginaPrincipal.EnLaUrl(
                        navegador,
                        entorno,
                        DashboardPage.BOTON_INGRESAR,
                        2,
                        1
                )
        );
        // El boton "Ingresar" del home solo abre el formulario de login; sin este
        // clic, el correo/contrasena nunca aparecen en pantalla (causa raiz del
        // fallo observado en CI: reintentos infinitos buscando el campo correo).
        theActorInTheSpotlight().attemptsTo(
                DarClick.enElElemento(DashboardPage.BOTON_INGRESAR)
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
        // Submit del formulario de login: es un boton distinto al "Ingresar" del
        // home (ese solo abre el formulario, ver UsuarioIngresaPaginTicketPe).
        theActorInTheSpotlight().attemptsTo(
                DarClick.enElElemento(
                        Login.BOTON_INGRESAR)
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