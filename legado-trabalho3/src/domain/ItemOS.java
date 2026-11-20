package domain;
import interfaces.ItemVendavel;

public class ItemOS {
    private int quantidade;
    private ItemVendavel item;

    public ItemOS(int quantidade, ItemVendavel item) {
        this.quantidade = quantidade;
        this.item = item;
    }

    public double calcularSubtotal() {
        return quantidade * item.getPrecoUnitario();
    }

    public int getQuantidade() { return quantidade; }
    public ItemVendavel getItem() { return item; }
}