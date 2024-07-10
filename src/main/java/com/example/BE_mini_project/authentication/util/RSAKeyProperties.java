package com.example.BE_mini_project.authentication.util;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

@Log
@Component
public class RSAKeyProperties {

    private static final String PUBLIC_KEY_FILE = "src/main/resources/certs/public-key.pem";
    private static final String PRIVATE_KEY_FILE = "src/main/resources/certs/private-key.pem";

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAKeyProperties() {
        try {
            if (keysExist()) {
                loadKeys();
            } else {
                generateAndSaveKeys();
            }
        } catch (Exception e) {
            log.severe("Failed to initialize RSA keys: " + e.getMessage());
        }
    }

    private boolean keysExist() {
        File publicKeyFile = new File(PUBLIC_KEY_FILE);
        File privateKeyFile = new File(PRIVATE_KEY_FILE);
        return publicKeyFile.exists() && privateKeyFile.exists();
    }

    private void loadKeys() throws Exception {
        this.publicKey = (RSAPublicKey) loadKey(PUBLIC_KEY_FILE, true);
        this.privateKey = (RSAPrivateKey) loadKey(PRIVATE_KEY_FILE, false);
        log.info("RSA keys loaded from files.");
    }

    private void generateAndSaveKeys() throws Exception {
        KeyPair pair = KeyGeneratorUtility.generateRsaKey();
        this.publicKey = (RSAPublicKey) pair.getPublic();
        this.privateKey = (RSAPrivateKey) pair.getPrivate();
        saveKey(this.publicKey, PUBLIC_KEY_FILE);
        saveKey(this.privateKey, PRIVATE_KEY_FILE);
        log.info("RSA keys generated and saved to files.");
    }

    private void saveKey(Key key, String fileName) throws IOException {
        byte[] keyBytes = key.getEncoded();
        String keyString = Base64.getEncoder().encodeToString(keyBytes);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("-----BEGIN " + (key instanceof RSAPublicKey ? "PUBLIC" : "PRIVATE") + " KEY-----");
            writer.newLine();
            writer.write(keyString);
            writer.newLine();
            writer.write("-----END " + (key instanceof RSAPublicKey ? "PUBLIC" : "PRIVATE") + " KEY-----");
        }
    }

    private Key loadKey(String fileName, boolean isPublicKey) throws Exception {
        StringBuilder keyBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("-----")) continue;
                keyBuilder.append(line);
            }
        }
        byte[] keyBytes = Base64.getDecoder().decode(keyBuilder.toString());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        if (isPublicKey) {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return keyFactory.generatePublic(spec);
        } else {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return keyFactory.generatePrivate(spec);
        }
    }

    public RSAPublicKey getPublicKey() {
        log.info(this.publicKey.toString());
        return this.publicKey;
    }

    public void setPublicKey(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        log.info(this.privateKey.toString());
        return this.privateKey;
    }

    public void setPrivateKey(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}