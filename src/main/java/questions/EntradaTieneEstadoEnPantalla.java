package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import page.MisEntradasPage;
import util.FormatoConsola;

/**
 * TC-WEB-05: la tarjeta de una entrada en "Mis entradas" debe mostrar el
 * estado esperado (p. ej. "válida", "en trámite de reembolso", "usada").
 */
public class EntradaTieneEstadoEnPantalla implements Question<Boolean> {

    private final String nombreEvento;
    private final String estadoEsperado;

    private EntradaTieneEstadoEnPantalla(String nombreEvento, String estadoEsperado) {
        this.nombreEvento = nombreEvento;
        this.estadoEsperado = estadoEsperado;
    }

    public static EntradaTieneEstadoEnPantalla delEvento(String nombreEvento, String estadoEsperado) {
        return new EntradaTieneEstadoEnPantalla(nombreEvento, estadoEsperado);
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        String estadoVisible = MisEntradasPage.ESTADO_ENTRADA_DE.of(nombreEvento).resolveFor(actor).getText();
        boolean coincide = estadoVisible.toLowerCase().contains(estadoEsperado.toLowerCase());
        FormatoConsola.info("Estado esperado: " + estadoEsperado + " | mostrado: " + estadoVisible);
        return coincide;
    }
}
