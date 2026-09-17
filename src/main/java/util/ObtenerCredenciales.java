package util;

import util.DecodificadorBase64;

public class ObtenerCredenciales {

    public static String ObtenerCorreo(){
        return DecodificadorBase64.Base64_Normal(System.getenv("CORREO"));
    }

    public static String ObtenerContrasena(){
        return DecodificadorBase64.Base64_Normal(System.getenv("PASSWORD"));
    }


}
