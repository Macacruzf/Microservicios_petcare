package com.petcare.ticket.dto;

import lombok.Data;

@Data
public class TicketRequest {
    private Long idUsuario;
    private Long idProducto;
    private Integer clasificacion;
    private String comentario;
}
