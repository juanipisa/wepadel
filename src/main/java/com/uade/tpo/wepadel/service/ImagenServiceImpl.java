package com.uade.tpo.wepadel.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.wepadel.entity.Imagen;
import com.uade.tpo.wepadel.entity.Producto;
import com.uade.tpo.wepadel.entity.dto.AddImagenRequest;
import com.uade.tpo.wepadel.entity.dto.ImagenArchivo;
import com.uade.tpo.wepadel.entity.dto.ImagenResponse;
import com.uade.tpo.wepadel.entity.dto.UpdateImagenRequest;
import com.uade.tpo.wepadel.util.ImagenContentTypeUtil;
import com.uade.tpo.wepadel.exceptions.ImagenNotFoundException;
import com.uade.tpo.wepadel.exceptions.ProductoInvalidoException;
import com.uade.tpo.wepadel.exceptions.ProductoNotFoundException;
import com.uade.tpo.wepadel.repository.ImagenRepository;
import com.uade.tpo.wepadel.repository.ProductoRepository;

@Service
public class ImagenServiceImpl implements ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public ImagenResponse getImagenById(Long id) {
        Imagen imagen = imagenRepository.findById(id).orElseThrow(ImagenNotFoundException::new);
        byte[] bytes = imagen.getContenido();
        if (bytes == null || bytes.length == 0) {
            throw new ImagenNotFoundException();
        }
        String encodedString = Base64.getEncoder().encodeToString(bytes);
        return ImagenResponse.builder()
                .id(imagen.getId())
                .nombre(imagen.getNombre())
                .archivoBase64(encodedString)
                .build();
    }

    @Override
    public ImagenArchivo getImagenArchivo(Long id) {
        Imagen imagen = imagenRepository.findById(id).orElseThrow(ImagenNotFoundException::new);
        byte[] bytes = imagen.getContenido();
        if (bytes == null || bytes.length == 0) {
            throw new ImagenNotFoundException();
        }
        return new ImagenArchivo(bytes, imagen.getNombre(), ImagenContentTypeUtil.fromNombre(imagen.getNombre()));
    }

    @Override
    public Long createImagen(AddImagenRequest request) {
        if (request.getProductoId() == null) {
            throw new ProductoInvalidoException("Falta el productoId");
        }   
        MultipartFile archivo = request.getArchivo();
        if (archivo == null || archivo.isEmpty()) {
            throw new ProductoInvalidoException("Falta el archivo");
        }           

        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(ProductoNotFoundException::new);

        // Política: un producto tiene una sola imagen
        imagenRepository.deleteByProductoId(producto.getId());

        byte[] bytes;
        try {
            bytes = archivo.getBytes();
        } catch (IOException e) {
            throw new ProductoInvalidoException("El archivo no es legible");
        }

        String nombre = nombreDesdeArchivo(archivo);
        Imagen guardada = imagenRepository.save(new Imagen(producto, bytes, nombre));
        return guardada.getId();
    }

    @Override
    public void updateImagen(Long imagenId, UpdateImagenRequest request) {
        Imagen imagen = imagenRepository.findById(imagenId)
                .orElseThrow(ImagenNotFoundException::new);

        MultipartFile archivo = request.getArchivo();
        if (archivo == null || archivo.isEmpty()) {
            throw new ProductoInvalidoException("Falta el archivo");
        }

        byte[] bytes;
        try {
            bytes = archivo.getBytes();
        } catch (IOException e) {
            throw new ProductoInvalidoException("El archivo no es legible");
        }

        imagen.setContenido(bytes);
        imagen.setNombre(nombreDesdeArchivo(archivo));
        imagenRepository.save(imagen);

        Long productoId = imagen.getProducto().getId();
        imagenRepository.deleteByProductoIdAndIdNot(productoId, imagenId);
    }

    @Override
    public List<ImagenResponse> getImagenesByProductoId(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new ProductoNotFoundException();
        }
        return imagenRepository.findByProductoIdOrderByIdAsc(productoId).stream()
                .map(i -> {
                    byte[] contenido = i.getContenido();
                    String encodedString = (contenido == null || contenido.length == 0)
                            ? ""
                            : Base64.getEncoder().encodeToString(contenido);
                    return ImagenResponse.builder()
                            .id(i.getId())
                            .nombre(i.getNombre())
                            .archivoBase64(encodedString)
                            .build();
                })
                .toList();
    }

    private static String nombreDesdeArchivo(MultipartFile archivo) {
        String original = archivo.getOriginalFilename();
        if (original != null && !original.isBlank()) {
            return original.trim();
        }
        return "sin_nombre";
    }

}
