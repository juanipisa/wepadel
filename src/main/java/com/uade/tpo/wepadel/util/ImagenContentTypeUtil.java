package com.uade.tpo.wepadel.util;

public final class ImagenContentTypeUtil {

    private ImagenContentTypeUtil() {
    }

    public static String fromNombre(String nombre) {
        if (nombre == null) {
            return "application/octet-stream";
        }
        String lower = nombre.toLowerCase();
        if (lower.endsWith(".png")) {
            return "image/png";
        }
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (lower.endsWith(".webp")) {
            return "image/webp";
        }
        if (lower.endsWith(".gif")) {
            return "image/gif";
        }
        return "application/octet-stream";
    }

}
