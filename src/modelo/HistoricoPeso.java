package modelo;

public class HistoricoPeso {
    private int id;
    private String cpfAluno;
    private String dataPeso;
    private double peso;

    public HistoricoPeso(int id, String cpfAluno, String dataPeso, double peso) {
        this.id = id;
        this.cpfAluno = cpfAluno;
        this.dataPeso = dataPeso;
        this.peso = peso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpfAluno() {
        return cpfAluno;
    }

    public void setCpfAluno(String cpfAluno) {
        this.cpfAluno = cpfAluno;
    }

    public String getDataPeso() {
        return dataPeso;
    }

    public void setDataPeso(String dataPeso) {
        this.dataPeso = dataPeso;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
