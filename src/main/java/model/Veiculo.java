package model;

import java.util.regex.Pattern;

public class Veiculo {
    private static final Pattern PADRAO_PLACA = Pattern.compile("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$");

    private final String placa;
    private final TipoVeiculo tipo;
    private final boolean mensalista;

    public Veiculo(String placa, TipoVeiculo tipo, boolean mensalista) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("Placa não pode ser nula ou vazia.");
        }
        String placaNormalizada = placa.trim().toUpperCase();
        if (!PADRAO_PLACA.matcher(placaNormalizada).matches()) {
            throw new IllegalArgumentException("Formato de placa inválido: " + placa);
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo do veículo é obrigatório.");
        }
        this.placa = placaNormalizada;
        this.tipo = tipo;
        this.mensalista = mensalista;
    }

    public String getPlaca() {
        return placa;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    public boolean isMensalista() {
        return mensalista;
    }
}