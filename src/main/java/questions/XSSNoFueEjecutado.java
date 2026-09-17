package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import util.FormatoConsola;

/**
 * Question para verificar que el código XSS no fue ejecutado en el navegador.
 */
public class XSSNoFueEjecutado implements Question<Boolean> {

    @Override
    public Boolean answeredBy(Actor actor) {
        WebDriver navegador = BrowseTheWeb.as(actor).getDriver();
        Object marcaXss = ((JavascriptExecutor) navegador).executeScript("return window.__xss;");
        
        if (marcaXss != null) {
            FormatoConsola.info("XSS ejecutado: window.__xss está definido (valor: " + marcaXss + ")");
            return false;
        }
        
        FormatoConsola.info("window.__xss no está definido: XSS bloqueado correctamente");
        return true;
    }

    public static XSSNoFueEjecutado enElNavegador() {
        return new XSSNoFueEjecutado();
    }
}
