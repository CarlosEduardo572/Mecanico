package domain;

public class Veiculo {
    private String placa;
    private String modelo;
    private String marca;
    private int ano;
    private Cliente proprietario;

    public Veiculo(String placa, String modelo, String marca, int ano, Cliente proprietario) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.proprietario = proprietario;
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public Cliente getProprietario() { return proprietario; }
}