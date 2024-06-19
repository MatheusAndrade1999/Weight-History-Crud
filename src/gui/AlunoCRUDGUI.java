package gui;

import dao.AlunoDAO;
import modelo.Aluno;
import modelo.HistoricoPeso;
import gui.HistoricoPesoCRUDGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlunoCRUDGUI extends JFrame {
    private JTextField cpfField, nomeField, dataNascimentoField, pesoField, alturaField;
    private AlunoDAO alunoDAO = new AlunoDAO();

    public AlunoCRUDGUI() {
        setTitle("CRUD de Aluno");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        panel.add(cpfField);

        panel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        panel.add(nomeField);

        panel.add(new JLabel("Data de Nascimento (dd/MM/yyyy):"));
        dataNascimentoField = new JTextField();
        panel.add(dataNascimentoField);

        panel.add(new JLabel("Peso:"));
        pesoField = new JTextField();
        panel.add(pesoField);

        panel.add(new JLabel("Altura:"));
        alturaField = new JTextField();
        panel.add(alturaField);

        JButton inserirButton = new JButton("Inserir");
        inserirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirAluno();
            }
        });
        panel.add(inserirButton);

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarAluno();
            }
        });
        panel.add(atualizarButton);

        JButton excluirButton = new JButton("Excluir");
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirAluno();
            }
        });
        panel.add(excluirButton);

        JButton consultarButton = new JButton("Consultar");
        consultarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultarAluno();
            }
        });
        panel.add(consultarButton);

        JButton historicoButton = new JButton("Histórico de Peso");
        historicoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = cpfField.getText();
                if (!cpf.isEmpty()) {
                    new HistoricoPesoCRUDGUI(cpf).setVisible(true);  // Passa o CPF como argumento
                } else {
                    JOptionPane.showMessageDialog(AlunoCRUDGUI.this, "Por favor, insira um CPF para visualizar o histórico de peso.");
                }
            }
        });
        panel.add(historicoButton);


        add(panel);
    }

    private void inserirAluno() {
        try {
            String cpf = cpfField.getText();
            String nome = nomeField.getText();
            Date dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(dataNascimentoField.getText());
            float peso = Float.parseFloat(pesoField.getText());
            float altura = Float.parseFloat(alturaField.getText());

            Aluno aluno = new Aluno(cpf, nome, dataNascimento, peso, altura);
            alunoDAO.inserir(aluno);

            JOptionPane.showMessageDialog(this, "Aluno inserido com sucesso!");
        } catch (ParseException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao inserir aluno: " + ex.getMessage());
        }

    }

    private void atualizarAluno() {
        try {
            String cpf = cpfField.getText();
            String nome = nomeField.getText();
            Date dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(dataNascimentoField.getText());
            float peso = Float.parseFloat(pesoField.getText());
            float altura = Float.parseFloat(alturaField.getText());

            Aluno aluno = new Aluno(cpf, nome, dataNascimento, peso, altura);
            alunoDAO.atualizar(aluno);

            JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso!");
        } catch (ParseException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar aluno: " + ex.getMessage());
        }
    }

    private void excluirAluno() {
        String cpf = cpfField.getText();
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um CPF válido para excluir.");
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o aluno com CPF " + cpf + "?");
        if (confirmacao == JOptionPane.YES_OPTION) {
            alunoDAO.excluir(cpf);
            JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!");
        }
    }

    private void consultarAluno() {
        String cpf = cpfField.getText();
        if (!cpf.isEmpty()) {
            Aluno aluno = alunoDAO.consultar(cpf);
            if (aluno != null) {
                nomeField.setText(aluno.getNome());
                dataNascimentoField.setText(new SimpleDateFormat("dd/MM/yyyy").format(aluno.getDataNascimento()));
                pesoField.setText(Float.toString(aluno.getPeso()));
                alturaField.setText(Float.toString(aluno.getAltura()));
            } else {
                JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, insira um CPF para consultar.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AlunoCRUDGUI().setVisible(true);
            }
        });
    }
}
