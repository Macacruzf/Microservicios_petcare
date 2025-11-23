package com.petcare.venta.service;

import com.petcare.venta.dto.VentaRequest;
import com.petcare.venta.model.DetalleVenta;
import com.petcare.venta.model.Venta;
import com.petcare.venta.repository.DetalleVentaRepository;
import com.petcare.venta.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @InjectMocks
    private VentaService ventaService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearVenta_deberiaGuardarVentaYDetalles() {
        // ------- REQUEST --------
        VentaRequest request = new VentaRequest();
        request.setCliente("Francisca");
        request.setMetodoPago("Tarjeta");
        request.setTotal(15000.0);

        VentaRequest.DetalleVentaRequest item = new VentaRequest.DetalleVentaRequest();
        item.setProductoId(10L);
        item.setNombre("Shampoo Perros");
        item.setCantidad(2);
        item.setSubtotal(10000.0);

        request.setItems(List.of(item));

        // ------- MOCK BEHAVIOR ------
        Venta ventaMock = new Venta();
        ventaMock.setIdVenta(1L);

        when(ventaRepository.save(any(Venta.class)))
                .thenReturn(ventaMock);

        // ------- EJECUTAR --------
        Venta resultado = ventaService.crearVenta(request);

        // ------- VALIDAR ---------
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdVenta());
        assertEquals("Francisca", request.getCliente());

        verify(ventaRepository, times(1)).save(any(Venta.class));
        verify(detalleVentaRepository, times(1)).save(any(DetalleVenta.class));
    }
}
