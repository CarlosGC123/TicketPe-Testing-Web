package util;

import java.util.Base64;

public class DecodificadorBase64 {

    public static String Base64_Normal(String base64Text) {
        return new String(Base64.getDecoder().decode(base64Text));
    }

}
