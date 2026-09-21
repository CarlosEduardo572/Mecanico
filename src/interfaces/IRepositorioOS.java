package interfaces;
import domain.OrdemServico;

public interface IRepositorioOS {
    void salvar(OrdemServico os);
    OrdemServico buscarPorId(int idOS);
}