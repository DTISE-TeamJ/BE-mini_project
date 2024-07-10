package com.example.BE_mini_project.authentication.util;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

/*
@Log
@Component
public class RSAKeyProperties {

    private static final String PUBLIC_KEY_FILE = "publicKey.pem";
    private static final String PRIVATE_KEY_FILE = "privateKey.pem";


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
*/

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

/*

-----BEGIN PRIVATE KEY-----
MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCtMczIU7/nao7B5gXnoQFONTw6yVUT2mYnZvkDSzKMbaV8w9YA4/IDCRFpo1jZe6gODyQPHfI4SdohNWzPGRjmuAOkXwgFfm4INPSxDFRcgm2t1U2FFbFD/ubxKm2sEPepHXdUKWXoU9+FUaQkgt4EhL2rPO1XApzXVNnaHBiSjjs8o8VCtA8KSfu45/WfRJYeCnejFkSWG/XxKbfcnPJQt7Gri2tfH2CfuskOlGcmb6mr29cI74rk6Wrg5JsziWHXtLOy7AwsfDSXbMyy9GmajAQIybRcgNOqobFP0Oe2gwfNotK5S19WrjvSGRCTMAWREpdKiHv7q/6uI0hGbQS7AgMBAAECggEANjm8WxBVqjfjoHPLb30qcnkadXFp0MB1cWq9FjnqBquhv5F1JGw57pddO0zoWZdsw5IcARuoJdWBlRosCh9ae4orocAbO9Uc0Db8UZ6Bn0E6480/6yYijR524hUdp1zcnbdbEB+yB26TMiOgl8ndh16kH8QAr1hjEMxBNRfM3b1jNRy1sbH74IV5eBwRqVudXhjU/c/SGLvpUnpuOOcYUyfGTBYjD+yBRAXpSXGH63nHJZwb8KuOJVQNrJ0rPBl2LKH0tDXpgCMm9P3EoUMEA+wpKOvA3V/V0eNYp3e6GdOFq9h6uXh1Y8AQUg6ISod/Ex+omIdsZxgQWL/C8TPF4QKBgQDzSsAfsrkTK9FWXEAj2m+i5Ww+ItYXv+MTqEA/OtCX4OwRDP44WxXq3xVppPd7CmCmB2/J1DozFlTu3E8mb6bOy7KC6XEkWL24FV+mrD7TiktGHB8PMcYwDo4rG43avtgxMNZ3RjMxMCeC9RnsW2uO5LBx5grpbGHK9Qvsm804UQKBgQC2PbiLMfBCdz0MC+1zwUwwXu4b39VhMVLYpfaACevHMGCgIm63zLXRHza0Xw9zHWwd3MyMZ2fXxveO6TzjoC6NIcpa7gC6tmEc7P0Yqe2NGAX9gJxmZbhw3BouBj6qFhoCBnUqh+cM8tgQ+0DcqnOii2l5HkYkM28WuwYR4ej1SwKBgQCmD8a1nCshf84icVNCZa2/dXN9sg+KJGrdlwFLZ1zL3jWjqce4NcvvBhg6hOR7cmjnyrmt/JNBHaQZaf0Ikjs8eeM94hNdKMlOZiBkxrsXbxTUJQu6NlI9qSG3INahkZRFdz1cKml00JaXl677Gqd+4G/jPo8CJv1VKA/cj7fzEQKBgQCPdrc4nO+O419jhGBBqAHacmDwAJ1yDeoyMzSCR00dWbA3X+PZPYZEQlZGWC9JZ/gc6hz8ysqsyy1Hi8UrTIZZBCjQvFxGoByDMO1t5Rfk4uyUTBLTaXBxKFRTtwbNzuhaf8rs2F/DickeVS0SifzOEQHPH04IYZUqR8DXgwhpUQKBgHCdJsiovLFS3LxAKLvWQhJx/w7A2h2rz5N8btJu3lvY4RgdEZwi7iE+Q2gnJ3ViL/ljp1A4bRk5oEhS6Y9Y6NygMCSTMnbCR/z/TCOG14LX0kjZf9jwHzGAIv/gfN6GlEzWhHZho9uwZKmNxQ6g2t/MjNfeEsXWHkAwse+T9JJ/
-----END PRIVATE KEY-----

 */

/*

-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArTHMyFO/52qOweYF56EBTjU8OslVE9pmJ2b5A0syjG2lfMPWAOPyAwkRaaNY2XuoDg8kDx3yOEnaITVszxkY5rgDpF8IBX5uCDT0sQxUXIJtrdVNhRWxQ/7m8SptrBD3qR13VCll6FPfhVGkJILeBIS9qzztVwKc11TZ2hwYko47PKPFQrQPCkn7uOf1n0SWHgp3oxZElhv18Sm33JzyULexq4trXx9gn7rJDpRnJm+pq9vXCO+K5Olq4OSbM4lh17SzsuwMLHw0l2zMsvRpmowECMm0XIDTqqGxT9DntoMHzaLSuUtfVq470hkQkzAFkRKXSoh7+6v+riNIRm0EuwIDAQAB
-----END PUBLIC KEY-----

 */