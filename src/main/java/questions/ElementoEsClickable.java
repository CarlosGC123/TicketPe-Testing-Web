package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.targets.Target;
import util.FormatoConsola;

import java.util.concurrent.TimeUnit;

public class ElementoEsClickable implements Question<Boolean> {

    private final Target target;
    private final int reintentosMax;
    private final long tiempoEspera;

    public ElementoEsClickable(Target target, int reintentosMax, long tiempoEspera) {

        this.target = target;
        this.reintentosMax = reintentosMax;
        this.tiempoEspera = tiempoEspera;
    }

    public static ElementoEsClickable EnElPage(Target target, int reintentosMax, long tiempoEspera) {
        return new ElementoEsClickable(target, reintentosMax, tiempoEspera);

    }

    @Override
    public Boolean answeredBy(Actor actor) {
        int contador = 0;
        int intento = contador + 1;
        while (contador < reintentosMax) {
            try {
                System.out.println("Intentando encontrar el elemento clickable: " + target.getName());
                if (target.resolveFor(actor).isClickable()) {
                    FormatoConsola.exito("Elemento clickable encontrado: " + target.getName() + " en el intento " + intento);
                   return true;
                }

            } catch (Exception e) {
                System.out.println("Reintento número: " + intento);
            }
            contador++;
            intento++;
            try {
                TimeUnit.SECONDS.sleep(tiempoEspera);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.out.println("Tiempo interrumpido: " + ie.getMessage());
            }
        }
        return false;
    }
}
