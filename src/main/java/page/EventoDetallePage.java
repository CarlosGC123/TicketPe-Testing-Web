package page;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

/**
 * Elementos de la ficha de un evento (`/catalogo/:id`).
 *
 * Mantenimiento: verificado solo por el texto público del catálogo, no por la
 * ficha de detalle en sí (requiere abrir cada evento). Validar y ajustar en la
 * primera ejecución real — ver README, "Mantenimiento continuo".
 */
public class EventoDetallePage extends PageObject {

    public static final Target ETIQUETA_AGOTADO =
            Target.the("Etiqueta AGOTADO en la ficha del evento")
                    .locatedBy("//*[contains(text(),'AGOTADO')]");

    public static final Target BOTON_COMPRAR =
            Target.the("Boton Comprar en la ficha del evento")
                    .locatedBy("//button[contains(text(),'Comprar')]");

    public static final Target PRECIO_TIPO_ENTRADA =
            Target.the("Precio del tipo de entrada {0}")
                    .locatedBy("//*[contains(text(),'{0}')]/ancestor::*[self::li or self::div][1]//*[contains(text(),'S/') or contains(text(),'Gratis')]");

    public static final Target DISPONIBLE_TIPO_ENTRADA =
            Target.the("Disponible del tipo de entrada {0}")
                    .locatedBy("//*[contains(text(),'{0}')]/ancestor::*[self::li or self::div][1]//*[contains(text(),'disp.')]");
}
