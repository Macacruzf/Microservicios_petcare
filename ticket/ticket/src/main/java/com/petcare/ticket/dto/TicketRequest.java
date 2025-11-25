
package com.petcare.ticket.dto;

import lombok.Data;

@Data
public class TicketRequest {

    private Long idUsuario;         // ID del cliente que hace la reseña
    private Long idProducto;        // Producto evaluado
    private Integer clasificacion;  // Estrellas (1 a 5)
    private String comentario;      // Texto del cliente
}
