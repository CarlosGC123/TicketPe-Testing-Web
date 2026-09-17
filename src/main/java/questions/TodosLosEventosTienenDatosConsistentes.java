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
 * de eventos fallidos y exitosos.
 */
public class TodosLosEventosTienenDatosConsistentes implements Question<Boolean> {

    @Override
    public Boolean answeredBy(Actor actor) {
        List<Map<String, String>> eventosFallidos = actor.recall("eventosFallidos");
        List<Map<String, String>> eventosExitososDetalle = actor.recall("eventosExitososDetalle");
        Integer eventosValidados = actor.recall("eventosValidados");
        Integer eventosExitosos = actor.recall("eventosExitosos");
        
        if (eventosFallidos == null) {
            throw new AssertionError("No se encontraron resultados de validación. " +
                    "Asegúrese de ejecutar la validación antes de verificar los resultados.");
        }
        
        if (eventosFallidos.isEmpty()) {
            // Construir mensaje de éxito detallado
            StringBuilder mensajeExito = new StringBuilder();
            mensajeExito.append("\n");
            mensajeExito.append("═══════════════════════════════════════════════════════════════\n");
            mensajeExito.append("  ✓ VALIDACIÓN EXITOSA\n");
            mensajeExito.append("═══════════════════════════════════════════════════════════════\n");
            mensajeExito.append("\n");
            mensajeExito.append("RESUMEN:\n");
            mensajeExito.append("  • Total de eventos validados: ").append(eventosValidados).append("\n");
            mensajeExito.append("  • Todos los eventos tienen datos consistentes\n");
            mensajeExito.append("\n");
            
            if (eventosExitososDetalle != null && !eventosExitososDetalle.isEmpty()) {
                mensajeExito.append("EVENTOS VALIDADOS EXITOSAMENTE:\n");
                mensajeExito.append("───────────────────────────────────────────────────────────────\n");
                int contador = 1;
                for (Map<String, String> evento : eventosExitososDetalle) {
                    mensajeExito.append("\n").append(contador).append(". ").append(evento.get("nombre")).append("\n");
                    mensajeExito.append("   ✓ Catálogo: ").append(evento.get("precioTarjeta"))
                            .append(" | ").append(evento.get("disponibilidadTarjeta")).append("\n");
                    mensajeExito.append("   ✓ Detalle: ").append(evento.get("datosDetalle")).append("\n");
                    contador++;
                }
            }
            
            mensajeExito.append("\n");
            mensajeExito.append("═══════════════════════════════════════════════════════════════\n");
            
            FormatoConsola.banner("✓ VALIDACIÓN EXITOSA");
            FormatoConsola.info("Todos los eventos (" + eventosValidados + ") tienen datos consistentes");
            FormatoConsola.info("entre el catálogo y la página de detalle");
            System.out.println(mensajeExito.toString());
            
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
        
        // Mostrar eventos exitosos primero (si los hay)
        if (eventosExitososDetalle != null && !eventosExitososDetalle.isEmpty()) {
            mensajeError.append("EVENTOS EXITOSOS (").append(eventosExitosos).append("):\n");
            mensajeError.append("───────────────────────────────────────────────────────────────\n");
            for (Map<String, String> evento : eventosExitososDetalle) {
                mensajeError.append("  ✓ ").append(evento.get("nombre")).append("\n");
                mensajeError.append("    Catálogo: ").append(evento.get("precioTarjeta"))
                        .append(" | ").append(evento.get("disponibilidadTarjeta")).append("\n");
                mensajeError.append("    Detalle: ").append(evento.get("datosDetalle")).append("\n");
            }
            mensajeError.append("\n");
        }
        
        mensajeError.append("EVENTOS CON INCONSISTENCIAS (").append(eventosFallidos.size()).append("):\n");
        mensajeError.append("───────────────────────────────────────────────────────────────\n");
        
        int contador = 1;
        for (Map<String, String> eventoFallido : eventosFallidos) {
            mensajeError.append("\n");
            mensajeError.append(contador).append(". ").append(eventoFallido.get("nombre")).append("\n");
            
            if (eventoFallido.containsKey("error")) {
                mensajeError.append("   ✗ ERROR: ").append(eventoFallido.get("error")).append("\n");
            } else {
                String precioCoincide = eventoFallido.get("precioCoincide");
                String disponibilidadCoincide = eventoFallido.get("disponibilidadCoincide");
                
                // Mostrar datos del catálogo
                mensajeError.append("   Catálogo:\n");
                mensajeError.append("     • Precio: ").append(eventoFallido.get("precioTarjeta")).append("\n");
                mensajeError.append("     • Disponibilidad: ").append(eventoFallido.get("disponibilidadTarjeta")).append("\n");
                
                // Mostrar datos del detalle
                if (eventoFallido.containsKey("datosDetalle")) {
                    mensajeError.append("   Detalle:\n");
                    mensajeError.append("     • ").append(eventoFallido.get("datosDetalle")).append("\n");
                }
                
                // Mostrar validaciones
                mensajeError.append("   Validación:\n");
                if ("false".equals(precioCoincide)) {
                    mensajeError.append("     ✗ Precio NO coincide\n");
                    if (eventoFallido.containsKey("motivoPrecio")) {
                        mensajeError.append("       Motivo: ").append(eventoFallido.get("motivoPrecio")).append("\n");
                    }
                } else {
                    mensajeError.append("     ✓ Precio coincide\n");
                }
                
                if ("false".equals(disponibilidadCoincide)) {
                    mensajeError.append("     ✗ Disponibilidad NO coincide\n");
                    if (eventoFallido.containsKey("motivoDisponibilidad")) {
                        mensajeError.append("       Motivo: ").append(eventoFallido.get("motivoDisponibilidad")).append("\n");
                    }
                } else {
                    mensajeError.append("     ✓ Disponibilidad coincide\n");
                }
            }
            
            contador++;
        }
        
        mensajeError.append("\n");
        mensajeError.append("═══════════════════════════════════════════════════════════════\n");
        mensajeError.append("  ACCIÓN REQUERIDA: Revisar los eventos con inconsistencias\n");
        mensajeError.append("═══════════════════════════════════════════════════════════════\n");
        
        // Lanzar AssertionError con el mensaje detallado
        throw new AssertionError(mensajeError.toString());
    }

    public static TodosLosEventosTienenDatosConsistentes entreCatalogoYDetalle() {
        return new TodosLosEventosTienenDatosConsistentes();
    }
}
