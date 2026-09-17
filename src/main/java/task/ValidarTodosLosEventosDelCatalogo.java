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
 * 
 * Lógica de validación:
 * - Precio: El precio de la tarjeta debe aparecer en AL MENOS UNA zona del detalle
 * - Disponibilidad: La suma de disponibilidades de TODAS las zonas debe coincidir con la tarjeta
 * - Lista de espera: Si está agotado, solo valida precio (disponibilidad = 0)
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
        List<Map<String, String>> eventosExitosos = new ArrayList<>();
        int eventosValidados = 0;
        
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
                
                FormatoConsola.info("  Catálogo → Precio: " + precioTarjeta + " | Disponibilidad: " + disponibilidadTarjeta);
                
                // Abrir la ficha del evento
                actor.attemptsTo(AbrirFichaDeEvento.porNombre(nombreEvento));
                
                // Esperar a que cargue la página de detalle
                Thread.sleep(1500);
                
                // Extraer todas las zonas del detalle
                Map<String, Object> datosDetalle = extraerDatosDelDetalle(actor);
                
                // Validar precio y disponibilidad
                Map<String, Object> resultadoValidacion = validarDatos(precioTarjeta, disponibilidadTarjeta, datosDetalle);
                
                boolean precioCoincide = (boolean) resultadoValidacion.get("precioCoincide");
                boolean disponibilidadCoincide = (boolean) resultadoValidacion.get("disponibilidadCoincide");
                String motivoPrecio = (String) resultadoValidacion.get("motivoPrecio");
                String motivoDisponibilidad = (String) resultadoValidacion.get("motivoDisponibilidad");
                
                if (!precioCoincide || !disponibilidadCoincide) {
                    Map<String, String> eventoFallido = new HashMap<>();
                    eventoFallido.put("nombre", nombreEvento);
                    eventoFallido.put("precioCoincide", String.valueOf(precioCoincide));
                    eventoFallido.put("disponibilidadCoincide", String.valueOf(disponibilidadCoincide));
                    eventoFallido.put("precioTarjeta", precioTarjeta);
                    eventoFallido.put("disponibilidadTarjeta", disponibilidadTarjeta);
                    eventoFallido.put("datosDetalle", datosDetalle.toString());
                    eventoFallido.put("motivoPrecio", motivoPrecio);
                    eventoFallido.put("motivoDisponibilidad", motivoDisponibilidad);
                    eventosFallidos.add(eventoFallido);
                    
                    FormatoConsola.error("✗ FALLO: " + nombreEvento);
                    if (!precioCoincide) {
                        FormatoConsola.error("  - Precio NO coincide: " + motivoPrecio);
                    }
                    if (!disponibilidadCoincide) {
                        FormatoConsola.error("  - Disponibilidad NO coincide: " + motivoDisponibilidad);
                    }
                } else {
                    Map<String, String> eventoExitoso = new HashMap<>();
                    eventoExitoso.put("nombre", nombreEvento);
                    eventoExitoso.put("precioTarjeta", precioTarjeta);
                    eventoExitoso.put("disponibilidadTarjeta", disponibilidadTarjeta);
                    eventoExitoso.put("datosDetalle", datosDetalle.toString());
                    eventosExitosos.add(eventoExitoso);
                    
                    FormatoConsola.info("✓ ÉXITO: " + nombreEvento);
                    FormatoConsola.info("  Detalle → " + datosDetalle.get("resumen"));
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
        actor.remember("eventosExitosos", eventosExitosos.size());
        actor.remember("eventosExitososDetalle", eventosExitosos);
        
        FormatoConsola.separador();
        FormatoConsola.banner("RESUMEN DE VALIDACIÓN");
        FormatoConsola.info("Total validados: " + eventosValidados);
        FormatoConsola.info("Exitosos: " + eventosExitosos.size());
        FormatoConsola.info("Fallidos: " + eventosFallidos.size());
    }
    
    /**
     * Extrae todas las zonas del detalle con sus precios y disponibilidades.
     * Retorna un Map con:
     * - zonas: List<Map> con nombre, precio y disponibilidad de cada zona
     * - totalDisponibilidad: suma de todas las disponibilidades
     * - precios: lista de todos los precios encontrados
     * - listaDeEspera: boolean indicando si hay "Lista de espera"
     */
    private Map<String, Object> extraerDatosDelDetalle(Actor actor) {
        Map<String, Object> datos = new HashMap<>();
        List<Map<String, String>> zonas = new ArrayList<>();
        List<String> precios = new ArrayList<>();
        int totalDisponibilidad = 0;
        boolean listaDeEspera = false;
        
        try {
            // Buscar todas las filas de zonas (pueden tener diferentes estructuras)
            List<WebElement> filasZonas = BrowseTheWeb.as(actor).getDriver()
                    .findElements(By.xpath("//div[contains(@class,'fila-zona') or @data-testid[starts-with(.,'zona-')]]"));
            
            FormatoConsola.info("  Zonas encontradas en detalle: " + filasZonas.size());
            
            for (WebElement filaZona : filasZonas) {
                try {
                    Map<String, String> zona = new HashMap<>();
                    
                    // Extraer nombre de la zona
                    List<WebElement> nombresZona = filaZona.findElements(By.xpath(".//div[contains(@class,'zona-nombre')]"));
                    String nombreZona = nombresZona.isEmpty() ? "Desconocida" : nombresZona.get(0).getText().trim();
                    zona.put("nombre", nombreZona);
                    
                    // Extraer precio
                    List<WebElement> preciosZona = filaZona.findElements(By.xpath(".//span[contains(@class,'zona-precio')]"));
                    String precioZona = preciosZona.isEmpty() ? "" : preciosZona.get(0).getText().trim();
                    zona.put("precio", precioZona);
                    if (!precioZona.isEmpty()) {
                        precios.add(precioZona);
                    }
                    
                    // Extraer disponibilidad
                    List<WebElement> disponibilidadesZona = filaZona.findElements(By.xpath(".//div[contains(@class,'mono') and contains(@class,'ayuda')]"));
                    String disponibilidadTexto = disponibilidadesZona.isEmpty() ? "" : disponibilidadesZona.get(0).getText().trim();
                    zona.put("disponibilidad", disponibilidadTexto);
                    
                    // Verificar si es "Lista de espera"
                    if (disponibilidadTexto.toLowerCase().contains("lista de espera")) {
                        listaDeEspera = true;
                        zona.put("disponibilidadNumero", "0");
                    } else {
                        // Extraer número de disponibilidad
                        String numeroDisp = disponibilidadTexto.replaceAll("[^0-9]", "");
                        if (!numeroDisp.isEmpty()) {
                            int dispNum = Integer.parseInt(numeroDisp);
                            totalDisponibilidad += dispNum;
                            zona.put("disponibilidadNumero", String.valueOf(dispNum));
                        } else {
                            zona.put("disponibilidadNumero", "0");
                        }
                    }
                    
                    zonas.add(zona);
                    FormatoConsola.info("    - " + nombreZona + ": " + precioZona + " | " + disponibilidadTexto);
                    
                } catch (Exception e) {
                    FormatoConsola.error("    Error al extraer datos de una zona: " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            FormatoConsola.error("  Error al extraer zonas del detalle: " + e.getMessage());
        }
        
        datos.put("zonas", zonas);
        datos.put("totalDisponibilidad", totalDisponibilidad);
        datos.put("precios", precios);
        datos.put("listaDeEspera", listaDeEspera);
        datos.put("resumen", zonas.size() + " zona(s), Total disponible: " + 
                (listaDeEspera ? "Lista de espera" : totalDisponibilidad));
        
        return datos;
    }
    
    /**
     * Valida que los datos de la tarjeta coincidan con los del detalle.
     * 
     * Reglas:
     * 1. Precio: El precio de la tarjeta debe aparecer en AL MENOS UNA zona del detalle
     *    - Si la tarjeta dice "Comprar desde S/ X", buscar que X aparezca en alguna zona
     *    - Si la tarjeta dice "Gratis" o "S/ 0", buscar que aparezca en alguna zona
     * 2. Disponibilidad: La suma de disponibilidades de TODAS las zonas debe coincidir con la tarjeta
     *    - Si hay "Lista de espera", la disponibilidad debe ser 0 o "Agotado"
     */
    private Map<String, Object> validarDatos(String precioTarjeta, String disponibilidadTarjeta, 
                                              Map<String, Object> datosDetalle) {
        Map<String, Object> resultado = new HashMap<>();
        
        @SuppressWarnings("unchecked")
        List<String> preciosDetalle = (List<String>) datosDetalle.get("precios");
        int totalDisponibilidadDetalle = (int) datosDetalle.get("totalDisponibilidad");
        boolean listaDeEspera = (boolean) datosDetalle.get("listaDeEspera");
        
        // Validar precio
        boolean precioCoincide = validarPrecio(precioTarjeta, preciosDetalle);
        String motivoPrecio = precioCoincide ? "OK" : 
                "Tarjeta: '" + precioTarjeta + "' no encontrado en detalle: " + preciosDetalle;
        
        // Validar disponibilidad
        boolean disponibilidadCoincide;
        String motivoDisponibilidad;
        
        if (listaDeEspera) {
            // Si hay lista de espera, la tarjeta debe mostrar "Agotado" o disponibilidad 0
            disponibilidadCoincide = disponibilidadTarjeta.toLowerCase().contains("agotado") || 
                                     extraerNumeroDisponibilidad(disponibilidadTarjeta) == 0;
            motivoDisponibilidad = disponibilidadCoincide ? "OK (Lista de espera)" : 
                    "Tarjeta: '" + disponibilidadTarjeta + "' pero detalle tiene Lista de espera";
        } else {
            // Comparar suma de disponibilidades
            int disponibilidadTarjetaNum = extraerNumeroDisponibilidad(disponibilidadTarjeta);
            disponibilidadCoincide = (disponibilidadTarjetaNum == totalDisponibilidadDetalle);
            motivoDisponibilidad = disponibilidadCoincide ? "OK" : 
                    "Tarjeta: " + disponibilidadTarjetaNum + " | Detalle (suma): " + totalDisponibilidadDetalle;
        }
        
        resultado.put("precioCoincide", precioCoincide);
        resultado.put("disponibilidadCoincide", disponibilidadCoincide);
        resultado.put("motivoPrecio", motivoPrecio);
        resultado.put("motivoDisponibilidad", motivoDisponibilidad);
        
        return resultado;
    }
    
    /**
     * Valida que el precio de la tarjeta aparezca en al menos uno de los precios del detalle.
     * Maneja casos especiales:
     * - "Comprar desde S/ X" → busca que X aparezca en algún precio del detalle
     * - "Gratis" → busca "S/ 0" o "Gratis" en el detalle
     */
    private boolean validarPrecio(String precioTarjeta, List<String> preciosDetalle) {
        if (precioTarjeta == null || preciosDetalle == null || preciosDetalle.isEmpty()) {
            return false;
        }
        
        // Normalizar precio de la tarjeta
        String precioTarjetaNormalizado = normalizarPrecio(precioTarjeta);
        
        // Extraer el precio mínimo si dice "Comprar desde S/ X"
        if (precioTarjeta.toLowerCase().contains("comprar desde")) {
            // Extraer el número después de "S/"
            String[] partes = precioTarjeta.split("S/");
            if (partes.length > 1) {
                String precioMinimo = "s/" + partes[1].trim().split("\\s+")[0];
                precioTarjetaNormalizado = normalizarPrecio(precioMinimo);
            }
        }
        
        // Buscar coincidencia en algún precio del detalle
        for (String precioDetalle : preciosDetalle) {
            String precioDetalleNormalizado = normalizarPrecio(precioDetalle);
            if (precioTarjetaNormalizado.equals(precioDetalleNormalizado)) {
                return true;
            }
            // También verificar si "gratis" coincide con "s/0"
            if ((precioTarjetaNormalizado.equals("gratis") && precioDetalleNormalizado.equals("s/0")) ||
                (precioTarjetaNormalizado.equals("s/0") && precioDetalleNormalizado.equals("gratis"))) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Extrae el número de disponibilidad de un texto.
     * Ejemplos: "42 disp." → 42, "Quedan 42" → 42, "Agotado" → 0
     */
    private int extraerNumeroDisponibilidad(String disponibilidad) {
        if (disponibilidad == null || disponibilidad.trim().isEmpty()) {
            return 0;
        }
        if (disponibilidad.toLowerCase().contains("agotado")) {
            return 0;
        }
        String numero = disponibilidad.replaceAll("[^0-9]", "");
        if (numero.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(numero);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Normaliza un precio para comparación.
     * Ejemplos: "S/ 50" → "s/50", "Gratis" → "gratis", "S/ 0" → "s/0"
     */
    private String normalizarPrecio(String precio) {
        if (precio == null) return "";
        return precio.trim().toLowerCase().replace(" ", "");
    }

    public static ValidarTodosLosEventosDelCatalogo conPrecioYDisponibilidad() {
        return instrumented(ValidarTodosLosEventosDelCatalogo.class);
    }
}
