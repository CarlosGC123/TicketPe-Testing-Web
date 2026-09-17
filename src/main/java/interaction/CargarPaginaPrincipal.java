package interaction;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.actions.Open;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import questions.ElementoEsVisible;
import util.FormatoConsola;

import java.util.concurrent.TimeUnit;

public class CargarPaginaPrincipal implements Task {

    private final String url;
    private final Target target1;
    private final int max_intentos;
    private final long tiempo_espera;
    private final WebDriver navegador;

    public CargarPaginaPrincipal(WebDriver navegador, String url, Target target1, int maxIntentos, long tiempoEspera) {
        this.url = url;
        this.target1 = target1;
        this.max_intentos = maxIntentos;
        this.tiempo_espera = tiempoEspera;
        this.navegador = navegador;
    }

    public static CargarPaginaPrincipal EnLaUrl(WebDriver navegador, String url,Target target1, int maxIntentos, long tiempoEspera) {
        return new CargarPaginaPrincipal(navegador, url, target1, maxIntentos, tiempoEspera);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        try{
            actor.can(BrowseTheWeb.with(navegador));
        } catch (Exception e) {
            throw new AssertionError("Error al asignar la habilidad de navegación: " + e.getMessage());
        }

        FormatoConsola.titulo("CARGANDO PÁGINA PRINCIPAL");

        for (int intento = 1; intento <= max_intentos; intento++) {
            try {
                actor.attemptsTo(Open.url(url));
                FormatoConsola.paso("Abriendo la URL: " + url + " (intento " + intento + ")");
            } catch (TimeoutException te) {
                System.out.println("Timeout al cargar la URL (intento " + intento + "), verificando elemento...");
            } catch (Exception e) {
                FormatoConsola.error("Error al abrir la URL en el intento " + intento + ": " + e.getMessage());
                throw new AssertionError("Error al abrir la URL: " + e.getMessage());
            }

            try {
                if (ElementoEsVisible.EnElPage(target1, 20, 2).answeredBy(actor)) {
                    FormatoConsola.exito("Pagina cargada correctamente en el intento " + intento);
                    return;
                }
            } catch (Exception e) {
                FormatoConsola.advertencia("Elemento no visible en el intento " + intento + ", intentando recargar...");
            }

            if (intento < max_intentos) {
                try {
                    FormatoConsola.info("Esperando " + tiempo_espera + " segundos antes de recargar...");
                    TimeUnit.SECONDS.sleep(tiempo_espera);
                    try {
                        navegador.navigate().refresh();
                    } catch (TimeoutException te) {
                        System.out.println("Timeout al recargar, continuando...");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Error al esperar: " + e.getMessage());
                    return;
                }
            }
        }

        FormatoConsola.error("No se pudo cargar la página después de " + max_intentos + " intentos.");
        throw new AssertionError("No se pudo cargar la página después de " + max_intentos + " intentos.");
    }
}
