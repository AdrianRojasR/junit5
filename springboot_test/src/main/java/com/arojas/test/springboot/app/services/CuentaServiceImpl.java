package com.arojas.test.springboot.app.services;

import com.arojas.test.springboot.app.models.Banco;
import com.arojas.test.springboot.app.models.Cuenta;
import com.arojas.test.springboot.app.repositories.BancoRepository;
import com.arojas.test.springboot.app.repositories.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

    private CuentaRepository cuentaRepository;

    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).get();
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransferencias(Long bancoId) {
        return bancoRepository.findById(bancoId).get().getTotalTransfrencia();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        return cuentaRepository.findById(cuentaId).get().getSaldo();
    }

    @Override
    @Transactional
    public void transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal monto, long bancoId) {


        Cuenta cuentaOrigen = cuentaRepository.findById(cuentaOrigenId).get();
        cuentaOrigen.debito(monto);
        cuentaRepository.save(cuentaOrigen);

        Cuenta cuentaDestino = cuentaRepository.findById(cuentaDestinoId).get();
        cuentaDestino.credito(monto);
        cuentaRepository.save(cuentaDestino);

        Banco banco = bancoRepository.findById(bancoId).get();
        int totalTransferencia = banco.getTotalTransfrencia();
        banco.setTotalTransfrencia(++totalTransferencia);
        bancoRepository.save(banco);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }
}
