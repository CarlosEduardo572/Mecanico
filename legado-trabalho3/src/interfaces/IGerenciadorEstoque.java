package interfaces;

public interface IGerenciadorEstoque {
    boolean consultarDisponibilidade(int idPeca, int qtd);
    void debitarEstoque(int idPeca, int qtd);
}