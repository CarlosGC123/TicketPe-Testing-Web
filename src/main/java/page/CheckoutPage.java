package page;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Elementos del checkout de una reserva.
 *
 * PENDIENTE DE VALIDAR contra el DOM real: el checkout requiere sesión
 * iniciada y una reserva activa, flujo que no se pudo inspeccionar de forma
 * estática al construir este framework. Ajustar en la primera ejecución real
 * — ver README, "Mantenimiento continuo".
 */
public class CheckoutPage extends PageObject {

    public static final Target CAMPO_NUMERO_TARJETA =
            Target.the("Campo para el numero de tarjeta")
                    .located(By.xpath("//input[@name='numeroTarjeta' or @id='numeroTarjeta' or contains(@placeholder,'tarjeta')]"));

    public static final Target BOTON_PAGAR =
            Target.the("Boton Pagar del checkout")
                    .located(By.xpath("//button[contains(text(),'Pagar')]"));

    public static final Target MENSAJE_PAGO_RECHAZADO =
            Target.the("Mensaje de pago rechazado")
                    .located(By.xpath("//*[contains(text(),'rechazad')]"));

    public static final Target CONTADOR_REGRESIVO =
            Target.the("Contador regresivo de expiracion de la reserva")
                    .located(By.xpath("//*[contains(@class,'contador') or contains(@class,'countdown') or contains(@class,'timer')]"));

    public static final Target MENSAJE_RESERVA_EXPIRADA =
            Target.the("Mensaje de reserva expirada")
                    .located(By.xpath("//*[contains(text(),'expir')]"));
}
