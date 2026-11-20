package interfaces;
import domain.Cliente;

public interface INotificador {
    void enviarMensagem(Cliente c, String msg);
}