package disaster.util;

import java.util.UUID;

public class CryptoUtils {

    public static String generateIntegrityId() {
        return UUID.randomUUID().toString();
    }
}
