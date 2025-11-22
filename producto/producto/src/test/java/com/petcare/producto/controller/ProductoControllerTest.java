package com.petcare.producto.controller;

import com.petcare.producto.model.*;
import com.petcare.producto.service.ProductoService;
import com.petcare.producto.dto.EstadoRequest;  // Import para EstadoRequest

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private Producto producto;
    private Categoria categoria;
    private Principal adminPrincipal;
    private Principal userPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoria = new Categoria(1L, "Alimentos");
        producto = new Producto(10L, "DogChow", 19990.0, 20, EstadoProducto.DISPONIBLE, categoria);

        adminPrincipal = () -> "admin";
        userPrincipal = () -> "cliente";
    }

    // ============================================================
    // ✔ /movil (sin HATEOAS) - MODIFICADO PARA VERIFICAR CONVERSIÓN DE ESTADO
    // ============================================================

    @Test
    void testObtenerProductosMovil_ConResultados() {
        when(productoService.getProductos(null, null))
                .thenReturn(List.of(producto));

        ResponseEntity<?> response = productoController.obtenerProductosMovil(null, null);

        assertEquals(200, response.getStatusCode().value());
        // Verifica que la respuesta contenga productos con estado como String
        List<?> body = (List<?>) response.getBody();
        assertNotNull(body);
        assertFalse(body.isEmpty());
        // Puedes agregar más verificaciones si usas un DTO específico
    }

    @Test
    void testObtenerProductosMovil_SinResultados() {
        when(productoService.getProductos(null, null)).thenReturn(List.of());

        ResponseEntity<?> response = productoController.obtenerProductosMovil(null, null);

        assertEquals(204, response.getStatusCode().value());
    }

    // ============================================================
    // ✔ GET / (HATEOAS)
    // ============================================================

    @Test
    void testObtenerProductosHateoas() {
        when(productoService.getProductos(null, null))
                .thenReturn(List.of(producto));

        ResponseEntity<?> response = productoController.obtenerProductos(null, null);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testObtenerProductosHateoasSinResultados() {
        when(productoService.getProductos(null, null)).thenReturn(List.of());

        ResponseEntity<?> response = productoController.obtenerProductos(null, null);

        assertEquals(204, response.getStatusCode().value());
    }

    // ============================================================
    // ✔ GET /{id}
    // ============================================================

    @Test
    void testObtenerProductoPorId() {
        when(productoService.getProductoConDetalles(10L))
                .thenReturn(producto);

        ResponseEntity<?> response = productoController.obtenerProductoConDetalles(10L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testObtenerProductoPorId_NotFound() {
        when(productoService.getProductoConDetalles(10L))
                .thenThrow(new ProductoService.ResourceNotFoundException("No encontrado"));

        ResponseEntity<?> response = productoController.obtenerProductoConDetalles(10L);

        assertEquals(404, response.getStatusCode().value());
    }

    // ============================================================
    // ✔ POST / (crear)
    // ============================================================

    @Test
    void testAgregarProducto_AdminOK() {
        when(productoService.agregarProducto(any())).thenReturn(producto);

        ResponseEntity<?> response = productoController.agregarProducto(producto, adminPrincipal);

        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void testAgregarProducto_SinPermiso() {
        ResponseEntity<?> response = productoController.agregarProducto(producto, userPrincipal);
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void testAgregarProducto_ErrorDatos() {
        when(productoService.agregarProducto(any()))
                .thenThrow(new IllegalArgumentException("Error"));

        ResponseEntity<?> response = productoController.agregarProducto(producto, adminPrincipal);

        assertEquals(400, response.getStatusCode().value());
    }

    // ============================================================
    // ✔ PUT /{id}
    // ============================================================


    @Test
    void testActualizarProducto_SinPermiso() {
        ResponseEntity<?> response =
                productoController.actualizarProducto(10L, producto, userPrincipal);

        assertEquals(403, response.getStatusCode().value());
    }

    // ============================================================
    // ✔ PUT /{id}/estado (USANDO EstadoRequest)
    // ============================================================

    @Test
    void testCambiarEstadoProducto_AdminOK() {
        EstadoRequest request = mock(EstadoRequest.class);
        when(request.getEstadoAsEnum()).thenReturn(EstadoProducto.NO_DISPONIBLE);

        doNothing().when(productoService).cambiarEstado(eq(10L), eq(EstadoProducto.NO_DISPONIBLE));

        ResponseEntity<?> response =
                productoController.cambiarEstadoProducto(10L, request, adminPrincipal);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testCambiarEstadoProducto_SinPermiso() {
        EstadoRequest request = mock(EstadoRequest.class);

        ResponseEntity<?> response =
                productoController.cambiarEstadoProducto(10L, request, userPrincipal);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void testCambiarEstadoProducto_EstadoInvalido() {
        EstadoRequest request = mock(EstadoRequest.class);
        when(request.getEstadoAsEnum()).thenThrow(new IllegalArgumentException("Estado inválido"));

        ResponseEntity<?> response =
                productoController.cambiarEstadoProducto(10L, request, adminPrincipal);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void testCambiarEstadoProducto_NotFound() {
        EstadoRequest request = mock(EstadoRequest.class);
        when(request.getEstadoAsEnum()).thenReturn(EstadoProducto.NO_DISPONIBLE);
        doThrow(new ProductoService.ResourceNotFoundException("No encontrado"))
                .when(productoService).cambiarEstado(eq(10L), any());

        ResponseEntity<?> response =
                productoController.cambiarEstadoProducto(10L, request, adminPrincipal);

        assertEquals(404, response.getStatusCode().value());
    }

    // ============================================================
    // ✔ DELETE /{id}
    // ============================================================

    @Test
    void testEliminarProducto_AdminOK() {

        doNothing().when(productoService).eliminarProducto(10L);

        ResponseEntity<?> response =
                productoController.eliminarProducto(10L, adminPrincipal);

        assertEquals(200, response.getStatusCode().value());
        verify(productoService).eliminarProducto(10L);
    }

    @Test
    void testEliminarProducto_SinPermiso() {
        ResponseEntity<?> response =
                productoController.eliminarProducto(10L, userPrincipal);

        assertEquals(403, response.getStatusCode().value());
    }

    // ============================================================
    // ✔ CRUD CATEGORÍAS
    // ============================================================

    @Test
    void testObtenerCategorias() {
        when(productoService.getCategorias()).thenReturn(List.of(categoria));

        ResponseEntity<?> response = productoController.obtenerCategorias();

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testObtenerCategorias_Vacio() {
        when(productoService.getCategorias()).thenReturn(List.of());

        ResponseEntity<?> response = productoController.obtenerCategorias();

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testAgregarCategoria_AdminOK() {
        when(productoService.agregarCategoria(any())).thenReturn(categoria);

        ResponseEntity<?> response =
                productoController.agregarCategoria(categoria, adminPrincipal);

        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void testAgregarCategoria_SinPermiso() {
        ResponseEntity<?> response =
                productoController.agregarCategoria(categoria, userPrincipal);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void testActualizarCategoria_AdminOK() {
        when(productoService.actualizarCategoria(eq(1L), any()))
                .thenReturn(categoria);

        ResponseEntity<?> response =
                productoController.actualizarCategoria(1L, categoria, adminPrincipal);

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void testEliminarCategoria_AdminOK() {

        doNothing().when(productoService).eliminarCategoria(1L);

        ResponseEntity<?> response =
                productoController.eliminarCategoria(1L, adminPrincipal);

        assertEquals(200, response.getStatusCode().value());
        verify(productoService).eliminarCategoria(1L);
    }
}
