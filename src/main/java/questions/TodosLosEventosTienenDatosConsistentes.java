package questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import util.FormatoConsola;

import java.util.List;
import java.util.Map;

/**
 * Question para verificar que todos los eventos del catálogo tienen datos consistentes
 * entre la tarjeta del catálogo y la página de detalle.
 * 
 * Si hay eventos con inconsistencias, lanza un AssertionError con la lista detallada
 * de eventos fallidos.
 */
public class TodosLosEventosTienenDatosConsistentes implements Question<Boolean> {

    @Override
    public Boolean answeredBy(Actor actor) {
        List<Map<String, String>> eventosFallidos = actor.recall("eventosFallidos");
        Integer eventosValidados = actor.recall("eventosValidados");
        Integer eventosExitosos = actor.recall("eventosExitosos");
        
        if (eventosFallidos == null) {
            throw new AssertionError("No se encontraron resultados de validación. " +
                    "Asegúrese de ejecutar la validación antes de verificar los resultados.");
        }
        
        if (eventosFallidos.isEmpty()) {
            FormatoConsola.banner("✓ VALIDACIÓN EXITOSA");
            FormatoConsola.info("Todos los eventos (" + eventosValidados + ") tienen datos consistentes");
            FormatoConsola.info("entre el catálogo y la página de detalle");
            return true;
        }
        
        // Construir mensaje de error detallado
        StringBuilder mensajeError = new StringBuilder();
        mensajeError.append("\n");
        mensajeError.append("═══════════════════════════════════════════════════════════════\n");
        mensajeError.append("  VALIDACIÓN FALLIDA: Inconsistencias encontradas\n");
        mensajeError.append("═══════════════════════════════════════════════════════════════\n");
        mensajeError.append("\n");
        mensajeError.append("RESUMEN:\n");
        mensajeError.append("  • Total de eventos validados: ").append(eventosValidados).append("\n");
        mensajeError.append("  • Eventos exitosos: ").append(eventosExitosos).append("\n");
        mensajeError.append("  • Eventos con inconsistencias: ").append(eventosFallidos.size()).append("\n");
        mensajeError.append("\n");
        mensajeError.append("EVENTOS CON INCONSISTENCIAS:\n");
        mensajeError.append("───────────────────────────────────────────────────────────────\n");
        
        int contador = 1;
        for (Map<String, String> eventoFallido : eventosFallidos) {
            mensajeError.append("\n");
            mensajeError.append(contador).append(". ").append(eventoFallido.get("nombre")).append("\n");
            
            if (eventoFallido.containsKey("error")) {
                mensajeError.append("   ERROR: ").append(eventoFallido.get("error")).append("\n");
            } else {
                String precioCoincide = eventoFallido.get("precioCoincide");
                String disponibilidadCoincide = eventoFallido.get("disponibilidadCoincide");
                
                if ("false".equals(precioCoincide)) {
                    mensajeError.append("   ✗ Precio NO coincide\n");
                    mensajeError.append("     Catálogo: ").append(eventoFallido.get("precioTarjeta")).append("\n");
                }
                
                if ("false".equals(disponibilidadCoincide)) {
                    mensajeError.append("   ✗ Disponibilidad NO coincide\n");
                    mensajeError.append("     Catálogo: ").append(eventoFallido.get("disponibilidadTarjeta")).append("\n");
                }
            }
            
            contador++;
        }
        
        mensajeError.append("\n");
        mensajeError.append("═══════════════════════════════════════════════════════════════\n");
        mensajeError.append("  ACCIÓN REQUERIDA: Revisar los eventos listados arriba\n");
        mensajeError.append("═══════════════════════════════════════════════════════════════\n");
        
        // Lanzar AssertionError con el mensaje detallado
        throw new AssertionError(mensajeError.toString());
    }

    public static TodosLosEventosTienenDatosConsistentes entreCatalogoYDetalle() {
        return new TodosLosEventosTienenDatosConsistentes();
    }
}
