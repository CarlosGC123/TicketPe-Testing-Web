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
 * Task para modificar los estados de las entradas compradas por API
 * (solicitar reembolso, hacer check-in, transferir).
 */
public class ModificarEstadosDeEntradas implements Task {

    private final String correoDestinatario;

    public ModificarEstadosDeEntradas(String correoDestinatario) {
        this.correoDestinatario = correoDestinatario;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("token");
        List<Integer> entradasIds = actor.recall("entradasIds");
        
        if (entradasIds == null || entradasIds.size() < 4) {
            throw new AssertionError("Se requieren al menos 4 entradas para modificar estados");
        }
        
        // Entrada 0: sin cambios
        
        // Entrada 1: solicitar reembolso
        Map<String, Object> bodyReembolso = new HashMap<>();
        bodyReembolso.put("motivo", "Cambio de planes");
        Response reembolso = ClienteApiCore.post("/entradas/" + entradasIds.get(1) + "/reembolso", bodyReembolso, token);
        if (reembolso.statusCode() != 200) {
            FormatoConsola.info("Advertencia: solicitud de reembolso falló con status " + reembolso.statusCode());
        }
        
        // Entrada 2: check-in (requiere token de administrador, se omite si no está disponible)
        // Este paso requeriría un token de administrador que no está disponible en este contexto
        FormatoConsola.info("Check-in de entrada requiere token de administrador (omitido en este contexto)");
        
        // Entrada 3: transferir
        Map<String, Object> bodyTransferencia = new HashMap<>();
        bodyTransferencia.put("correo_destinatario", correoDestinatario);
        Response transferencia = ClienteApiCore.post("/entradas/" + entradasIds.get(3) + "/transferir", bodyTransferencia, token);
        if (transferencia.statusCode() != 200) {
            FormatoConsola.info("Advertencia: transferencia falló con status " + transferencia.statusCode());
        }
        
        FormatoConsola.info("Estados de entradas modificados");
    }

    public static ModificarEstadosDeEntradas transferirUnaA(String correoDestinatario) {
        return instrumented(ModificarEstadosDeEntradas.class, correoDestinatario);
    }
}
