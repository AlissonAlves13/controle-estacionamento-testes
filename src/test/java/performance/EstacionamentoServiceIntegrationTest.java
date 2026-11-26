package performance;

import model.PerfilUsuario;
import model.Ticket;
import model.TipoVeiculo;
import model.Veiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.EstacionamentoRepository;
import repository.InMemoryEstacionamentoRepository;
import service.CalculadoraTarifa;
import service.EstacionamentoService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EstacionamentoServiceIntegrationTest {

    private EstacionamentoRepository repository;
    private CalculadoraTarifa calculadora;
    private EstacionamentoService service;
    private LocalDateTime agora;

    @BeforeEach
    void setUp() {
        repository = new InMemoryEstacionamentoRepository();
        calculadora = new CalculadoraTarifa();
        service = new EstacionamentoService(2, repository, calculadora);
        agora = LocalDateTime.of(2026, 11, 25, 14, 0);
    }

    @Test
    void fluxoCompletoEntradaESaida_deveCalcularTarifaELiberarVaga() {
        Veiculo carro = new Veiculo("KLM4E56", TipoVeiculo.CARRO, false);
        service.registrarEntrada(carro, agora);
        assertEquals(1, repository.contarTicketsAbertos());

        Ticket ticketEncerrado = service.registrarSaida("KLM4E56", agora.plusMinutes(90));

        assertAll(
                () -> assertFalse(ticketEncerrado.isAberto()),
                () -> assertEquals(15.0, ticketEncerrado.getValorPago(), 0.001),
                () -> assertEquals(0, repository.contarTicketsAbertos())
        );
    }

    @Test
    void registrarEntradaNoLimiteDeCapacidade_deveAceitarAteLotarERejeitarExcedente() {
        service.registrarEntrada(new Veiculo("AAA1A11", TipoVeiculo.CARRO, false), agora);
        service.registrarEntrada(new Veiculo("BBB2B22", TipoVeiculo.MOTO, false), agora);

        assertEquals(2, repository.contarTicketsAbertos());

        Veiculo terceiroVeiculo = new Veiculo("CCC3C33", TipoVeiculo.CARRO, false);
        assertThrows(IllegalStateException.class,
                () -> service.registrarEntrada(terceiroVeiculo, agora));
    }

    @Test
    void registrarEntradaComPlacaDuplicada_deveRejeitar() {
        service.registrarEntrada(new Veiculo("DUP1D23", TipoVeiculo.CARRO, false), agora);

        assertThrows(IllegalStateException.class,
                () -> service.registrarEntrada(new Veiculo("DUP1D23", TipoVeiculo.CARRO, false), agora.plusMinutes(5)));
    }

    @Test
    void criarVeiculoComPlacaInvalida_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Veiculo("123-INVALIDA", TipoVeiculo.CARRO, false));
    }

    @Test
    void seguranca_operadorSemPermissaoTentaLiberarSemCobranca_deveBloquear() {
        service.registrarEntrada(new Veiculo("SEC9A99", TipoVeiculo.CARRO, false), agora);

        assertThrows(SecurityException.class,
                () -> service.liberarSemCobranca("SEC9A99", agora.plusHours(2), PerfilUsuario.OPERADOR));
    }

    @Test
    void seguranca_tentativaDeLiberacaoSemIdentificacao_deveBloquear() {
        service.registrarEntrada(new Veiculo("SEC8A88", TipoVeiculo.CARRO, false), agora);

        assertThrows(SecurityException.class,
                () -> service.liberarSemCobranca("SEC8A88", agora.plusHours(2), null));
    }

    @Test
    void seguranca_gerenteAutorizadoLiberaSemCobranca_deveEncerrarComValorZero() {
        service.registrarEntrada(new Veiculo("GER7A77", TipoVeiculo.CARRO, false), agora);

        Ticket ticket = service.liberarSemCobranca("GER7A77", agora.plusHours(3), PerfilUsuario.GERENTE);

        assertTrue(ticket.isLiberadoManualmente());
        assertEquals(0.0, ticket.getValorPago(), 0.001);
        assertFalse(ticket.isAberto());
    }
}