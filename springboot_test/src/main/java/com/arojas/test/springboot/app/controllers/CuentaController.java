package com.arojas.test.springboot.app.controllers;

import com.arojas.test.springboot.app.models.Cuenta;
import com.arojas.test.springboot.app.models.TransaccionDto;
import com.arojas.test.springboot.app.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    CuentaService cuentaService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cuenta detalle(@PathVariable Long id){
        return cuentaService.findById(id);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransaccionDto request){
        cuentaService.transferir(request.getCuentaOrigen(), request.getCuentaDestino(),request.getMonto(),request.getBancoId());
        Map<String, Object> response = new HashMap<>();
        response.put("Date", LocalDate.now().toString());
        response.put("status","OK");
        response.put("mensaje","Transferencia realizada");
        response.put("transaccion", request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> listasCuentas(){
        List<Cuenta> cuentas = cuentaService.findAll();
        return ResponseEntity.ok(cuentas);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cuenta guardar(@RequestBody Cuenta cuenta) {
        return cuentaService.save(cuenta);
    }

}

