package task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.targets.Target;
import questions.ElementoEsVisible;
import util.FormatoConsola;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class EscribeTexto implements Task {

    private final Target target;
    private final String texto;

    // Constructor de la clase
    public EscribeTexto(Target target, String texto) {
        this.target = target;
        this.texto = texto;
    }

    //Metodo para instanciar la clase
    public static EscribeTexto enElElemento(Target target, String texto) {
        return new EscribeTexto(target, texto);
    }

    // Metodo para realizar la acción de dar clic en un elemento
    @Override
    public <T extends Actor> void performAs(T actor){

        try {
            actor.should(seeThat(ElementoEsVisible.EnElPage(target,5,3)));
            actor.attemptsTo(Enter.theValue(texto).into(target));
            FormatoConsola.exito("Se escribió el texto: '" + texto + "' en el elemento: " + target.getName());
        }
        catch (Exception e) {
            FormatoConsola.error("Error al escribir el texto: '" + texto + "' en el elemento: " + target.getName() + " - " + e.getMessage());
            throw new AssertionError("No se pudo escribir el texto: '" + texto + "' en el elemento: " + target.getName() + " después de varios intentos.");
        }
    }
}
