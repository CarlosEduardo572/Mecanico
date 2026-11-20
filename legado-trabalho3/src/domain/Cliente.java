package domain;

public class Cliente {
    private int idCliente;
    private String nome;
    private String cpf;
    private String contato;

    public Cliente(int idCliente, String nome, String cpf, String contato) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.cpf = cpf;
        this.contato = contato;
    }

    public String getNome() { return nome; }
    public String getContato() { return contato; }
}