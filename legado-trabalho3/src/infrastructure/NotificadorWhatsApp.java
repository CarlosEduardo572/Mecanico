package infrastructure;
import domain.Cliente;
import interfaces.INotificador;

public class NotificadorWhatsApp implements INotificador {
    @Override
    public void enviarMensagem(Cliente c, String msg) {
        System.out.println("Enviando WhatsApp para " + c.getNome() + " (" + c.getContato() + "): " + msg);
    }
}