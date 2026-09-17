package task;

import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import util.ClienteApiCore;
import util.FormatoConsola;

import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task para crear una reserva por API usando los datos del evento y tipo de entrada
 * almacenados en el estado del actor.
 */
public class CrearReservaPorAPI implements Task {

    private final int cantidad;

    public CrearReservaPorAPI(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        String tipoEntradaId = actor.recall("tipoEntradaId");
        
        Map<String, Object> body = new HashMap<>();
        body.put("tipo_entrada_id", Integer.parseInt(tipoEntradaId));
        body.put("cantidad", cantidad);
        
        Response reserva = ClienteApiCore.post("/reservas", body, token);
        if (reserva.statusCode() != 201) {
            throw new AssertionError("Creación de reserva falló con status " + reserva.statusCode()
                    + ": " + reserva.body().asString());
        }
        
        String reservaId = String.valueOf(reserva.jsonPath().getInt("id"));
        actor.remember("reservaId", reservaId);
        
        FormatoConsola.info("Reserva creada: " + reservaId);
    }

    public static CrearReservaPorAPI conCantidad(int cantidad) {
        return instrumented(CrearReservaPorAPI.class, cantidad);
    }
}
