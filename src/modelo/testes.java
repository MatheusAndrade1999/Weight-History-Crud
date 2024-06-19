package modelo;

import dao.HistoricoPesoDAO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class testes {
    public static void main(String[] args) {
        try {
            // String de data no formato "DD/MM/YYYY"
            String dataPesoString = "20/06/2015";
            
            // Formato de entrada da string
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
            
            // Parsing da string para objeto Date
            Date dataPesoDate = formatoEntrada.parse(dataPesoString);
            
            // Formato de saída para o banco de dados
            SimpleDateFormat formatoSaida = new SimpleDateFormat("yyyy-MM-dd");
            
            // String formatada para inserção no banco de dados
            String dataPesoFormatada = formatoSaida.format(dataPesoDate);
            
            System.out.println("Data formatada para o banco de dados: " + dataPesoFormatada);
            
            // Criando objeto HistoricoPeso com a data formatada
            HistoricoPeso historicoPeso = new HistoricoPeso(1, "12345678902", dataPesoFormatada , 80.0);
            
            // Instanciando o DAO e inserindo o objeto no banco de dados
            HistoricoPesoDAO historicoPesoDAO = new HistoricoPesoDAO();
            System.out.println(historicoPesoDAO.consultar("12345678902").getCpfAluno());
            System.out.println("Registro de peso inserido com sucesso!");
        } catch (ParseException e) {
            System.out.println("Erro ao fazer o parsing da data. Certifique-se de que a data está no formato correto (DD/MM/YYYY).");
        }
    }
}
