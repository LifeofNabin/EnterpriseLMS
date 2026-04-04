package com.enterpriselms.backend.common.utils;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    
    public static boolean isValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        
        return hasUpper && hasLower && hasDigit;
    }
    
    public static String getRequirements() {
        return "Password must be at least 8 characters and contain uppercase, lowercase, and digit";
    }
}