package by.afinny.apigateway.util;

import org.springframework.security.core.Authentication;
import java.util.UUID;

public class Utils {
    public static UUID getClientId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }
}
