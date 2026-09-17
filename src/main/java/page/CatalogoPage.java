package page;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

/**
 * Elementos del catálogo (`/catalogo`). Selectores ancla por texto visible,
 * en el mismo estilo ya usado en {@link DashboardPage#OPCION_TORNEO_AJEDREZ}.
 *
 * Verificado contra el contenido real de /catalogo (setiembre 2026): cada
 * tarjeta muestra "S/ &lt;precio&gt;" o "Gratis", "&lt;N&gt; disp." o "Lista de
 * espera", y la etiqueta "AGOTADO" cuando no hay cupo.
 *
 * Mantenimiento: si el catálogo cambia de diseño (otro texto, otra clase),
 * estos xpaths dejan de encontrar el elemento — ver README, "Mantenimiento
 * continuo".
 */
public class CatalogoPage extends PageObject {

    public static final Target TARJETA_EVENTO =
            Target.the("Tarjeta del evento {0}")
                    .locatedBy("//a[@data-testid='tarjeta-evento' and contains(.,'{0}')]");

    public static final Target ETIQUETA_AGOTADO_DE =
            Target.the("Etiqueta AGOTADO de la tarjeta del evento {0}")
                    .locatedBy("//a[.//*[contains(text(),'{0}')]]//*[contains(text(),'AGOTADO')]");

    public static final Target DISPONIBLE_DE =
            Target.the("Disponibilidad mostrada en la tarjeta del evento {0}")
                    .locatedBy("//a[.//*[contains(text(),'{0}')]]//*[contains(text(),'disp.')]");

    public static final Target PRECIO_DE =
            Target.the("Precio mostrado en la tarjeta del evento {0}")
                    .locatedBy("//a[.//*[contains(text(),'{0}')]]//*[contains(text(),'S/') or contains(text(),'Gratis')]");

    public static final Target TODAS_LAS_TARJETAS_EVENTOS =
            Target.the("Todas las tarjetas de eventos en el catálogo")
                    .locatedBy("//a[@data-testid='tarjeta-evento']");

    public static final Target NOMBRE_EVENTO_EN_TARJETA =
            Target.the("Nombre del evento en la tarjeta")
                    .locatedBy("//a[@data-testid='tarjeta-evento']//h3");
}
