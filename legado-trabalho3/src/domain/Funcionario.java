package domain;

public abstract class Funcionario {
    protected String matriculaFuncional;
    protected String nome;
    protected String cargo;

    public abstract void realizarTrabalho();
}