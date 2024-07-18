package com.example.BE_mini_project.authentication.util;

import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.Resource;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class PemUtils {
    public static PublicKey readPublicKeyFromResource(Resource resource, String algorithm) throws Exception {
        try (InputStreamReader keyReader = new InputStreamReader(resource.getInputStream());
             PemReader pemReader = new PemReader(keyReader)) {

            byte[] content = pemReader.readPemObject().getContent();
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
            KeyFactory factory = KeyFactory.getInstance(algorithm);
            return factory.generatePublic(pubKeySpec);
        }
    }
}