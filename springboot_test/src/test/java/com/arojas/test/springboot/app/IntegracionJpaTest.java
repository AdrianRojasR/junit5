package com.arojas.test.springboot.app;

import com.arojas.test.springboot.app.models.Cuenta;
import com.arojas.test.springboot.app.repositories.CuentaRespository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@DataJpaTest
public class IntegracionJpaTest {

    @Autowired
    CuentaRespository cuentaRespository;

    @Test
    void testFindId() {
        Optional<Cuenta> cuenta = cuentaRespository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Andres",cuenta.get().getPersona());
    }

    @Test
    void testFindPersona() {
        Optional<Cuenta> cuenta = cuentaRespository.findByPersona("Andres");
        assertTrue(cuenta.isPresent());
        assertEquals("Andres",cuenta.get().getPersona());
        assertEquals("1000.00",cuenta.get().getSaldo().toPlainString());
    }

    @Test
    void testFindPersonaTrowException() {
        Optional<Cuenta> cuenta = cuentaRespository.findByPersona("yearr");

        assertThrows(NoSuchElementException.class,cuenta::orElseThrow);
        assertFalse(cuenta.isPresent());
    }

    @Test
    void findAll() {
        List<Cuenta> listaCuentas = cuentaRespository.findAll();
        assertEquals(2,listaCuentas.size());
    }

    @Test
    void saveCuenta() {
        cuentaRespository.save(new Cuenta(null,"yearr",new BigDecimal(1500)));
        Cuenta cuenta = cuentaRespository.findByPersona("yearr").orElseThrow();

        assertEquals("yearr",cuenta.getPersona());
        assertEquals(3L,cuenta.getId());
    }

    @Test
    void delete() {
        Cuenta cuenta = cuentaRespository.findById(2L).orElseThrow();
        cuentaRespository.delete(cuenta);

        assertThrows(NoSuchElementException.class,()-> cuentaRespository.findById(2L).orElseThrow());

        assertEquals(1,cuentaRespository.findAll().size())
        ;


    }
}
