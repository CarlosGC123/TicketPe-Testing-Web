package task;

import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import util.ClienteApiCore;
import util.FormatoConsola;

import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task para seleccionar un evento con venta abierta desde el catálogo
 * y almacenar su información en el estado del actor.
 */
public class SeleccionarEventoConVentaAbierta implements Task {

    private final boolean requiereDisponibilidad;

    public SeleccionarEventoConVentaAbierta(boolean requiereDisponibilidad) {
        this.requiereDisponibilidad = requiereDisponibilidad;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Response catalogo = ClienteApiCore.get("/eventos?limite=50");
        List<Map<String, Object>> eventos = catalogo.jsonPath().getList("eventos");

        for (Map<String, Object> evento : eventos) {
            String id = String.valueOf(evento.get("id"));
            Response disponibilidad = ClienteApiCore.disponibilidad(id);
            List<Map<String, Object>> tipos = disponibilidad.jsonPath().getList("disponibilidad");
            
            if (tipos == null || tipos.isEmpty()) {
                continue;
            }
            
            var tipoAbierto = tipos.stream()
                    .filter(t -> Boolean.TRUE.equals(t.get("venta_abierta"))
                            && (!requiereDisponibilidad || ((Number) t.get("disponible")).intValue() > 0))
                    .findFirst();
                    
            if (tipoAbierto.isPresent()) {
                actor.remember("eventoId", id);
                actor.remember("eventoNombre", String.valueOf(evento.get("nombre")));
                actor.remember("tipoEntradaId", String.valueOf(tipoAbierto.get().get("id")));
                actor.remember("tipoEntradaNombre", String.valueOf(tipoAbierto.get().get("nombre")));
                
                FormatoConsola.info("Evento seleccionado: " + evento.get("nombre"));
                return;
            }
        }
        
        throw new AssertionError("No se encontró ningún evento con venta abierta" +
                (requiereDisponibilidad ? " y disponible > 0" : "") +
                " (caso bloqueado por precondición no controlable)");
    }

    public static SeleccionarEventoConVentaAbierta conDisponibilidad() {
        return instrumented(SeleccionarEventoConVentaAbierta.class, true);
    }
    
    public static SeleccionarEventoConVentaAbierta sinRequerirDisponibilidad() {
        return instrumented(SeleccionarEventoConVentaAbierta.class, false);
    }
}
