package runner;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import util.FormatoConsola;

import java.time.LocalDateTime;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        plugin = {"pretty", "json:target/cucumber/cucumber.json"},
        glue = "stepdefinition",
        tags = "@LOGIN",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class CucumberTestSuite {

    private static LocalDateTime horaInicio;

    @BeforeClass
    public static void inicioEjecucion() {
        horaInicio = LocalDateTime.now();
        FormatoConsola.banner("INICIO DE EJECUCIÓN");
        FormatoConsola.tiempo("INICIO");
        FormatoConsola.separador();
    }

    @AfterClass
    public static void finEjecucion() {
        LocalDateTime horaFin = LocalDateTime.now();
        long segundos = java.time.Duration.between(horaInicio, horaFin).getSeconds();
        long minutos  = segundos / 60;
        long segsRest = segundos % 60;

        FormatoConsola.separador();
        FormatoConsola.banner("FIN DE EJECUCIÓN");
        FormatoConsola.tiempo("FIN");
        FormatoConsola.cabeceraTablaClave();
        FormatoConsola.tabla("Hora inicio", horaInicio.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:dd")));
        FormatoConsola.tabla("Hora fin",    horaFin.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:dd")));
        FormatoConsola.tabla("Duración",    minutos + "m " + segsRest + "s");
        FormatoConsola.pieTablaClave();
    }
}

