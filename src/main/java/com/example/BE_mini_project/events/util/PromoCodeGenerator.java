package com.example.BE_mini_project.events.util;

import java.security.SecureRandom;

public class PromoCodeGenerator {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String ALPHA_NUMERIC = UPPER + LOWER + DIGITS;
    private static final SecureRandom random = new SecureRandom();

    public static String generatePromoCode() {
        StringBuilder builder = new StringBuilder(8);

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(ALPHA_NUMERIC.length());
            builder.append(ALPHA_NUMERIC.charAt(index));
        }

        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(DIGITS.length());
            builder.append(DIGITS.charAt(index));
        }

        return builder.toString();
    }
}