package io.jenkins.plugins.unblocked.utils;

import hudson.util.Secret;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public class Hmac {

    private static final String ALGORITHM = "HmacSHA256";

    public static String sign(String body, Secret signature) {

        final var mac = secretKey(body, signature);
        final var bytes = body.getBytes(StandardCharsets.UTF_8);
        final var signed = mac.doFinal(bytes);

        return "sha256=" + Hex.encodeHexString(signed);
    }

    private static Mac secretKey(String body, Secret signature) {
        final Mac mac;

        try {
            mac = Mac.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        final var bytes = signature.getPlainText().getBytes(StandardCharsets.UTF_8);
        final var key = new SecretKeySpec(bytes, ALGORITHM);

        try {
            mac.init(key);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return mac;
    }
}
