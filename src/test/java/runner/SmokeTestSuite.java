package runner;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import util.FormatoConsola;

import java.time.LocalDateTime;

/**
 * Runner para suite de SMOKE: casos críticos y rápidos que se ejecutan en cada
 * push/PR a main (R1 §10.4). Incluye 6 casos:
 * - Login (autenticación básica)
 * - TC-WEB-01: Precio/disponibilidad coincide con API
 * - TC-WEB-02: Evento agotado se muestra correctamente
 * - TC-WEB-03: Pago rechazado permite reintentar
 * - TC-WEB-06: Chat pide confirmación antes de reservar (seguridad)
 * - TC-WEB-07: Chat previene XSS (seguridad)
 *
 * Criterios de inclusión (R1 §10.5):
 * - Ejecución rápida (< 2 min por caso)
 * - Datos propios creados por API
 * - Sin esperas largas
 * - Críticos para el negocio o seguridad
 *
 * Uso en CI/CD: mvn clean verify -Dtest=SmokeTestSuite
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        plugin = {"pretty", "json:target/cucumber/cucumber.json"},
        glue = "stepdefinition",
        tags = "@smoke and not @manual",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class SmokeTestSuite {

    private static LocalDateTime horaInicio;

    @BeforeClass
    public static void inicioEjecucion() {
        horaInicio = LocalDateTime.now();
        FormatoConsola.banner("SMOKE TEST SUITE - INICIO");
        FormatoConsola.info("Suite crítica para continuous testing (cada push/PR)");
        FormatoConsola.info("Casos incluidos: Login, TC-WEB-01, TC-WEB-02, TC-WEB-03, TC-WEB-06, TC-WEB-07");
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
        FormatoConsola.banner("SMOKE TEST SUITE - FIN");
        FormatoConsola.tiempo("FIN");
        FormatoConsola.cabeceraTablaClave();
        FormatoConsola.tabla("Hora inicio", horaInicio.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        FormatoConsola.tabla("Hora fin",    horaFin.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        FormatoConsola.tabla("Duración",    minutos + "m " + segsRest + "s");
        FormatoConsola.pieTablaClave();
    }
}
