package task.clicks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;
import questions.ElementoEsClickable;
import questions.ElementoEsVisible;
import util.FormatoConsola;

import java.util.concurrent.TimeUnit;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

/*
    Clase que implementa metodos para dar click en un target
    tiene incorporado reintentos para garantizar el click
*/

public class DarClick implements Task {

    private final Target target;

    // Constructor de la clase
    public DarClick(Target target) {
        this.target = target;
    }

    //Metodo para instanciar la clase
    public static DarClick enElElemento(Target target) {
        return new DarClick(target);
    }

    // Metodo para realizar la acción de dar clic en un elemento
    @Override
    public <T extends Actor> void performAs(T actor){

        try {
            actor.should(seeThat(ElementoEsVisible.EnElPage(target,20,3)));
            actor.should(seeThat(ElementoEsClickable.EnElPage(target,20,3)));
            actor.attemptsTo(Click.on(target));
            FormatoConsola.exito("Se dio click al elemento: " + target.getName());
        }
        catch (Exception e) {
            throw new AssertionError("No se pudo dar click al elemento : " + target.getName() + " después de varios intentos.");
        }
    }
}