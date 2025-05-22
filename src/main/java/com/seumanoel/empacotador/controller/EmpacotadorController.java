package com.seumanoel.empacotador.controller;

import com.seumanoel.empacotador.dto.EmpacotamentoResponse;
import com.seumanoel.empacotador.model.Pedido;
import com.seumanoel.empacotador.service.EmpacotadorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empacotar")
public class EmpacotadorController {

    private final EmpacotadorService service;

    public EmpacotadorController(EmpacotadorService service) {
        this.service = service;
    }

    @PostMapping
    public List<EmpacotamentoResponse> empacotar(@RequestBody List<Pedido> pedidos) {
        return service.empacotar(pedidos);
    }
}
