package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.targets.Target;
import util.FormatoConsola;

import java.util.concurrent.TimeUnit;

/*
    Clase que implementa métodos para
    garantizar que un elemento sea visible
*/

public class ElementoEsVisible implements Question<Boolean> {

    // Atributos de la clase
    private final Target target;
    private final int reintentosMax;
    private final long tiempoEspera;

    //Constructor de la clase
    public ElementoEsVisible(Target target, int reintentosMax, long tiempoEspera) {
        this.target = target;
        this.reintentosMax = reintentosMax;
        this.tiempoEspera = tiempoEspera;
    }

    //Metodo que retorna una instancia de la clase
    public static ElementoEsVisible EnElPage(Target target, int reintentosMax, long tiempoEspera) {
        return new ElementoEsVisible(target,reintentosMax,tiempoEspera);
    }

    /*
     * Metodo que verifica si un
     * elemento es visible en el page con reintentos para robustez
     * @param actor
     * @return booleano
     */

    //Herencia de la clase
    @Override
    public Boolean answeredBy(Actor actor) {

        int contador = 0;
        int intento = contador + 1;
        while (contador < reintentosMax) {
            try {
                //Imprimir lo que va a realizar el script
                System.out.println("Intentando encontrar el elemento: " + target.getName());
                // Validación condicional para preguntarse si el elemento está presente
                if (target.resolveFor(actor).isDisplayed()) {
                    //Si el elemento fue encontrado, debe notificarme o avisarme en la consola de que
                    //fue encontrado
                    FormatoConsola.exito("Elemento encontrado: " + target.getName() + " en el intento " + intento);
                    //Como el elemento fue encontrado, entonces la pregunta (Question) es verdadera
                    return true;
                }
                //Si no se cumple la condición el script mediante el catch debe controlar esa excepcion
            } catch (Exception e) {
                System.out.println("Reintento número: " + intento);
            }

            //Como no lo encontró va a reintentar nuevamente, por eso el contador aumenta en una unidad
            contador++;
            intento++;
            //El script va a congelarse un cierto tiempo "tiempoEspera" para volver a
            //continuar con la ejecucion
            try {
                TimeUnit.SECONDS.sleep(tiempoEspera);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                System.out.println("Tiempo interrumpido: " + ie.getMessage());
            }
        }

        //Esta línea se ejecuta cuando el bucle while ha terminado, es decir
        //cuando los reintentos alcanzaron el límite, lo que funcionalmente quiere decir
        //que el elemento no fue encontrado, respondiendo la pregunta (Question) como falsa
        FormatoConsola.error("No se pudo encontrar el elemento: " + target.getName() + " después de " + reintentosMax + " intentos.");
        return false;
    }
}
