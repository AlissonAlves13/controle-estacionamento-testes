package service;

import model.TipoVeiculo;
import model.Veiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTarifaTest {

    private CalculadoraTarifa calculadora;
    private LocalDateTime baseEntrada;

    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraTarifa();
        baseEntrada = LocalDateTime.of(2026, 11, 25, 10, 0);
    }

    @ParameterizedTest(name = "Carro por {0} min deve pagar R$ {1}")
    @CsvSource({
            "0, 0.0",
            "15, 0.0",
            "16, 10.0",
            "60, 10.0",
            "61, 15.0",
            "120, 15.0",
            "121, 20.0"
    })
    void calcularTarifaCarro_deveRespeitarValoresLimiteDeTempo(int minutos, double valorEsperado) {
        Veiculo carro = new Veiculo("ABC1D23", TipoVeiculo.CARRO, false);
        LocalDateTime saida = baseEntrada.plusMinutes(minutos);

        double valorCalculado = calculadora.calcular(carro, baseEntrada, saida);

        assertEquals(valorEsperado, valorCalculado, 0.001);
    }

    @Test
    void calcularTarifaMoto_deveAplicarDescontoDe50PorCento() {
        Veiculo moto = new Veiculo("XYZ9876", TipoVeiculo.MOTO, false);
        LocalDateTime saida = baseEntrada.plusMinutes(45);

        double valor = calculadora.calcular(moto, baseEntrada, saida);

        assertEquals(5.0, valor, 0.001);
    }

    @Test
    void calcularTarifaMensalista_deveSerIsentoIndependenteDoTempo() {
        Veiculo mensalista = new Veiculo("MNS2026", TipoVeiculo.CARRO, true);
        LocalDateTime saida = baseEntrada.plusHours(5);

        double valor = calculadora.calcular(mensalista, baseEntrada, saida);

        assertEquals(0.0, valor, 0.001);
    }

    @Test
    void calcularTarifaComSaidaAnteriorAEntrada_deveLancarExcecao() {
        Veiculo carro = new Veiculo("ABC1D23", TipoVeiculo.CARRO, false);
        LocalDateTime saidaInvalida = baseEntrada.minusMinutes(1);

        assertThrows(IllegalArgumentException.class,
                () -> calculadora.calcular(carro, baseEntrada, saidaInvalida));
    }

    @Test
    void regressao_permanenciaExataDe15Minutos_deveManterIsencao() {
        Veiculo carro = new Veiculo("REG1A15", TipoVeiculo.CARRO, false);
        LocalDateTime saidaNoLimite = baseEntrada.plusMinutes(15);

        assertEquals(0.0, calculadora.calcular(carro, baseEntrada, saidaNoLimite), 0.001);
    }
}