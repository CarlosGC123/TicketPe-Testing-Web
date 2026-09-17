package page;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;
import org.openqa.selenium.By;

/**
 * Elementos del widget de chat del asistente de IA.
 *
 * PENDIENTE DE VALIDAR contra el DOM real: el chat es un widget flotante que
 * no aparece en un fetch estático de la página. Ajustar en la primera
 * ejecución real — ver README, "Mantenimiento continuo".
 */
public class ChatPage extends PageObject {

    public static final Target BOTON_ABRIR_CHAT =
            Target.the("Boton para abrir el chat")
                    .located(By.xpath("//button[contains(@aria-label,'chat') or contains(@class,'chat')]"));

    public static final Target CAMPO_MENSAJE =
            Target.the("Campo de mensaje del chat")
                    .located(By.xpath("//textarea[contains(@placeholder,'ensaje')] | //input[contains(@placeholder,'ensaje')]"));

    public static final Target BOTON_ENVIAR =
            Target.the("Boton enviar del chat")
                    .located(By.xpath("//button[contains(@aria-label,'enviar') or @type='submit'][ancestor::*[contains(@class,'chat')]]"));

    public static final Target ULTIMA_RESPUESTA =
            Target.the("Ultima respuesta del asistente en el chat")
                    .located(By.xpath("(//*[contains(@class,'mensaje-asistente') or contains(@class,'assistant')])[last()]"));

    public static final Target ULTIMO_MENSAJE_USUARIO =
            Target.the("Ultimo mensaje del usuario en el chat")
                    .located(By.xpath("(//*[contains(@class,'mensaje-usuario') or contains(@class,'user')])[last()]"));
}
