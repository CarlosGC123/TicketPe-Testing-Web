package task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import page.ChatPage;
import questions.ElementoEsVisible;
import task.clicks.DarClick;
import util.FormatoConsola;

import static net.serenitybdd.screenplay.actions.Enter.theValue;

/**
 * Envía un mensaje al chat del asistente de IA, abriendo el widget primero si
 * hace falta. Lógica de Negocio (CTAL-TAE v2.0): usada por TC-WEB-06 y
 * TC-WEB-07.
 */
public class EnviarMensajeAlChat implements Task {

    private final String mensaje;

    private EnviarMensajeAlChat(String mensaje) {
        this.mensaje = mensaje;
    }

    public static EnviarMensajeAlChat conTexto(String mensaje) {
        return new EnviarMensajeAlChat(mensaje);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        if (!ElementoEsVisible.EnElPage(ChatPage.CAMPO_MENSAJE, 3, 1).answeredBy(actor)) {
            actor.attemptsTo(DarClick.enElElemento(ChatPage.BOTON_ABRIR_CHAT));
        }
        actor.attemptsTo(theValue(mensaje).into(ChatPage.CAMPO_MENSAJE));
        actor.attemptsTo(DarClick.enElElemento(ChatPage.BOTON_ENVIAR));
        FormatoConsola.paso("Mensaje enviado al chat: " + mensaje);
    }
}
