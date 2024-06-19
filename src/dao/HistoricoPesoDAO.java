package dao;

import factory.ConnectionFactory;
import modelo.HistoricoPeso;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoricoPesoDAO {

    public void inserir(HistoricoPeso historicoPeso) {
        String sql = "INSERT INTO historico_peso (cpf_aluno, data_peso, peso) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, historicoPeso.getCpfAluno());
            stmt.setString(2, historicoPeso.getDataPeso());
            stmt.setDouble(3, historicoPeso.getPeso());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir histórico de peso", e);
        }
    }

    public void atualizar(HistoricoPeso historicoPeso) {
        String sql = "UPDATE historico_peso SET data_peso = ?, peso = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, historicoPeso.getDataPeso());
            stmt.setDouble(2, historicoPeso.getPeso());
            stmt.setInt(3, historicoPeso.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar histórico de peso", e);
        }
    }

    public void excluir(String cpf, String data, String peso) {
        String sql = "DELETE FROM historico_peso WHERE cpf_aluno = ? and data_peso = ? and peso = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.setString(2, data);
            stmt.setString(3, peso);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir histórico de peso", e);
        }
    }

        public List<HistoricoPeso> consultar(String cpf_aluno) {
        String sql = "SELECT * FROM historico_peso WHERE cpf_aluno = ?";
        List<HistoricoPeso> historicos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf_aluno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String cpfAluno = rs.getString("cpf_aluno");
                String dataPeso = rs.getString("data_peso");
                double peso = rs.getDouble("peso");

                historicos.add(new HistoricoPeso(id, cpfAluno, dataPeso, peso));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar históricos de peso", e);
        }

        return historicos;
    }


    public HistoricoPeso consultarPorId(String cpf, String data, String peso) {
        String sql = "SELECT * FROM historico_peso WHERE cpf_aluno = ? and peso = ? and data_peso = ?";
        HistoricoPeso historicoPeso = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.setString(2, peso);
            stmt.setString(3, data);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String cpfAluno = rs.getString("cpf_aluno");
                String dataPeso = rs.getString("data_peso");
                float pesoAluno = rs.getFloat("peso");
                int id =  rs.getInt("id");

                historicoPeso = new HistoricoPeso(id, cpfAluno, dataPeso, pesoAluno);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar histórico de peso por ID", e);
        }

        return historicoPeso;
    }

    
}

