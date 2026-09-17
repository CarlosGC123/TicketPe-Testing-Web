package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import page.EventoDetallePage;
import util.FormatoConsola;

/**
 * Question que valida si la disponibilidad mostrada en la tarjeta del catálogo
 * coincide con la disponibilidad mostrada en la página de detalle del evento.
 * 
 * Normaliza los formatos para comparación:
 * - "42 disp." en tarjeta debe coincidir con "Quedan 42" en detalle
 * - Extrae solo el número para comparar
 */
public class DisponibilidadTarjetaCoincideConDetalle implements Question<Boolean> {

    private final String nombreZona;

    public DisponibilidadTarjetaCoincideConDetalle(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        // Obtener disponibilidad de la tarjeta (almacenado previamente)
        String disponibilidadTarjeta = actor.recall("disponibilidadTarjeta");
        
        // Obtener disponibilidad del detalle
        String disponibilidadDetalle = Text.of(EventoDetallePage.DISPONIBILIDAD_ZONA.of(nombreZona))
                .answeredBy(actor)
                .trim();
        
        FormatoConsola.info("Validando disponibilidad:");
        FormatoConsola.info("  - Disponibilidad en tarjeta: " + disponibilidadTarjeta);
        FormatoConsola.info("  - Disponibilidad en detalle: " + disponibilidadDetalle);
        
        // Extraer números de ambas disponibilidades
        String numeroTarjeta = extraerNumero(disponibilidadTarjeta);
        String numeroDetalle = extraerNumero(disponibilidadDetalle);
        
        boolean coincide = numeroTarjeta.equals(numeroDetalle);
        
        if (coincide) {
            FormatoConsola.info("✓ Las disponibilidades coinciden (" + numeroTarjeta + " entradas)");
        } else {
            FormatoConsola.info("✗ Las disponibilidades NO coinciden");
            FormatoConsola.info("  - Número en tarjeta: " + numeroTarjeta);
            FormatoConsola.info("  - Número en detalle: " + numeroDetalle);
        }
        
        return coincide;
    }

    /**
     * Extrae el número de disponibilidad del texto.
     * - "42 disp." -> "42"
     * - "Quedan 42" -> "42"
     * - "100 disponibles" -> "100"
     */
    private String extraerNumero(String texto) {
        if (texto == null) {
            return "0";
        }
        
        // Extraer solo los números del texto
        String soloNumeros = texto.replaceAll("[^0-9]", "").trim();
        
        // Si está vacío, retornar "0"
        if (soloNumeros.isEmpty()) {
            return "0";
        }
        
        return soloNumeros;
    }

    public static DisponibilidadTarjetaCoincideConDetalle paraLaZona(String nombreZona) {
        return new DisponibilidadTarjetaCoincideConDetalle(nombreZona);
    }
}
