package performance;

import model.TipoVeiculo;
import model.Veiculo;
import org.junit.jupiter.api.Test;
import repository.InMemoryEstacionamentoRepository;
import service.CalculadoraTarifa;
import service.EstacionamentoService;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTimeout;

class DesempenhoTest {

    @Test
    void processamentoEmLoteDe1000EntradasESaidas_deveConcluirEmMenosDe500ms() {
        EstacionamentoService service = new EstacionamentoService(
                1000,
                new InMemoryEstacionamentoRepository(),
                new CalculadoraTarifa()
        );
        LocalDateTime entrada = LocalDateTime.of(2026, 11, 25, 8, 0);

        assertTimeout(Duration.ofMillis(500), () -> {
            for (int i = 0; i < 1000; i++) {
                String placa = String.format("ABC%04d", i);
                Veiculo v = new Veiculo(placa, TipoVeiculo.CARRO, false);
                service.registrarEntrada(v, entrada);
                service.registrarSaida(placa, entrada.plusMinutes(45));
            }
        });
    }
}