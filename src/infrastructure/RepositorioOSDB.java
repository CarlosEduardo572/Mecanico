package infrastructure;
import domain.OrdemServico;
import interfaces.IRepositorioOS;

public class RepositorioOSDB implements IRepositorioOS {
    @Override
    public void salvar(OrdemServico os) {
        System.out.println("Salvando/Atualizando OS " + os.getIdOS() + " no banco de dados...");
    }

    @Override
    public OrdemServico buscarPorId(int idOS) {
        System.out.println("Buscando OS " + idOS + " no banco de dados...");
        return null;
    }
}