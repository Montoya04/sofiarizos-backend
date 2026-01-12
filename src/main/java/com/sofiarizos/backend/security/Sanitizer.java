package com.sofiarizos.backend.security;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

@Component
public class Sanitizer {
    
    public String clean(String input) {
        if (input == null) return null;

        //Eliminar scripts
        String cleaned = input.replaceAll("(?i)<script.*?>.*?</script>", "");

        // Escape HTML
        cleaned = StringEscapeUtils.escapeHtml4(cleaned);

        // Trim espacios peligrosos
        return cleaned.trim();
    }
}
