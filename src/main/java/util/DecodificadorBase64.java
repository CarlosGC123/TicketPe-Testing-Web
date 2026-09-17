package util;

import java.util.Base64;

public class DecodificadorBase64 {

    public static String Base64_Normal(String base64Text) {
        if (base64Text == null || base64Text.trim().isEmpty()) {
            return null;
        }
        return new String(Base64.getDecoder().decode(base64Text));
    }

}
