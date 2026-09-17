package task;

import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import util.ClienteApiCore;
import util.FormatoConsola;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task para comprar entradas por API (crear reserva + pagar).
 */
public class ComprarEntradasPorAPI implements Task {

    private final int cantidad;

    public ComprarEntradasPorAPI(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        String tipoEntradaId = actor.recall("tipoEntradaId");
        
        // Crear reserva
        Map<String, Object> bodyReserva = new HashMap<>();
        bodyReserva.put("tipo_entrada_id", Integer.parseInt(tipoEntradaId));
        bodyReserva.put("cantidad", cantidad);
        
        Response reserva = ClienteApiCore.post("/reservas", bodyReserva, token);
        if (reserva.statusCode() != 201) {
            throw new AssertionError("Creación de reserva falló con status " + reserva.statusCode());
        }
        
        int reservaId = reserva.jsonPath().getInt("id");
        
        // Pagar con tarjeta de prueba aprobada
        Map<String, Object> bodyPago = new HashMap<>();
        bodyPago.put("numero_tarjeta", "4242424242424242");
        bodyPago.put("cvv", "123");
        bodyPago.put("mes_expiracion", 12);
        bodyPago.put("anio_expiracion", 2030);
        
        Response pago = ClienteApiCore.post("/reservas/" + reservaId + "/pago", bodyPago, token);
        if (pago.statusCode() != 200) {
            throw new AssertionError("Pago falló con status " + pago.statusCode());
        }
        
        List<Integer> entradasIds = pago.jsonPath().getList("entradas.id", Integer.class);
        actor.remember("entradasIds", entradasIds);
        
        FormatoConsola.info("Compra completada: " + cantidad + " entrada(s)");
    }

    public static ComprarEntradasPorAPI conCantidad(int cantidad) {
        return instrumented(ComprarEntradasPorAPI.class, cantidad);
    }
}
