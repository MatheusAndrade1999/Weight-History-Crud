package dao;

import factory.ConnectionFactory;
import modelo.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public void inserir(Aluno aluno) {
        String sql = "INSERT INTO aluno (cpf, nome, data_nascimento, peso, altura) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aluno.getCpf());
            stmt.setString(2, aluno.getNome());
            stmt.setDate(3, new java.sql.Date(aluno.getDataNascimento().getTime()));
            stmt.setFloat(4, aluno.getPeso());
            stmt.setFloat(5, aluno.getAltura());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir aluno", e);
        }
    }

    public void atualizar(Aluno aluno) {
        String sql = "UPDATE aluno SET nome = ?, data_nascimento = ?, peso = ?, altura = ? WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aluno.getNome());
            stmt.setDate(2, new java.sql.Date(aluno.getDataNascimento().getTime()));
            stmt.setFloat(3, aluno.getPeso());
            stmt.setFloat(4, aluno.getAltura());
            stmt.setString(5, aluno.getCpf());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar aluno", e);
        }
    }

    public void excluir(String cpf) {
        String sql = "DELETE FROM aluno WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir aluno", e);
        }
    }

    public Aluno consultar(String cpf) {
        String sql = "SELECT * FROM aluno WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                Date dataNascimento = rs.getDate("data_nascimento");
                float peso = rs.getFloat("peso");
                float altura = rs.getFloat("altura");

                return new Aluno(cpf, nome, dataNascimento, peso, altura);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar aluno", e);
        }

        return null;
    }

    public List<Aluno> listarTodos() {
        String sql = "SELECT * FROM aluno";
        List<Aluno> alunos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String cpf = rs.getString("cpf");
                String nome = rs.getString("nome");
                Date dataNascimento = rs.getDate("data_nascimento");
                float peso = rs.getFloat("peso");
                float altura = rs.getFloat("altura");

                alunos.add(new Aluno(cpf, nome, dataNascimento, peso, altura));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar alunos", e);
        }

        return alunos;
    }
}
