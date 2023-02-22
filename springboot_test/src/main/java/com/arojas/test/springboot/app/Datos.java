package com.arojas.test.springboot.app;

import com.arojas.test.springboot.app.models.Banco;
import com.arojas.test.springboot.app.models.Cuenta;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {

    //public static final Cuenta CUENTA_1 = new Cuenta(1L, "Adrian", new BigDecimal("1000"));
    //public static final Cuenta CUENTA_2 = new Cuenta(2L, "Yearr", new BigDecimal("2000"));
   // public static final Banco BANCO = new Banco(1L, "Banco nacional", 0);
    public static Optional<Cuenta> crearcuenta1(){
        return Optional.of(new Cuenta(1L, "Adrian", new BigDecimal("1000")));
    }
    public static Optional<Cuenta> crearcuenta2(){
        return Optional.of(new Cuenta(2L, "Yearr", new BigDecimal("2000")));
    }
    public static Optional<Banco> crearBanco(){
        return Optional.of(new Banco(1L, "Banco nacional", 0));
    }
}
