package com.petcare.producto.controller;

import com.petcare.producto.dto.CategoriaRequest;
import com.petcare.producto.dto.EstadoRequest;
import com.petcare.producto.dto.ProductoUpdateDto;
import com.petcare.producto.model.Categoria;
import com.petcare.producto.model.Producto;
import com.petcare.producto.service.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoControllerTest {

    @InjectMocks
    private ProductoController productoController;

    @Mock
    private ProductoService productoService;

    private Producto productoEjemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productoEjemplo = new Producto();
        productoEjemplo.setIdProducto(1L);
        productoEjemplo.setNombre("Dog Chow Adulto");
        productoEjemplo.setPrecio(23990.0);
        productoEjemplo.setStock(10);
    }

    // ============================================================
    // ✔ TEST LISTAR PRODUCTOS GENERAL
    // ============================================================
    @Test
    void obtenerProductos_OK() {
        when(productoService.getProductos(null, null))
                .thenReturn(List.of(productoEjemplo));

        ResponseEntity<?> respuesta = productoController.obtenerProductos(null, null);

        assertEquals(200, respuesta.getStatusCodeValue());
        assertTrue(respuesta.getBody() instanceof List);
        verify(productoService).getProductos(null, null);
    }

    @Test
    void obtenerProductos_NoContent() {
        when(productoService.getProductos(null, null)).thenReturn(Collections.emptyList());

        ResponseEntity<?> respuesta = productoController.obtenerProductos(null, null);

        assertEquals(204, respuesta.getStatusCodeValue());
        verify(productoService).getProductos(null, null);
    }

    // ============================================================
    // ✔ TEST OBTENER PRODUCTO POR ID
    // ============================================================
    @Test
    void obtenerProductoConDetalles_OK() {
        when(productoService.getProductoById(1L)).thenReturn(productoEjemplo);

        ResponseEntity<?> respuesta = productoController.obtenerProductoConDetalles(1L);

        assertEquals(200, respuesta.getStatusCodeValue());
        assertEquals(productoEjemplo, respuesta.getBody());
        verify(productoService).getProductoById(1L);
    }

    @Test
    void obtenerProductoConDetalles_NotFound() {
        when(productoService.getProductoById(1L))
                .thenThrow(new ProductoService.ResourceNotFoundException("No encontrado"));

        ResponseEntity<?> respuesta = productoController.obtenerProductoConDetalles(1L);

        assertEquals(404, respuesta.getStatusCodeValue());
        verify(productoService).getProductoById(1L);
    }

    // ============================================================
    // ✔ TEST CREAR PRODUCTO
    // ============================================================
    @Test
    void agregarProducto_Created() {
        when(productoService.agregarProducto(any())).thenReturn(productoEjemplo);

        ResponseEntity<?> respuesta = productoController.agregarProducto(productoEjemplo);

        assertEquals(201, respuesta.getStatusCodeValue());
        verify(productoService).agregarProducto(any());
    }

    // ============================================================
    // ✔ TEST ACTUALIZAR PRODUCTO
    // ============================================================
    @Test
    void actualizarProducto_OK() {
        ProductoUpdateDto dto = new ProductoUpdateDto();
        dto.setNombre("Nuevo nombre");

        when(productoService.actualizarProducto(eq(1L), any()))
                .thenReturn(productoEjemplo);

        ResponseEntity<?> respuesta = productoController.actualizarProducto(1L, dto);

        assertEquals(200, respuesta.getStatusCodeValue());
        verify(productoService).actualizarProducto(eq(1L), any());
    }

    @Test
    void actualizarProducto_NotFound() {
        ProductoUpdateDto dto = new ProductoUpdateDto();

        when(productoService.actualizarProducto(eq(1L), any()))
                .thenThrow(new ProductoService.ResourceNotFoundException("No encontrado"));

        ResponseEntity<?> respuesta = productoController.actualizarProducto(1L, dto);

        assertEquals(404, respuesta.getStatusCodeValue());
        verify(productoService).actualizarProducto(eq(1L), any());
    }

    // ============================================================
    // ✔ TEST CAMBIAR ESTADO
    // ============================================================
    @Test
    void cambiarEstadoProducto_OK() {
        EstadoRequest req = new EstadoRequest();
        req.setEstado("DISPONIBLE");

        ResponseEntity<?> respuesta = productoController.cambiarEstadoProducto(1L, req);

        assertEquals(200, respuesta.getStatusCodeValue());
        verify(productoService).cambiarEstado(1L, "DISPONIBLE");
    }

    @Test
    void cambiarEstadoProducto_NotFound() {
        EstadoRequest req = new EstadoRequest();
        req.setEstado("SIN_STOCK");

        doThrow(new ProductoService.ResourceNotFoundException("No encontrado"))
                .when(productoService).cambiarEstado(1L, "SIN_STOCK");

        ResponseEntity<?> respuesta = productoController.cambiarEstadoProducto(1L, req);

        assertEquals(404, respuesta.getStatusCodeValue());
    }

    // ============================================================
    // ✔ TEST ELIMINAR
    // ============================================================
    @Test
    void eliminarProducto_OK() {
        ResponseEntity<?> respuesta = productoController.eliminarProducto(1L);

        assertEquals(200, respuesta.getStatusCodeValue());
        verify(productoService).eliminarProducto(1L);
    }

    @Test
    void eliminarProducto_NotFound() {
        doThrow(new ProductoService.ResourceNotFoundException("No encontrado"))
                .when(productoService).eliminarProducto(1L);

        ResponseEntity<?> respuesta = productoController.eliminarProducto(1L);

        assertEquals(404, respuesta.getStatusCodeValue());
    }

    // ============================================================
    // ✔ TEST DESCONTAR STOCK
    // ============================================================
    @Test
    void descontarStock_OK() {
        Map<String, Integer> body = Map.of("cantidad", 5);

        when(productoService.getProductoById(1L)).thenReturn(productoEjemplo);

        ResponseEntity<?> respuesta = productoController.descontarStock(1L, body);

        assertEquals(200, respuesta.getStatusCodeValue());
        assertEquals(5, productoEjemplo.getStock());
        verify(productoService).guardar(any());
    }

    @Test
    void descontarStock_NotFound() {
        Map<String, Integer> body = Map.of("cantidad", 5);

        when(productoService.getProductoById(1L))
                .thenThrow(new ProductoService.ResourceNotFoundException("No encontrado"));

        ResponseEntity<?> respuesta = productoController.descontarStock(1L, body);

        assertEquals(404, respuesta.getStatusCodeValue());
    }

    @Test
    void descontarStock_StockInsuficiente() {
        Map<String, Integer> body = Map.of("cantidad", 20);
        productoEjemplo.setStock(5);

        when(productoService.getProductoById(1L)).thenReturn(productoEjemplo);

        ResponseEntity<?> respuesta = productoController.descontarStock(1L, body);

        assertEquals(400, respuesta.getStatusCodeValue());
    }

    // ============================================================
    // ✔ TEST LISTAR CATEGORÍAS
    // ============================================================
    @Test
    void obtenerCategorias_OK() {
        Categoria cat = new Categoria(1L, "Alimentos");
        when(productoService.getCategorias()).thenReturn(List.of(cat));

        ResponseEntity<?> respuesta = productoController.obtenerCategorias();

        assertEquals(200, respuesta.getStatusCodeValue());
        verify(productoService).getCategorias();
    }

    @Test
    void obtenerCategorias_NoContent() {
        when(productoService.getCategorias()).thenReturn(List.of());

        ResponseEntity<?> respuesta = productoController.obtenerCategorias();

        assertEquals(204, respuesta.getStatusCodeValue());
    }

    // ============================================================
    // ✔ TEST CREAR CATEGORÍA
    // ============================================================
    @Test
    void agregarCategoria_Created() {
        CategoriaRequest req = new CategoriaRequest();
        req.setNombre("Juguetes");

        Categoria creada = new Categoria(1L, "Juguetes");

        when(productoService.agregarCategoria(any())).thenReturn(creada);

        ResponseEntity<?> respuesta = productoController.agregarCategoria(req);

        assertEquals(201, respuesta.getStatusCodeValue());
        verify(productoService).agregarCategoria(any());
    }

    // ============================================================
    // ✔ TEST ACTUALIZAR CATEGORÍA
    // ============================================================
    @Test
    void actualizarCategoria_OK() {
        CategoriaRequest req = new CategoriaRequest();
        req.setNombre("Accesorios");

        Categoria categoriaActualizada = new Categoria(1L, "Accesorios");

        when(productoService.actualizarCategoria(eq(1L), any())).thenReturn(categoriaActualizada);

        ResponseEntity<?> respuesta = productoController.actualizarCategoria(1L, req);

        assertEquals(200, respuesta.getStatusCodeValue());
        verify(productoService).actualizarCategoria(eq(1L), any());
    }

    @Test
    void actualizarCategoria_NotFound() {
        CategoriaRequest req = new CategoriaRequest();
        req.setNombre("Accesorios");

        when(productoService.actualizarCategoria(eq(1L), any()))
                .thenThrow(new ProductoService.ResourceNotFoundException("No encontrado"));

        ResponseEntity<?> respuesta = productoController.actualizarCategoria(1L, req);

        assertEquals(404, respuesta.getStatusCodeValue());
    }

    // ============================================================
    // ✔ TEST ELIMINAR CATEGORÍA
    // ============================================================
    @Test
    void eliminarCategoria_OK() {
        ResponseEntity<?> respuesta = productoController.eliminarCategoria(1L);

        assertEquals(200, respuesta.getStatusCodeValue());
        verify(productoService).eliminarCategoria(1L);
    }

    @Test
    void eliminarCategoria_NotFound() {
        doThrow(new ProductoService.ResourceNotFoundException("No encontrada"))
                .when(productoService).eliminarCategoria(1L);

        ResponseEntity<?> respuesta = productoController.eliminarCategoria(1L);

        assertEquals(404, respuesta.getStatusCodeValue());
    }
}
