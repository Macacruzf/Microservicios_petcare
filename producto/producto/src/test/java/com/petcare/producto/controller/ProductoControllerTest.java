package com.petcare.producto.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcare.producto.dto.CategoriaRequest;
import com.petcare.producto.dto.EstadoRequest;
import com.petcare.producto.dto.ProductoUpdateDto;
import com.petcare.producto.model.Categoria;
import com.petcare.producto.model.EstadoProducto;
import com.petcare.producto.model.Producto;
import com.petcare.producto.service.ProductoService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    // ============================================================
    // TEST: OBTENER PRODUCTOS MÓVIL
    // ============================================================
    @Test
    @DisplayName("GET /movil - Retorna lista de DTOs cuando hay productos")
    void obtenerProductosMovil_Exito() throws Exception {
        // Arrange
        Categoria cat = new Categoria();
        cat.setIdCategoria(1L);
        cat.setNombre("Alimentos");

        Producto p1 = new Producto();
        p1.setIdProducto(1L);
        p1.setNombre("Dog Chow");
        p1.setPrecio(50.0);
        p1.setStock(10);
        p1.setEstado(EstadoProducto.DISPONIBLE);
        p1.setCategoria(cat);

        when(productoService.getProductos(null, null)).thenReturn(Arrays.asList(p1));

        // Act & Assert
        mockMvc.perform(get("/api/v1/productos/movil")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Dog Chow"))
                .andExpect(jsonPath("$[0].categoria.nombre").value("Alimentos"));
    }

    @Test
    @DisplayName("GET /movil - Retorna 204 No Content si la lista está vacía")
    void obtenerProductosMovil_Vacio() throws Exception {
        when(productoService.getProductos(null, null)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/productos/movil"))
                .andExpect(status().isNoContent());
    }

    // ============================================================
    // TEST: OBTENER PRODUCTO POR ID
    // ============================================================
    @Test
    @DisplayName("GET /{id} - Retorna producto si existe")
    void obtenerProductoPorId_Exito() throws Exception {
        Producto p = new Producto();
        p.setIdProducto(1L);
        p.setNombre("Hueso de Goma");

        when(productoService.getProductoById(1L)).thenReturn(p);

        mockMvc.perform(get("/api/v1/productos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Hueso de Goma"));
    }

    @Test
    @DisplayName("GET /{id} - Retorna 404 si no existe")
    void obtenerProductoPorId_NoEncontrado() throws Exception {
        // Simulamos la excepción personalizada. Asegúrate de que esta clase exista y sea accesible.
        when(productoService.getProductoById(99L))
                .thenThrow(new ProductoService.ResourceNotFoundException("Producto no encontrado"));

        mockMvc.perform(get("/api/v1/productos/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto no encontrado"));
    }

    // ============================================================
    // TEST: CREAR PRODUCTO
    // ============================================================
    @Test
    @DisplayName("POST / - Crea producto y retorna 201 Created")
    void agregarProducto_Exito() throws Exception {
        Producto input = new Producto();
        input.setNombre("Juguete Nuevo");
        
        Producto output = new Producto();
        output.setIdProducto(5L);
        output.setNombre("Juguete Nuevo");
        output.setEstado(EstadoProducto.DISPONIBLE);

        when(productoService.agregarProducto(any(Producto.class))).thenReturn(output);

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProducto").value(5L))
                .andExpect(jsonPath("$.estado").value("DISPONIBLE"));
    }

    // ============================================================
    // TEST: ACTUALIZAR PRODUCTO
    // ============================================================
    @Test
    @DisplayName("PUT /{id} - Actualiza producto correctamente")
    void actualizarProducto_Exito() throws Exception {
        ProductoUpdateDto dto = new ProductoUpdateDto();
        dto.setNombre("Nombre Actualizado");

        Producto actualizado = new Producto();
        actualizado.setIdProducto(1L);
        actualizado.setNombre("Nombre Actualizado");

        when(productoService.actualizarProducto(eq(1L), any(ProductoUpdateDto.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/productos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nombre Actualizado"));
    }

    // ============================================================
    // TEST: CAMBIAR ESTADO
    // ============================================================
    @Test
    @DisplayName("PUT /{id}/estado - Cambia estado correctamente")
    void cambiarEstadoProducto_Exito() throws Exception {
        EstadoRequest request = new EstadoRequest();
        // Asumiendo que tu EstadoRequest tiene un setter o campo compatible con JSON
        // Para este ejemplo, simulamos el objeto request. Ajusta según tu DTO real.
        // Si EstadoRequest usa un String o Enum directamente:
        // request.setEstado("AGOTADO"); 
        
        // Mockeamos que el request devuelve el enum correcto
        // Nota: En un test real, Jackson deserializará el JSON al objeto EstadoRequest
        
        doNothing().when(productoService).cambiarEstado(eq(1L), any(EstadoProducto.class));

        // Construimos un JSON manual simple si el DTO es complejo
        String jsonRequest = "{\"estado\": \"AGOTADO\"}"; 

        mockMvc.perform(put("/api/v1/productos/{id}/estado", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    // ============================================================
    // TEST: ELIMINAR PRODUCTO
    // ============================================================
    @Test
    @DisplayName("DELETE /{id} - Elimina producto y retorna 200")
    void eliminarProducto_Exito() throws Exception {
        doNothing().when(productoService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/v1/productos/{id}", 1L))
                .andExpect(status().isOk());
    }

    // ============================================================
    // TEST: CATEGORÍAS
    // ============================================================
    @Test
    @DisplayName("GET /categorias - Retorna lista de categorías")
    void obtenerCategorias_Exito() throws Exception {
        Categoria c1 = new Categoria();
        c1.setNombre("Juguetes");
        
        when(productoService.getCategorias()).thenReturn(List.of(c1));

        mockMvc.perform(get("/api/v1/productos/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juguetes"));
    }
    
    @Test
    @DisplayName("POST /categorias - Crea nueva categoría")
    void agregarCategoria_Exito() throws Exception {
        CategoriaRequest request = new CategoriaRequest();
        request.setNombre("Nueva Cat");
        
        Categoria created = new Categoria();
        created.setIdCategoria(10L);
        created.setNombre("Nueva Cat");
        
        when(productoService.agregarCategoria(any(Categoria.class))).thenReturn(created);

        mockMvc.perform(post("/api/v1/productos/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCategoria").value(10L));
    }
}