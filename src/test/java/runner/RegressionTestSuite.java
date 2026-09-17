package runner;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import util.FormatoConsola;

import java.time.LocalDateTime;

/**
 * Runner para suite de REGRESIÓN COMPLETA: todos los casos automatizados Web
 * que se ejecutan en corrida nocturna programada (R1 §10.4). Incluye 8 casos:
 * - Login (autenticación básica)
 * - TC-WEB-01: Precio/disponibilidad coincide con API
 * - TC-WEB-02: Evento agotado se muestra correctamente
 * - TC-WEB-03: Pago rechazado permite reintentar
 * - TC-WEB-04: Cuenta regresiva usa hora del servidor
 * - TC-WEB-05: "Mis entradas" muestra estados reales
 * - TC-WEB-06: Chat pide confirmación antes de reservar (seguridad)
 * - TC-WEB-07: Chat previene XSS (seguridad)
 *
 * Criterios de inclusión (R1 §10.5):
 * - Todos los casos automatizados Web
 * - Datos propios creados por API
 * - Independencia entre casos
 * - Cobertura completa de flujos críticos
 *
 * Uso en CI/CD: mvn clean verify -Dtest=RegressionTestSuite
 * Uso local: mvn test -Dtest=RegressionTestSuite
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features",
        plugin = {"pretty", "json:target/cucumber/cucumber.json"},
        glue = "stepdefinition",
        tags = "@regression and not @manual",
        snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class RegressionTestSuite {

    private static LocalDateTime horaInicio;

    @BeforeClass
    public static void inicioEjecucion() {
        horaInicio = LocalDateTime.now();
        FormatoConsola.banner("REGRESSION TEST SUITE - INICIO");
        FormatoConsola.info("Suite completa de regresión (corrida nocturna programada)");
        FormatoConsola.info("Casos incluidos: Login + TC-WEB-01 a TC-WEB-07 (8 casos totales)");
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
        FormatoConsola.banner("REGRESSION TEST SUITE - FIN");
        FormatoConsola.tiempo("FIN");
        FormatoConsola.cabeceraTablaClave();
        FormatoConsola.tabla("Hora inicio", horaInicio.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        FormatoConsola.tabla("Hora fin",    horaFin.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        FormatoConsola.tabla("Duración",    minutos + "m " + segsRest + "s");
        FormatoConsola.tabla("Casos ejecutados", "8 (Login + TC-WEB-01 a TC-WEB-07)");
        FormatoConsola.pieTablaClave();
    }
}
