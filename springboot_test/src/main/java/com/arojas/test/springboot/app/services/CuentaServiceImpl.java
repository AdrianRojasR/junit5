package com.arojas.test.springboot.app.services;

import com.arojas.test.springboot.app.models.Banco;
import com.arojas.test.springboot.app.models.Cuenta;
import com.arojas.test.springboot.app.repositories.BancoRepository;
import com.arojas.test.springboot.app.repositories.CuentaRespository;
import com.arojas.test.springboot.app.services.CuentaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

    private CuentaRespository cuentaRespository;

    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRespository cuentaRespository, BancoRepository bancoRepository) {
        this.cuentaRespository = cuentaRespository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        return cuentaRespository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransferencias(Long bancoId) {
        return bancoRepository.findById(bancoId).orElseThrow().getTotalTransfrencia();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        return cuentaRespository.findById(cuentaId).orElseThrow().getSaldo();
    }

    @Override
    @Transactional
    public void transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal monto, long bancoId) {


        Cuenta cuentaOrigen = cuentaRespository.findById(cuentaOrigenId).orElseThrow();
        cuentaOrigen.debito(monto);
        cuentaRespository.save(cuentaOrigen);

        Cuenta cuentaDestino = cuentaRespository.findById(cuentaDestinoId).orElseThrow();
        cuentaDestino.credito(monto);
        cuentaRespository.save(cuentaDestino);

        Banco banco = bancoRepository.findById(bancoId).orElseThrow();
        int totalTransferencia = banco.getTotalTransfrencia();
        banco.setTotalTransfrencia(++totalTransferencia);
        bancoRepository.save(banco);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        return cuentaRespository.findAll();
    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
        return cuentaRespository.save(cuenta);
    }
}
