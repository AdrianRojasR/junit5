package com.arojas.test.springboot.app.controllers;

import com.arojas.test.springboot.app.Datos;
import com.arojas.test.springboot.app.models.Cuenta;
import com.arojas.test.springboot.app.models.TransaccionDto;
import com.arojas.test.springboot.app.services.CuentaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(CuentaController.class)
public class CuentaControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CuentaService cuentaService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testDetalle() throws Exception {
        Mockito.when(cuentaService.findById(1L)).thenReturn(Datos.crearcuenta1().get());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cuentas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testTransferir() throws Exception {
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setCuentaOrigen(1L);
        transaccionDto.setCuentaDestino(2L);
        transaccionDto.setMonto(new BigDecimal(200));
        transaccionDto.setBancoId(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaccionDto)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Date").value(LocalDate.now().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transaccion.cuentaOrigen").value(1L));
    }

    @Test
    void testListar() throws Exception {
        // Given
        List<Cuenta> cuentas = Arrays.asList(Datos.crearcuenta1().get(),
                Datos.crearcuenta2().get()
        );
        Mockito.when(cuentaService.findAll()).thenReturn(cuentas);

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON))
                // Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].persona").value("Adrian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].persona").value("Yearr"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].saldo").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].saldo").value("2000"))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(cuentas)));

        Mockito.verify(cuentaService).findAll();
    }

    @Test
    void testGuardar() throws Exception {
        // Given
        Cuenta cuenta = new Cuenta(null, "Pepe", new BigDecimal("3000"));
        Mockito.when(cuentaService.save(Mockito.any())).then(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuenta)))
                // Then
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.persona", is("Pepe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo", is(3000)));
        Mockito.verify(cuentaService).save(Mockito.any());
    }
}
