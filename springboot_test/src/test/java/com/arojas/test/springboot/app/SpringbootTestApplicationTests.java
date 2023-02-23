package com.arojas.test.springboot.app;

import com.arojas.test.springboot.app.exceptions.DineroInsuficienteExceptions;
import com.arojas.test.springboot.app.models.Banco;
import com.arojas.test.springboot.app.models.Cuenta;
import com.arojas.test.springboot.app.repositories.BancoRepository;
import com.arojas.test.springboot.app.repositories.CuentaRepository;
import com.arojas.test.springboot.app.services.CuentaService;
import com.arojas.test.springboot.app.services.CuentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SpringbootTestApplicationTests {

	@MockBean
	CuentaRepository cuentaRepository;
	@MockBean
	BancoRepository bancoRepository;

	@Autowired
	CuentaService cuentaService;


//	@BeforeEach
	 void setUp() {
		cuentaRepository = mock(CuentaRepository.class);
		bancoRepository = mock(BancoRepository.class);
		 cuentaService = new CuentaServiceImpl(cuentaRepository,bancoRepository);
	}

	@Test
	void contextLoads() {

		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearcuenta1());
		when(cuentaRepository.findById(2L)).thenReturn(Datos.crearcuenta2());
		when(bancoRepository.findById(1L)).thenReturn(Datos.crearBanco());

		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);

		assertEquals("1000",saldoOrigen.toPlainString());
		assertEquals("2000",saldoDestino.toPlainString());

		cuentaService.transferir(1L,2L,new BigDecimal("100"),1L);

		saldoOrigen = cuentaService.revisarSaldo(1L);
		saldoDestino = cuentaService.revisarSaldo(2L);

		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(3)).findById(2L);

		assertEquals("900",saldoOrigen.toPlainString());
		assertEquals("2100",saldoDestino.toPlainString());
 		assertEquals(1, cuentaService.revisarTotalTransferencias(1L));
	}

	@Test
	void contextLoadsException() {

		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearcuenta1());
		when(cuentaRepository.findById(2L)).thenReturn(Datos.crearcuenta2());
		when(bancoRepository.findById(1L)).thenReturn(Datos.crearBanco());

		BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
		BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);

		assertEquals("1000",saldoOrigen.toPlainString());
		assertEquals("2000",saldoDestino.toPlainString());

		assertThrows(DineroInsuficienteExceptions.class, () -> {cuentaService.transferir(1L,2L,new BigDecimal("1200"),1L);});

		verify(cuentaRepository, times(2)).findById(1L);
		verify(cuentaRepository, times(1)).findById(2L);
		verify(cuentaRepository, never()).save(any(Cuenta.class));
		verify(bancoRepository,never()).findById(1L);
		verify(bancoRepository,never()).save(any(Banco.class));

	}
	@Test
	void contextLoads3() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.crearcuenta1());

		Cuenta cuenta1 = cuentaService.findById(1L);
		Cuenta cuenta2 = cuentaService.findById(1L);

		assertSame(cuenta1, cuenta2);
		assertTrue(cuenta1 == cuenta2);
		assertEquals("Andrés", cuenta1.getPersona());
		assertEquals("Andrés", cuenta2.getPersona());

		verify(cuentaRepository, times(2)).findById(1L);
	}

	@Test
	void testFindAll() {
		// Given
		List<Cuenta> datos = Arrays.asList(Datos.crearcuenta1().get(), Datos.crearcuenta2().get());
		when(cuentaRepository.findAll()).thenReturn(datos);

		// when
		List<Cuenta> cuentas = cuentaService.findAll();

		// then
		assertFalse(cuentas.isEmpty());
		assertEquals(2, cuentas.size());
		assertTrue(cuentas.contains(Datos.crearcuenta2().get()));

		verify(cuentaRepository).findAll();
	}

	@Test
	void testSave() {
		// given
		Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));
		when(cuentaRepository.save(any())).then(invocation ->{
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});

		// when
		Cuenta cuenta = cuentaService.save(cuentaPepe);
		// then
		assertEquals("Pepe", cuenta.getPersona());
		assertEquals(3, cuenta.getId());
		assertEquals("3000", cuenta.getSaldo().toPlainString());

		verify(cuentaRepository).save(any());
	}

}
