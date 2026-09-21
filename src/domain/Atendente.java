package domain;

public class Atendente extends Funcionario {
    private String ramal;

    public Atendente(String matriculaFuncional, String nome, String ramal) {
        this.matriculaFuncional = matriculaFuncional;
        this.nome = nome;
        this.cargo = "Atendente";
        this.ramal = ramal;
    }

    @Override
    public void realizarTrabalho() {
        System.out.println("Atendente " + nome + " atendendo o cliente no balcão.");
    }

    public void abrirChamadoOS() {
        System.out.println("Iniciando processo de abertura de OS no sistema.");
    }
}