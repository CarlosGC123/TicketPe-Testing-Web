package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import page.EventoDetallePage;
import util.FormatoConsola;

/**
 * Question que valida si el precio mostrado en la tarjeta del catálogo
 * coincide con el precio mostrado en la página de detalle del evento.
 * 
 * Normaliza los formatos para comparación:
 * - "Gratis" en tarjeta debe coincidir con "S/ 0" o "Gratis" en detalle
 * - "S/ X" debe coincidir exactamente
 */
public class PrecioTarjetaCoincideConDetalle implements Question<Boolean> {

    private final String nombreZona;

    public PrecioTarjetaCoincideConDetalle(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        // Obtener precio de la tarjeta (almacenado previamente)
        String precioTarjeta = actor.recall("precioTarjeta");
        
        // Obtener precio del detalle
        String precioDetalle = Text.of(EventoDetallePage.PRECIO_ZONA.of(nombreZona))
                .answeredBy(actor)
                .trim();
        
        FormatoConsola.info("Validando precio:");
        FormatoConsola.info("  - Precio en tarjeta: " + precioTarjeta);
        FormatoConsola.info("  - Precio en detalle: " + precioDetalle);
        
        // Normalizar precios para comparación
        String precioTarjetaNormalizado = normalizarPrecio(precioTarjeta);
        String precioDetalleNormalizado = normalizarPrecio(precioDetalle);
        
        boolean coincide = precioTarjetaNormalizado.equals(precioDetalleNormalizado);
        
        if (coincide) {
            FormatoConsola.info("✓ Los precios coinciden");
        } else {
            FormatoConsola.info("✗ Los precios NO coinciden");
            FormatoConsola.info("  - Normalizado tarjeta: " + precioTarjetaNormalizado);
            FormatoConsola.info("  - Normalizado detalle: " + precioDetalleNormalizado);
        }
        
        return coincide;
    }

    /**
     * Normaliza el precio para comparación.
     * - "Gratis" -> "0"
     * - "S/ 0" -> "0"
     * - "S/ 50" -> "50"
     */
    private String normalizarPrecio(String precio) {
        if (precio == null) {
            return "";
        }
        
        precio = precio.trim().toUpperCase();
        
        // Si es "Gratis", normalizar a "0"
        if (precio.contains("GRATIS")) {
            return "0";
        }
        
        // Extraer solo los números del precio
        String soloNumeros = precio.replaceAll("[^0-9.]", "").trim();
        
        // Si está vacío, es 0
        if (soloNumeros.isEmpty()) {
            return "0";
        }
        
        return soloNumeros;
    }

    public static PrecioTarjetaCoincideConDetalle paraLaZona(String nombreZona) {
        return new PrecioTarjetaCoincideConDetalle(nombreZona);
    }
}
