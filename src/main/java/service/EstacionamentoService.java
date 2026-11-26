package service;

import model.PerfilUsuario;
import model.Ticket;
import model.Veiculo;
import repository.EstacionamentoRepository;
import java.time.LocalDateTime;

public class EstacionamentoService {
    private final int capacidadeMaxima;
    private final EstacionamentoRepository repository;
    private final CalculadoraTarifa calculadora;

    public EstacionamentoService(int capacidadeMaxima,
                                 EstacionamentoRepository repository,
                                 CalculadoraTarifa calculadora) {
        if (capacidadeMaxima <= 0) {
            throw new IllegalArgumentException("Capacidade máxima deve ser maior que zero.");
        }
        this.capacidadeMaxima = capacidadeMaxima;
        this.repository = repository;
        this.calculadora = calculadora;
    }

    public Ticket registrarEntrada(Veiculo veiculo, LocalDateTime entrada) {
        if (veiculo == null || entrada == null) {
            throw new IllegalArgumentException("Veículo e horário de entrada são obrigatórios.");
        }
        if (repository.contarTicketsAbertos() >= capacidadeMaxima) {
            throw new IllegalStateException("Estacionamento lotado.");
        }
        if (repository.buscarTicketAbertoPorPlaca(veiculo.getPlaca()).isPresent()) {
            throw new IllegalStateException("Veículo já possui ticket em aberto: " + veiculo.getPlaca());
        }

        Ticket ticket = new Ticket(veiculo, entrada);
        repository.salvar(ticket);
        return ticket;
    }

    public Ticket registrarSaida(String placa, LocalDateTime saida) {
        Ticket ticket = repository.buscarTicketAbertoPorPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum ticket aberto encontrado para a placa: " + placa));

        double valor = calculadora.calcular(ticket.getVeiculo(), ticket.getEntrada(), saida);
        ticket.encerrar(saida, valor, false);
        repository.salvar(ticket);
        return ticket;
    }

    public Ticket liberarSemCobranca(String placa, LocalDateTime saida, PerfilUsuario perfil) {
        if (perfil == null) {
            throw new SecurityException("Operação sem identificação de perfil é impedida.");
        }
        if (perfil != PerfilUsuario.GERENTE) {
            throw new SecurityException("Apenas GERENTE pode realizar liberação gratuita manual.");
        }

        Ticket ticket = repository.buscarTicketAbertoPorPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException("Nenhum ticket aberto encontrado para a placa: " + placa));

        ticket.encerrar(saida, 0.0, true);
        repository.salvar(ticket);
        return ticket;
    }
}