package com.arojas.test.springboot.app;

import com.arojas.test.springboot.app.exceptions.DineroInsuficienteExceptions;
import com.arojas.test.springboot.app.models.Banco;
import com.arojas.test.springboot.app.models.Cuenta;
import com.arojas.test.springboot.app.repositories.BancoRepository;
import com.arojas.test.springboot.app.repositories.CuentaRespository;
import com.arojas.test.springboot.app.services.CuentaService;
import com.arojas.test.springboot.app.services.CuentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SpringbootTestApplicationTests {

	@MockBean
	CuentaRespository cuentaRespository;
	@MockBean
	BancoRepository bancoRepository;

	@Autowired
	CuentaService cuentaService;


//	@BeforeEach
	 void setUp() {
		cuentaRespository = mock(CuentaRespository.class);
		bancoRepository = mock(BancoRepository.class);
		 cuentaService = new CuentaServiceImpl(cuentaRespository,bancoRepository);
	}

	@Test
	void contextLoads() {

		when(cuentaRespository.findById(1L)).thenReturn(Datos.crearcuenta1());
		when(cuentaRespository.findById(2L)).thenReturn(Datos.crearcuenta2());
		when(bancoRepository.findById(1L)).thenReturn(Datos.crearBanco());

		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);

		assertEquals("1000",saldoOrigen.toPlainString());
		assertEquals("2000",saldoDestino.toPlainString());

		cuentaService.transferir(1L,2L,new BigDecimal("100"),1L);

		saldoOrigen = cuentaService.revisarSaldo(1L);
		saldoDestino = cuentaService.revisarSaldo(2L);

		verify(cuentaRespository, times(3)).findById(1L);
		verify(cuentaRespository, times(3)).findById(2L);

		assertEquals("900",saldoOrigen.toPlainString());
		assertEquals("2100",saldoDestino.toPlainString());
 		assertEquals(1, cuentaService.revisarTotalTransferencias(1L));
	}

	@Test
	void contextLoadsException() {

		when(cuentaRespository.findById(1L)).thenReturn(Datos.crearcuenta1());
		when(cuentaRespository.findById(2L)).thenReturn(Datos.crearcuenta2());
		when(bancoRepository.findById(1L)).thenReturn(Datos.crearBanco());

		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);

		assertEquals("1000",saldoOrigen.toPlainString());
		assertEquals("2000",saldoDestino.toPlainString());

		assertThrows(DineroInsuficienteExceptions.class, () -> {cuentaService.transferir(1L,2L,new BigDecimal("1200"),1L);});

		verify(cuentaRespository, times(2)).findById(1L);
		verify(cuentaRespository, times(1)).findById(2L);
		verify(cuentaRespository, never()).save(any(Cuenta.class));
		verify(bancoRepository,never()).findById(1L);
		verify(bancoRepository,never()).save(any(Banco.class));

	}

}
