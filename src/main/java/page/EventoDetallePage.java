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
                    .locatedBy("//*[contains(text(),'S/') or contains(text(),'Gratis')]");

    public static final Target DISPONIBLE_TIPO_ENTRADA =
            Target.the("Disponible del tipo de entrada {0}")
                    .locatedBy("//*[contains(text(),'{0}')]/ancestor::*[self::li or self::div][1]//*[contains(text(),'disp.')]");

    // Selectores específicos para validación de coincidencia catálogo vs detalle
    public static final Target ZONA_POR_NOMBRE =
            Target.the("Zona {0} en la página de detalle")
                    .locatedBy("//div[@data-testid='zona-{0}' or contains(@class,'fila-zona')]");

    public static final Target NOMBRE_ZONA =
            Target.the("Nombre de la zona {0}")
                    .locatedBy("//div[@data-testid='zona-{0}']//div[contains(@class,'zona-nombre')]");

    public static final Target DISPONIBILIDAD_ZONA =
            Target.the("Disponibilidad de la zona {0}")
                    .locatedBy("//div[@data-testid='zona-{0}']//div[contains(@class,'mono') and contains(@class,'ayuda')]");

    public static final Target PRECIO_ZONA =
            Target.the("Precio de la zona {0}")
                    .locatedBy("//div[@data-testid='zona-{0}']//span[contains(@class,'zona-precio')]");

    public static final Target BOTON_VOLVER_AL_CATALOGO =
            Target.the("Botón volver al catálogo")
                    .locatedBy("//a[contains(@class,'btn-texto') and contains(@href,'/catalogo') and contains(text(),'volver al catálogo')]");
}
