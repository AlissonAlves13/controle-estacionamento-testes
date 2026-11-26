package repository;

import model.Ticket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryEstacionamentoRepository implements EstacionamentoRepository {
    private final List<Ticket> tickets = new ArrayList<>();

    @Override
    public void salvar(Ticket ticket) {
        if (!tickets.contains(ticket)) {
            tickets.add(ticket);
        }
    }

    @Override
    public Optional<Ticket> buscarTicketAbertoPorPlaca(String placa) {
        return tickets.stream()
                .filter(t -> t.isAberto() && t.getVeiculo().getPlaca().equalsIgnoreCase(placa))
                .findFirst();
    }

    @Override
    public int contarTicketsAbertos() {
        return (int) tickets.stream().filter(Ticket::isAberto).count();
    }

    @Override
    public List<Ticket> listarTodos() {
        return new ArrayList<>(tickets);
    }
}