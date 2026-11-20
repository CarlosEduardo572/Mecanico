package domain;
import interfaces.ItemVendavel;

public class Servico implements ItemVendavel {
    private int idServico;
    private String descricao;
    private double precoMaoDeObra;

    public Servico(int idServico, String descricao, double precoMaoDeObra) {
        this.idServico = idServico;
        this.descricao = descricao;
        this.precoMaoDeObra = precoMaoDeObra;
    }

    @Override
    public double getPrecoUnitario() { return precoMaoDeObra; }

    @Override
    public String getDescricao() { return descricao; }
}