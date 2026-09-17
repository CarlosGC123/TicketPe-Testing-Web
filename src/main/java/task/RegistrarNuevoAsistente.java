package task;

import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import util.ClienteApiCore;
import util.FormatoConsola;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task para registrar un nuevo asistente por API y almacenar sus credenciales
 * en el estado del actor para uso posterior.
 */
public class RegistrarNuevoAsistente implements Task {

    @Override
    public <T extends Actor> void performAs(T actor) {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String correo = "asistente-" + uuid + "@testathon.local";
        String contrasena = "Pass123!";

        Map<String, Object> body = new HashMap<>();
        body.put("correo", correo);
        body.put("password", contrasena);
        body.put("nombre", "Asistente " + uuid);

        Response registro = ClienteApiCore.post("/auth/registro", body, null);
        if (registro.statusCode() != 201) {
            throw new AssertionError("Registro falló con status " + registro.statusCode()
                    + ": " + registro.body().asString());
        }
        
        String token = registro.jsonPath().getString("token");
        
        // Almacenar en el estado del actor
        actor.remember("correo", correo);
        actor.remember("contrasena", contrasena);
        actor.remember("token", token);
        
        FormatoConsola.info("Asistente registrado: " + correo);
    }

    public static RegistrarNuevoAsistente porAPI() {
        return instrumented(RegistrarNuevoAsistente.class);
    }
}
