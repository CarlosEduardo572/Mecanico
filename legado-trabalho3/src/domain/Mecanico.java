package domain;

public class Mecanico extends Funcionario {
    private String especialidade;

    public Mecanico(String matriculaFuncional, String nome, String especialidade) {
        this.matriculaFuncional = matriculaFuncional;
        this.nome = nome;
        this.cargo = "Mecânico";
        this.especialidade = especialidade;
    }

    @Override
    public void realizarTrabalho() {
        System.out.println("Mecânico " + nome + " executando o serviço na oficina.");
    }

    public void diagnosticarDefeito() {
        System.out.println("Diagnosticando veículo...");
    }
}