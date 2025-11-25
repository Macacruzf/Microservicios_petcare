package com.petcare.producto.service;

import com.petcare.producto.model.Categoria;
import com.petcare.producto.model.EstadoProducto;
import com.petcare.producto.model.Producto;
import com.petcare.producto.repository.CategoriaRepository;
import com.petcare.producto.repository.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProductoService productoService;

    private Categoria categoria;
    private Producto producto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoria = new Categoria();
        categoria.setIdCategoria(1L);
        categoria.setNombre("Alimentos");

        producto = new Producto();
        producto.setIdProducto(10L);
        producto.setNombre("DogChow");
        producto.setPrecio(19990.0);
        producto.setStock(20);
        producto.setEstado(EstadoProducto.DISPONIBLE);
        producto.setCategoria(categoria);
    }

    // ============================================================
    // ✔ Tests obtener productos
    // ============================================================
    @Test
    void testGetProductos_NoFiltros() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> result = productoService.getProductos(null, null);

        assertEquals(1, result.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testGetProductos_FiltroNombre() {
        when(productoRepository.findByNombreContainingIgnoreCase("dog"))
                .thenReturn(List.of(producto));

        List<Producto> result = productoService.getProductos("dog", null);

        assertEquals(1, result.size());
    }

    @Test
    void testGetProductos_FiltroCategoria() {
        when(productoRepository.findByCategoria_IdCategoria(1L))
                .thenReturn(List.of(producto));

        List<Producto> result = productoService.getProductos(null, 1L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetProductos_AmbosFiltros() {
        when(productoRepository.findByNombreContainingIgnoreCaseAndCategoria_IdCategoria("dog", 1L))
                .thenReturn(List.of(producto));

        List<Producto> result = productoService.getProductos("dog", 1L);

        assertEquals(1, result.size());
    }

    // ============================================================
    // ✔ Test obtener por estado
    // ============================================================
    @Test
    void testGetProductosPorEstado() {
        when(productoRepository.findByEstado(EstadoProducto.DISPONIBLE))
                .thenReturn(List.of(producto));

        List<Producto> result = productoService.getProductosPorEstado(EstadoProducto.DISPONIBLE);

        assertEquals(1, result.size());
    }

    // ============================================================
    // ✔ Test obtener por ID
    // ============================================================
    @Test
    void testGetProductoConDetalles() {
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

        Producto result = productoService.getProductoConDetalles(10L);

        assertNotNull(result);
        assertEquals("DogChow", result.getNombre());
    }

    @Test
    void testGetProductoConDetalles_NotFound() {
        when(productoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ProductoService.ResourceNotFoundException.class,
                () -> productoService.getProductoConDetalles(10L));
    }

    // ============================================================
    // ✔ Test crear producto
    // ============================================================
    @Test
    void testAgregarProducto() {

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto nuevo = productoService.agregarProducto(producto);

        assertNotNull(nuevo);
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testAgregarProducto_CategoriaInvalida() {
        producto.setCategoria(null);

        assertThrows(IllegalArgumentException.class,
                () -> productoService.agregarProducto(producto));
    }



    // ============================================================
    // ✔ Test actualizar stock
    // ============================================================
    @Test
    void testActualizarStock() {
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

        productoService.actualizarStock(10L, 5);

        assertEquals(5, producto.getStock());
    }

    // ============================================================
    // ✔ Test eliminar producto
    // ============================================================
    @Test
    void testEliminarProducto() {
        when(productoRepository.existsById(10L)).thenReturn(true);

        productoService.eliminarProducto(10L);

        verify(productoRepository).deleteById(10L);
    }

    @Test
    void testEliminarProducto_NotFound() {
        when(productoRepository.existsById(10L)).thenReturn(false);

        assertThrows(ProductoService.ResourceNotFoundException.class,
                () -> productoService.eliminarProducto(10L));
    }
}
