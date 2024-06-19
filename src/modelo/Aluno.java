package modelo;

import java.util.Date;
import java.text.DecimalFormat;

public class Aluno {
    private String cpf;
    private String nome;
    private Date dataNascimento;
    private float peso;
    private float altura;

//Constructor
    public Aluno(String cpf, String nome, Date dataNascimento, float peso, float altura) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.peso = peso;
        this.altura = altura;
    }
    
//Getter e setters = alt+insert
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }
    
    //IMC, preciso conseguir chamar das outras classes agora
    public double calcularIMC(double peso, float altura) {
        double imc = peso / (altura * altura);
        
        return imc;
    }
    
    public String interpretarIMC(double peso, float altura) {
        double imc = calcularIMC(peso, altura);
        if (imc < 18.5) {
            return "Abaixo do peso";
        } else if (imc >= 18.5 && imc < 25) {
            return "Peso normal";
        } else if (imc >= 25 && imc < 30) {
            return "Sobrepeso";
        } else {
            return "Obesidade";
        }
    }
}
