package page;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

/**
 * Elementos de "Mis entradas".
 *
 * PENDIENTE DE VALIDAR contra el DOM real (página autenticada) — ver README,
 * "Mantenimiento continuo".
 */
public class MisEntradasPage extends PageObject {

    public static final Target TARJETA_ENTRADA_DE =
            Target.the("Tarjeta de la entrada del evento {0}")
                    .locatedBy("//*[contains(text(),'{0}')]/ancestor::*[self::li or self::div][1]");

    public static final Target ESTADO_ENTRADA_DE =
            Target.the("Etiqueta de estado de la entrada del evento {0}")
                    .locatedBy("//*[contains(text(),'{0}')]/ancestor::*[self::li or self::div][1]//*[contains(@class,'estado')]");
}
