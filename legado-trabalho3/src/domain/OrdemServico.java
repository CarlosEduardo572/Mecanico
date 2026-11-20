package domain;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {
    private int idOS;
    private Date dataAbertura;
    private Date previsaoDevolucao;
    private String status;

    private Veiculo veiculo;
    private Mecanico mecanico;
    private List<ItemOS> itens = new ArrayList<>();

    public OrdemServico(int idOS, Veiculo veiculo, Mecanico mecanico) {
        this.idOS = idOS;
        this.veiculo = veiculo;
        this.mecanico = mecanico;
        this.dataAbertura = new Date();
        this.status = "Aberta";
    }

    public void adicionarItem(ItemOS item) {
        this.itens.add(item);
    }

    public double calcularValorTotal() {
        double total = 0;
        for(ItemOS item : itens) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    public void atualizarStatus(String novoStatus) {
        this.status = novoStatus;
    }

    public int getIdOS() { return idOS; }
    public Veiculo getVeiculo() { return veiculo; }
    public List<ItemOS> getItens() { return itens; }
}