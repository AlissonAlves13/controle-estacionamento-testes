package service;

import model.TipoVeiculo;
import model.Veiculo;
import java.time.Duration;
import java.time.LocalDateTime;

public class CalculadoraTarifa {
    public static final int TOLERANCIA_MINUTOS = 15;
    public static final double VALOR_PRIMEIRA_HORA = 10.0;
    public static final double VALOR_HORA_ADICIONAL = 5.0;

    public double calcular(Veiculo veiculo, LocalDateTime entrada, LocalDateTime saida) {
        if (veiculo == null || entrada == null || saida == null) {
            throw new IllegalArgumentException("Parâmetros obrigatórios não podem ser nulos.");
        }
        if (saida.isBefore(entrada)) {
            throw new IllegalArgumentException("Horário de saída anterior ao horário de entrada.");
        }

        if (veiculo.isMensalista()) {
            return 0.0;
        }

        long minutos = Duration.between(entrada, saida).toMinutes();

        if (minutos <= TOLERANCIA_MINUTOS) {
            return 0.0;
        }

        double valorBase;
        if (minutos <= 60) {
            valorBase = VALOR_PRIMEIRA_HORA;
        } else {
            long minutosExcedentes = minutos - 60;
            long horasAdicionais = (long) Math.ceil(minutosExcedentes / 60.0);
            valorBase = VALOR_PRIMEIRA_HORA + (horasAdicionais * VALOR_HORA_ADICIONAL);
        }

        if (veiculo.getTipo() == TipoVeiculo.MOTO) {
            return valorBase * 0.5;
        }

        return valorBase;
    }
}