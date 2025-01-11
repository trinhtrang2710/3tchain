package src;

import java.security.MessageDigest;
import java.security.Key;
import java.util.Base64;

public class StringUtils {
    // Applies SHA-256 to a string and returns the result
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder(); // Using StringBuilder for better performance
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error applying SHA-256", e);
        }
    }

    // Get difficulty string target (e.g., difficulty of 5 will return "00000")
    public static String getDifficultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    // Convert byte array to hexadecimal string
    public static String getHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    // Encode byte array to Base64 string
    public static String encode64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    // Decode Base64 string to byte array
    public static byte[] decode64(String input) {
        return Base64.getDecoder().decode(input);
    }

    // Get key as encoded string (useful for storing/transmitting public keys)
    public static String getStringFromKey(Key key) {
        return encode64(key.getEncoded());
    }

    // Verify if a hex string has valid format
    public static boolean isValidHexString(String hexString) {
        return hexString.matches("^[0-9a-fA-F]+$");
    }

    // Convert hex string to byte array
    public static byte[] hexStringToByteArray(String hexString) {
        if (!isValidHexString(hexString)) {
            throw new IllegalArgumentException("Invalid hex string");
        }

        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}