package com.petcare.producto.controller;

import com.petcare.producto.dto.CategoriaRequest;
import com.petcare.producto.dto.EstadoRequest;
import com.petcare.producto.dto.ProductoUpdateDto;
import com.petcare.producto.model.Categoria;
import com.petcare.producto.model.EstadoProducto;
import com.petcare.producto.model.Producto;
import com.petcare.producto.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ====================================
    // ✔ TEST: Obtener productos móvil
    // ====================================
    @Test
    void testObtenerProductosMovil() {
        Producto p = new Producto();
        p.setIdProducto(1L);
        p.setNombre("Shampoo");
        p.setPrecio(5990.0);
        p.setStock(10);
        p.setEstado(EstadoProducto.DISPONIBLE);

        Categoria c = new Categoria();
        c.setIdCategoria(1L);
        c.setNombre("Baño");
        p.setCategoria(c);

        when(productoService.getProductos(null, null)).thenReturn(List.of(p));

        ResponseEntity<?> response = controller.obtenerProductosMovil(null, null);

        assertEquals(200, response.getStatusCode().value());
    }

    // ====================================
    // ✔ TEST: Crear categoría
    // ====================================
    @Test
    void testAgregarCategoria() {
        CategoriaRequest req = new CategoriaRequest();
        req.setNombre("Accesorios");

        Categoria c = new Categoria();
        c.setIdCategoria(1L);
        c.setNombre("Accesorios");

        when(productoService.agregarCategoria(any())).thenReturn(c);

        ResponseEntity<?> response = controller.agregarCategoria(req);

        assertEquals(201, response.getStatusCode().value());
    }

    // ====================================
    // ✔ TEST: Cambiar estado
    // ====================================
    @Test
    void testCambiarEstado() {
        EstadoRequest request = new EstadoRequest();
        request.setEstado("AGOTADO");

        doNothing().when(productoService).cambiarEstado(1L, EstadoProducto.NO_DISPONIBLE);

        ResponseEntity<?> response = controller.cambiarEstadoProducto(1L, request);

        assertEquals(200, response.getStatusCode().value());
    }

    // ====================================
    // ✔ TEST: Actualizar producto
    // ====================================
    @Test
    void testActualizarProducto() {
        ProductoUpdateDto dto = new ProductoUpdateDto();
        dto.setNombre("Nuevo nombre");

        Producto p = new Producto();
        p.setIdProducto(1L);

        when(productoService.actualizarProducto(eq(1L), any())).thenReturn(p);

        ResponseEntity<?> response = controller.actualizarProducto(1L, dto);

        assertEquals(200, response.getStatusCode().value());
    }
}
