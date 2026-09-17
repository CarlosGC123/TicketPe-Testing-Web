package task;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.model.util.EnvironmentVariables;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import page.CatalogoPage;
import util.FormatoConsola;

import java.util.ArrayList;
import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task para obtener la lista de todos los eventos disponibles en el catálogo.
 * Navega a /catalogo y extrae los nombres de todos los eventos visibles.
 */
public class ObtenerTodosLosEventosDelCatalogo implements Task {

    private final EnvironmentVariables environmentVariables;

    public ObtenerTodosLosEventosDelCatalogo(EnvironmentVariables environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Navegar al catálogo
        String baseUrl = EnvironmentSpecificConfiguration.from(environmentVariables).getProperty("baseurl");
        String urlCatalogo = baseUrl + "/catalogo";
        
        BrowseTheWeb.as(actor).getDriver().get(urlCatalogo);
        
        // Esperar a que cargue la página
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Obtener todos los elementos h3 dentro de las tarjetas de eventos
        List<WebElement> elementosNombres = BrowseTheWeb.as(actor).getDriver()
                .findElements(By.xpath("//a[@data-testid='tarjeta-evento']//h3"));
        
        List<String> nombresEventos = new ArrayList<>();
        for (WebElement elemento : elementosNombres) {
            String nombre = elemento.getText().trim();
            if (!nombre.isEmpty()) {
                nombresEventos.add(nombre);
            }
        }
        
        // Almacenar la lista en el estado del actor
        actor.remember("todosLosEventos", nombresEventos);
        
        FormatoConsola.info("Eventos encontrados en el catálogo: " + nombresEventos.size());
        for (String nombre : nombresEventos) {
            FormatoConsola.info("  - " + nombre);
        }
    }

    public static ObtenerTodosLosEventosDelCatalogo delSitio(EnvironmentVariables environmentVariables) {
        return instrumented(ObtenerTodosLosEventosDelCatalogo.class, environmentVariables);
    }
}
