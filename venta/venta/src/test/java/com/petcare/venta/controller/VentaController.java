package com.petcare.venta.controller;

import com.petcare.venta.dto.VentaRequest;
import com.petcare.venta.model.Venta;
import com.petcare.venta.service.VentaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaControllerTest {

    @Mock
    private VentaService ventaService;

    @InjectMocks
    private VentaController ventaController;

    public VentaControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearVenta_retorna200() {
        VentaRequest req = new VentaRequest();
        req.setCliente("Fran");
        req.setTotal(15000.0);

        Venta mockVenta = new Venta();
        mockVenta.setCliente("Fran");
        mockVenta.setTotal(15000.0);

        when(ventaService.crearVenta(req)).thenReturn(mockVenta);

        ResponseEntity<?> res = ventaController.crearVenta(req);

        assertEquals(200, res.getStatusCode().value());
        Venta body = (Venta) res.getBody();
        assertEquals("Fran", body.getCliente());
    }
    @Test
    void listarVentas_retornaLista() {
        // Simulamos respuesta del service
        when(ventaService.listarTodas()).thenReturn(java.util.List.of());

        // Ejecutamos el endpoint del controller
        ResponseEntity<?> res = ventaController.listarVentas();

        // Validamos respuesta
        assertEquals(200, res.getStatusCode().value());
        assertTrue(((java.util.List<?>) res.getBody()).isEmpty());
    }
}