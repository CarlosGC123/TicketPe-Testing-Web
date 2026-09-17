package interaction;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.model.util.EnvironmentVariables;
import org.openqa.selenium.WebDriver;
import page.DashboardPage;
import page.Login;
import questions.ElementoEsVisible;
import task.EscribeTexto;
import task.clicks.DarClick;
import util.FormatoConsola;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task para iniciar sesión en la UI con credenciales almacenadas en el estado del actor.
 */
public class IniciarSesionEnUI implements Task {

    private final WebDriver navegador;
    private final EnvironmentVariables environmentVariables;

    public IniciarSesionEnUI(WebDriver navegador, EnvironmentVariables environmentVariables) {
        this.navegador = navegador;
        this.environmentVariables = environmentVariables;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String correo = actor.recall("correo");
        String contrasena = actor.recall("contrasena");
        
        String entorno = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("baseurl");
        
        actor.wasAbleTo(
                CargarPaginaPrincipal.EnLaUrl(navegador, entorno, DashboardPage.BOTON_INGRESAR, 2, 1)
        );
        actor.attemptsTo(DarClick.enElElemento(DashboardPage.BOTON_INGRESAR));
        actor.attemptsTo(EscribeTexto.enElElemento(Login.CAMPO_CORREO, correo));
        actor.attemptsTo(EscribeTexto.enElElemento(Login.CAMPO_CONTRASENA, contrasena));
        actor.attemptsTo(DarClick.enElElemento(Login.BOTON_INGRESAR));
        actor.should(seeThat(ElementoEsVisible.EnElPage(DashboardPage.CUENTA_ACCESO, 5, 3)));
        
        FormatoConsola.paso("Sesión iniciada en UI");
    }

    public static IniciarSesionEnUI conCredencialesDelActor(WebDriver navegador, EnvironmentVariables environmentVariables) {
        return instrumented(IniciarSesionEnUI.class, navegador, environmentVariables);
    }
}
