package com.petcare.venta.controller;

import com.petcare.venta.dto.VentaRequest;
import com.petcare.venta.model.Venta;
import com.petcare.venta.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venta")
@CrossOrigin("*")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @Operation(
            summary = "Crear una venta",
            description = "Recibe los datos del cliente y los productos para registrar una nueva venta."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Venta creada correctamente",
                    content = @Content(schema = @Schema(implementation = Venta.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content
            )
    })
    @PostMapping("/crear")
    public ResponseEntity<Venta> crearVenta(@RequestBody VentaRequest request) {
        Venta venta = ventaService.crearVenta(request);
        return ResponseEntity.ok(venta);
    }

    @Operation(
            summary = "Listar todas las ventas",
            description = "Devuelve el listado completo de ventas registradas en el sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Listado de ventas",
            content = @Content(schema = @Schema(implementation = Venta.class))
    )
    @GetMapping("/listar")
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    @Operation(
            summary = "Obtener una venta por ID",
            description = "Devuelve los datos de una venta específica, incluyendo sus detalles."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Venta encontrada",
                    content = @Content(schema = @Schema(implementation = Venta.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venta no encontrada",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }
}
