package util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Utilidad de Librería Base para desplazar la hora que ve la página (Date /
 * Date.now), sin depender de permisos de CDP. Técnica: se reemplaza el
 * constructor `Date` del contexto de la página por uno que aplica un offset
 * fijo en milisegundos — funcionalmente equivalente a mover el reloj del
 * sistema, pero solo dentro del JS de la página.
 *
 * Uso: TC-WEB-04 (la cuenta regresiva del checkout usa la hora del servidor,
 * R3 §ESC02). Si el checkout calcula el conteo con `performance.now()` en vez
 * de `Date`, este mock no aplica y hay que extenderlo — ver README, sección
 * "Mantenimiento continuo".
 */
public class NavegadorReloj {

    private NavegadorReloj() { }

    public static void adelantar(WebDriver navegador, long minutos) {
        long offsetMs = minutos * 60_000L;
        String script =
                "const offset = arguments[0];" +
                "const RealDate = Date;" +
                "Date = class extends RealDate {" +
                "  constructor(...args) {" +
                "    if (args.length === 0) { super(RealDate.now() + offset); }" +
                "    else { super(...args); }" +
                "  }" +
                "  static now() { return RealDate.now() + offset; }" +
                "};";
        ((JavascriptExecutor) navegador).executeScript(script, offsetMs);
        FormatoConsola.info("Reloj del navegador adelantado " + minutos + " minutos (offset JS, no CDP)");
    }
}
