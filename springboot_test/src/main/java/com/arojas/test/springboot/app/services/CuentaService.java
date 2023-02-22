package com.arojas.test.springboot.app.services;
import com.arojas.test.springboot.app.models.Cuenta;
import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {

    Cuenta findById(Long id);

    int revisarTotalTransferencias(Long BancoId);

    BigDecimal revisarSaldo(Long cuentaId);
    void transferir(Long cuentaOrigen,Long cuentaDestino, BigDecimal monto, long bancoId);

    List<Cuenta> findAll();

   Cuenta save(Cuenta cuenta);
}
