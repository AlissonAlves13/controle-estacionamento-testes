package repository;

import model.Ticket;
import java.util.List;
import java.util.Optional;

public interface EstacionamentoRepository {
    void salvar(Ticket ticket);
    Optional<Ticket> buscarTicketAbertoPorPlaca(String placa);
    int contarTicketsAbertos();
    List<Ticket> listarTodos();
}