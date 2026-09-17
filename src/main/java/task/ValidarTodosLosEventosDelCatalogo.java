package task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import page.CatalogoPage;
import page.EventoDetallePage;
import util.FormatoConsola;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task para validar precio y disponibilidad de todos los eventos del catálogo.
 * Itera sobre cada evento, valida que los datos del catálogo coincidan con el detalle,
 * y acumula los eventos que fallan la validación.
 */
public class ValidarTodosLosEventosDelCatalogo implements Task {

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Obtener la lista de eventos almacenada previamente
        List<String> nombresEventos = actor.recall("todosLosEventos");
        
        if (nombresEventos == null || nombresEventos.isEmpty()) {
            throw new AssertionError("No se encontraron eventos en el catálogo para validar");
        }
        
        List<Map<String, String>> eventosFallidos = new ArrayList<>();
        int eventosValidados = 0;
        int eventosExitosos = 0;
        
        FormatoConsola.banner("INICIANDO VALIDACIÓN DE TODOS LOS EVENTOS");
        FormatoConsola.info("Total de eventos a validar: " + nombresEventos.size());
        
        for (String nombreEvento : nombresEventos) {
            eventosValidados++;
            FormatoConsola.separador();
            FormatoConsola.info("Validando evento " + eventosValidados + "/" + nombresEventos.size() + ": " + nombreEvento);
            
            try {
                // Seleccionar el evento en el catálogo
                actor.attemptsTo(SeleccionarEventoEnCatalogo.porNombre(nombreEvento));
                
                // Extraer datos de la tarjeta del catálogo
                String precioTarjeta = actor.recall("precioTarjeta");
                String disponibilidadTarjeta = actor.recall("disponibilidadTarjeta");
                
                // Abrir la ficha del evento
                actor.attemptsTo(AbrirFichaDeEvento.porNombre(nombreEvento));
                
                // Esperar a que cargue la página de detalle
                Thread.sleep(1500);
                
                // Validar precio y disponibilidad en el detalle
                boolean precioCoincide = validarPrecioEnDetalle(actor, precioTarjeta);
                boolean disponibilidadCoincide = validarDisponibilidadEnDetalle(actor, disponibilidadTarjeta);
                
                if (!precioCoincide || !disponibilidadCoincide) {
                    Map<String, String> eventoFallido = new HashMap<>();
                    eventoFallido.put("nombre", nombreEvento);
                    eventoFallido.put("precioCoincide", String.valueOf(precioCoincide));
                    eventoFallido.put("disponibilidadCoincide", String.valueOf(disponibilidadCoincide));
                    eventoFallido.put("precioTarjeta", precioTarjeta);
                    eventoFallido.put("disponibilidadTarjeta", disponibilidadTarjeta);
                    eventosFallidos.add(eventoFallido);
                    
                    FormatoConsola.error("✗ FALLO: " + nombreEvento);
                    if (!precioCoincide) {
                        FormatoConsola.error("  - Precio no coincide");
                    }
                    if (!disponibilidadCoincide) {
                        FormatoConsola.error("  - Disponibilidad no coincide");
                    }
                } else {
                    eventosExitosos++;
                    FormatoConsola.info("✓ ÉXITO: " + nombreEvento);
                }
                
                // Volver al catálogo para continuar con el siguiente evento
                actor.attemptsTo(VolverAlCatalogo.desdeLaFichaDelEvento());
                
                // Esperar un poco antes de procesar el siguiente evento
                Thread.sleep(1000);
                
            } catch (Exception e) {
                Map<String, String> eventoFallido = new HashMap<>();
                eventoFallido.put("nombre", nombreEvento);
                eventoFallido.put("error", e.getMessage());
                eventosFallidos.add(eventoFallido);
                
                FormatoConsola.error("✗ ERROR al validar: " + nombreEvento + " - " + e.getMessage());
                
                // Intentar volver al catálogo incluso si hubo error
                try {
                    actor.attemptsTo(VolverAlCatalogo.desdeLaFichaDelEvento());
                } catch (Exception ex) {
                    // Si no se puede volver, navegar directamente al catálogo
                    FormatoConsola.error("No se pudo usar el botón volver, navegando directamente al catálogo");
                }
            }
        }
        
        // Almacenar resultados en el estado del actor
        actor.remember("eventosFallidos", eventosFallidos);
        actor.remember("eventosValidados", eventosValidados);
        actor.remember("eventosExitosos", eventosExitosos);
        
        FormatoConsola.separador();
        FormatoConsola.banner("RESUMEN DE VALIDACIÓN");
        FormatoConsola.info("Total validados: " + eventosValidados);
        FormatoConsola.info("Exitosos: " + eventosExitosos);
        FormatoConsola.info("Fallidos: " + eventosFallidos.size());
    }
    
    private boolean validarPrecioEnDetalle(Actor actor, String precioTarjeta) {
        try {
            // Buscar el precio en la zona General del detalle
            List<WebElement> preciosDetalle = BrowseTheWeb.as(actor).getDriver()
                    .findElements(By.xpath("//div[@data-testid='zona-General']//span[contains(@class,'zona-precio')]"));
            
            if (preciosDetalle.isEmpty()) {
                return false;
            }
            
            String precioDetalle = preciosDetalle.get(0).getText().trim();
            
            // Normalizar precios para comparación
            String precioTarjetaNormalizado = normalizarPrecio(precioTarjeta);
            String precioDetalleNormalizado = normalizarPrecio(precioDetalle);
            
            return precioTarjetaNormalizado.equals(precioDetalleNormalizado);
        } catch (Exception e) {
            FormatoConsola.error("Error al validar precio: " + e.getMessage());
            return false;
        }
    }
    
    private boolean validarDisponibilidadEnDetalle(Actor actor, String disponibilidadTarjeta) {
        try {
            // Buscar la disponibilidad en la zona General del detalle
            List<WebElement> disponibilidadesDetalle = BrowseTheWeb.as(actor).getDriver()
                    .findElements(By.xpath("//div[@data-testid='zona-General']//div[contains(@class,'mono') and contains(@class,'ayuda')]"));
            
            if (disponibilidadesDetalle.isEmpty()) {
                return false;
            }
            
            String disponibilidadDetalle = disponibilidadesDetalle.get(0).getText().trim();
            
            // Normalizar disponibilidades para comparación
            String disponibilidadTarjetaNormalizada = normalizarDisponibilidad(disponibilidadTarjeta);
            String disponibilidadDetalleNormalizada = normalizarDisponibilidad(disponibilidadDetalle);
            
            return disponibilidadTarjetaNormalizada.equals(disponibilidadDetalleNormalizada);
        } catch (Exception e) {
            FormatoConsola.error("Error al validar disponibilidad: " + e.getMessage());
            return false;
        }
    }
    
    private String normalizarPrecio(String precio) {
        if (precio == null) return "";
        // Eliminar espacios y convertir a minúsculas
        return precio.trim().toLowerCase().replace(" ", "");
    }
    
    private String normalizarDisponibilidad(String disponibilidad) {
        if (disponibilidad == null) return "";
        // Extraer solo el número de disponibilidad
        // Ejemplo: "42 disp." -> "42", "Quedan 42" -> "42"
        String normalizado = disponibilidad.replaceAll("[^0-9]", "");
        return normalizado;
    }

    public static ValidarTodosLosEventosDelCatalogo conPrecioYDisponibilidad() {
        return instrumented(ValidarTodosLosEventosDelCatalogo.class);
    }
}
