package controller;

import domain.Veiculo;
import domain.Mecanico;
import domain.OrdemServico;
import domain.ItemOS;
import domain.Peca;
import domain.Cliente;
import interfaces.IGerenciadorEstoque;
import interfaces.INotificador;
import interfaces.IRepositorioOS;

public class ControladorOrdemServico {

    private IGerenciadorEstoque estoque;
    private INotificador notificador;
    private IRepositorioOS repositorio;

    public ControladorOrdemServico(IGerenciadorEstoque estoque, INotificador notificador, IRepositorioOS repositorio) {
        this.estoque = estoque;
        this.notificador = notificador;
        this.repositorio = repositorio;
    }

    public void processarAberturaOS(Veiculo v, Mecanico m) {
        OrdemServico novaOS = new OrdemServico(1, v, m);

        repositorio.salvar(novaOS);
        System.out.println("OS aberta e salva para o veículo placa: " + v.getPlaca());
    }

    public void finalizarOS(int idOS) {
        OrdemServico os = repositorio.buscarPorId(idOS);

        if (os != null) {
            os.atualizarStatus("Finalizada");

            double valorTotal = os.calcularValorTotal();

            for(ItemOS item : os.getItens()) {
                if(item.getItem() instanceof Peca) {
                    Peca peca = (Peca) item.getItem();
                    estoque.debitarEstoque(peca.getIdPeca(), item.getQuantidade());
                }
            }

            repositorio.salvar(os);

            Cliente dono = os.getVeiculo().getProprietario();
            String mensagem = "Sua Ordem de Serviço foi finalizada. Valor Total: R$ " + valorTotal;
            notificador.enviarMensagem(dono, mensagem);
        } else {
            System.out.println("Ordem de Serviço não encontrada!");
        }
    }
}