package com.progressoft.corpy.tools;

import org.jasypt.util.text.AES256TextEncryptor;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        validateArgs(args);
        switch (args[0]){
            case "e":
                encrypt(args[1], args[2]);
                break;
            case "d":
                decrypt(args[1], args[2]);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private static void validateArgs(String[] args) {
        if(args.length != 3){
            printErrorMessage("Invalid command, expected 3 arguments!");
            System.exit(0);
        }
        if(!Set.of("e", "d").contains(args[0])){
            printErrorMessage("unrecognized command!");
            System.exit(0);
        }
    }

    private static void encrypt(String plainText, String password) {
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(password);
        String encryptText = textEncryptor.encrypt(plainText);
        System.out.printf("ENC(%s)%n", encryptText);
    }

    private static void decrypt(String encryptText, String password) {
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(password);
        String plainText = textEncryptor.decrypt(normalize(encryptText));
        System.out.println(plainText);
    }

    private static String normalize(String encryptText) {
        Pattern pattern = Pattern.compile("ENC\\((.*)\\)");
        Matcher matcher = pattern.matcher(encryptText);
        if(matcher.matches()){
            return matcher.group(1);
        }
        return encryptText;
    }

    private static void printErrorMessage(String errorMessage) {
        System.err.println(errorMessage);
        System.err.println("Command should be");
        System.err.println("\tjava -jar jasypt-encryption.jar <option> <text> <password>");
        System.err.println("\tpossible options are: 'e' to encrypt or 'd' for decrypt.");
    }
}