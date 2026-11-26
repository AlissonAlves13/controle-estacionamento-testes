package model;

import java.time.LocalDateTime;

public class Ticket {
    private final Veiculo veiculo;
    private final LocalDateTime entrada;
    private LocalDateTime saida;
    private double valorPago;
    private boolean aberto;
    private boolean liberadoManualmente;

    public Ticket(Veiculo veiculo, LocalDateTime entrada) {
        if (veiculo == null || entrada == null) {
            throw new IllegalArgumentException("Veículo e data de entrada são obrigatórios.");
        }
        this.veiculo = veiculo;
        this.entrada = entrada;
        this.aberto = true;
    }

    public void encerrar(LocalDateTime saida, double valorPago, boolean liberadoManualmente) {
        if (saida == null || saida.isBefore(entrada)) {
            throw new IllegalArgumentException("Data de saída não pode ser anterior à entrada.");
        }
        this.saida = saida;
        this.valorPago = valorPago;
        this.liberadoManualmente = liberadoManualmente;
        this.aberto = false;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public double getValorPago() {
        return valorPago;
    }

    public boolean isAberto() {
        return aberto;
    }

    public boolean isLiberadoManualmente() {
        return liberadoManualmente;
    }
}