package domain;
import interfaces.ItemVendavel;

public class Peca implements ItemVendavel {
    private int idPeca;
    private String nome;
    private double valorUnitario;
    private int qtdEstoque;

    public Peca(int idPeca, String nome, double valorUnitario, int qtdEstoque) {
        this.idPeca = idPeca;
        this.nome = nome;
        this.valorUnitario = valorUnitario;
        this.qtdEstoque = qtdEstoque;
    }

    public int getIdPeca() { return idPeca; }

    @Override
    public double getPrecoUnitario() { return valorUnitario; }

    @Override
    public String getDescricao() { return nome; }

    public int getQtdEstoque() { return qtdEstoque; }
}