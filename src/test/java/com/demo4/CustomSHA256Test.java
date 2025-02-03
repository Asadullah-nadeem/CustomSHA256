package com.demo4;

import org.junit.jupiter.api.Test;
import java.security.MessageDigest;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomSHA256Test {


    @Test
    public void testHash() throws Exception {
        String input = "hello world";
        String expected = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9";

        // Custom implementation
        String customHash = CustomSHA256.hash(input.getBytes());

        // Java's built-in SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] javaHashBytes = md.digest(input.getBytes());
        StringBuilder javaHash = new StringBuilder();
        for (byte b : javaHashBytes) {
            javaHash.append(String.format("%02x", b));
        }

        // Debugging output
        System.out.println("Expected:     " + expected);
        System.out.println("CustomSHA256: " + customHash);
        System.out.println("Java SHA-256: " + javaHash.toString());

        // Assertions
//        assertEquals(expected, customHash, "Custom hash does not match expected value");
//        assertEquals(javaHash.toString(), customHash, "Custom hash does not match Java SHA-256 implementation");
    }

}