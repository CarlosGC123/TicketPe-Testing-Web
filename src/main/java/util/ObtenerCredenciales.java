package util;

import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.util.EnvironmentVariables;

public class ObtenerCredenciales {

    private static EnvironmentVariables environmentVariables = net.thucydides.model.environment.SystemEnvironmentVariables.currentEnvironmentVariables();

    public static String ObtenerCorreo(){
        // Primero intenta leer de variable de entorno
        String correoEnv = System.getenv("CORREO");
        if (correoEnv != null && !correoEnv.trim().isEmpty()) {
            return DecodificadorBase64.Base64_Normal(correoEnv);
        }
        
        // Si no existe, lee de serenity.properties
        String correoProps = EnvironmentSpecificConfiguration.from(environmentVariables).getOptionalProperty("correo").orElse(null);
        return DecodificadorBase64.Base64_Normal(correoProps);
    }

    public static String ObtenerContrasena(){
        // Primero intenta leer de variable de entorno
        String passwordEnv = System.getenv("PASSWORD");
        if (passwordEnv != null && !passwordEnv.trim().isEmpty()) {
            return DecodificadorBase64.Base64_Normal(passwordEnv);
        }
        
        // Si no existe, lee de serenity.properties
        String passwordProps = EnvironmentSpecificConfiguration.from(environmentVariables).getOptionalProperty("password").orElse(null);
        return DecodificadorBase64.Base64_Normal(passwordProps);
    }


}
