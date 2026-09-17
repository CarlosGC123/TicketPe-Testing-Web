package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import page.ChatPage;

/**
 * TC-WEB-07: si el usuario escribe HTML/JS en el chat, debe mostrarse como
 * texto literal — nunca ejecutarse (OWASP A03, XSS, RSK-25). No se usa
 * alert() en el payload para no bloquear el navegador; en su lugar se marca
 * una variable global (window.__xss) que NO debe existir si el mensaje se
 * escapó correctamente.
 */
public class MensajeDelUsuarioSeMuestraComoTextoLiteral implements Question<Boolean> {

    public static MensajeDelUsuarioSeMuestraComoTextoLiteral enElChat() {
        return new MensajeDelUsuarioSeMuestraComoTextoLiteral();
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        String textoMostrado = ChatPage.ULTIMO_MENSAJE_USUARIO.resolveFor(actor).getText();
        boolean seMuestraLiteral = textoMostrado.contains("<img") || textoMostrado.contains("onerror");

        WebDriver navegador = BrowseTheWeb.as(actor).getDriver();
        Object marcaXss = ((JavascriptExecutor) navegador).executeScript("return window.__xss;");

        return seMuestraLiteral && marcaXss == null;
    }
}
