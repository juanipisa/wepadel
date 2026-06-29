package com.uade.tpo.wepadel.util;

import java.net.URLConnection;
import java.util.Base64;

import com.uade.tpo.wepadel.entity.Imagen;

/**
 * Convierte imágenes guardadas en la base (bytes + nombre de archivo)
 * al formato que entiende el navegador en un {@code <img src="...">}:
 *
 * <pre>data:image/jpeg;base64,/9j/4AAQ...</pre>
 *
 * El tipo {@code image/jpeg}, {@code image/png}, etc. se infiere del nombre
 * del archivo (por ejemplo {@code paleta.jpg} → {@code image/jpeg}).
 */
public final class ImagenDataUrlUtil {

    private static final String DEFAULT_MIME = "image/jpeg";

    private ImagenDataUrlUtil() {
    }

    public static String toDataUrl(Imagen imagen) {
        if (imagen == null) {
            return null;
        }

        byte[] contenido = imagen.getContenido();
        if (contenido == null || contenido.length == 0) {
            return null;
        }

        String mimeType = URLConnection.guessContentTypeFromName(imagen.getNombre());
        if (mimeType == null) {
            mimeType = DEFAULT_MIME;
        }

        String base64 = Base64.getEncoder().encodeToString(contenido);
        return "data:" + mimeType + ";base64," + base64;
    }

}
