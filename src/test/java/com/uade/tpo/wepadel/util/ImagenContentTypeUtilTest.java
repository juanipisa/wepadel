package com.uade.tpo.wepadel.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ImagenContentTypeUtilTest {

    @Test
    void fromNombre_detectaExtensionesComunes() {
        assertThat(ImagenContentTypeUtil.fromNombre("foto.png")).isEqualTo("image/png");
        assertThat(ImagenContentTypeUtil.fromNombre("foto.jpg")).isEqualTo("image/jpeg");
        assertThat(ImagenContentTypeUtil.fromNombre("foto.JPEG")).isEqualTo("image/jpeg");
        assertThat(ImagenContentTypeUtil.fromNombre("foto.webp")).isEqualTo("image/webp");
        assertThat(ImagenContentTypeUtil.fromNombre("foto.gif")).isEqualTo("image/gif");
        assertThat(ImagenContentTypeUtil.fromNombre("sin_extension")).isEqualTo("application/octet-stream");
    }

}
