package gui;

import dao.AlunoDAO;
import dao.HistoricoPesoDAO;
import modelo.HistoricoPeso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import javax.swing.JFileChooser;
import java.io.File;
import modelo.Aluno;

public class HistoricoPesoCRUDGUI extends JFrame {
    private JTextField cpfField;
    private HistoricoPesoDAO historicoPesoDAO = new HistoricoPesoDAO();
    private JTable table;
    
    public HistoricoPesoCRUDGUI(String cpf) {
    this(); // Chama o construtor padrão
    cpfField.setText(cpf);
    carregarRegistros(cpf);
}

    public HistoricoPesoCRUDGUI() {
        setTitle("Histórico de Peso");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Painel para consulta por CPF
        JPanel consultaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel cpfLabel = new JLabel("CPF do Aluno:");
        cpfField = new JTextField(15);
        JButton consultarButton = new JButton("Consultar");
        consultarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = cpfField.getText().trim();
                if (!cpf.isEmpty()) {
                    carregarRegistros(cpf);
                } else {
                    JOptionPane.showMessageDialog(HistoricoPesoCRUDGUI.this, "Digite o CPF do aluno para consultar.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        consultaPanel.add(cpfLabel);
        consultaPanel.add(cpfField);
        consultaPanel.add(consultarButton);
        panel.add(consultaPanel, BorderLayout.NORTH);

        // Tabela para exibir os registros de histórico de peso
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botões de ação
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton inserirButton = new JButton("Inserir Peso");
        inserirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirDialogoInserir();
            }
        });
        buttonPanel.add(inserirButton);
        
        //Atualiza o peso do campo especifico escolhido pelo usuario
        JButton atualizarButton = new JButton("Atualizar Peso");
        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(HistoricoPesoCRUDGUI.this, "Selecione um registro para atualizar.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                 String cpf = table.getValueAt(row, 0).toString(); // ID está na primeira coluna
                 String data = table.getValueAt(row, 1).toString();
                 String peso = table.getValueAt(row, 2).toString();
                HistoricoPeso historicoPeso = historicoPesoDAO.consultarPorId(cpf, data, peso);
                if (historicoPeso != null) {
                    abrirDialogoAtualizar(historicoPeso);
                } else {
                    JOptionPane.showMessageDialog(HistoricoPesoCRUDGUI.this, "Registro não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(atualizarButton);

        //Exclui o peso especifico escolhido pelo usuario
        JButton excluirButton = new JButton("Excluir Peso");
        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(HistoricoPesoCRUDGUI.this, "Selecione um registro para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    String cpf = table.getValueAt(row, 0).toString(); // ID está na primeira coluna
                    String data = table.getValueAt(row, 1).toString();
                    String peso = table.getValueAt(row, 2).toString();
                    historicoPesoDAO.excluir(cpf, data, peso);
                    carregarRegistros(cpfField.getText()); // Recarregar com o CPF atual
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(HistoricoPesoCRUDGUI.this, "Erro ao excluir o registro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(excluirButton);

        //Gera um arquivo txt no caminho da memoria que o usuario escolher
        JButton gerarArquivoButton = new JButton("Gerar Arquivo de Histórico");
        gerarArquivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gerarArquivoHistorico();
                    JOptionPane.showMessageDialog(HistoricoPesoCRUDGUI.this, "Arquivo de histórico gerado com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(HistoricoPesoCRUDGUI.this, "Erro ao gerar arquivo de histórico: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(gerarArquivoButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Inicializar com consulta vazia
        carregarRegistros("");
    }

        private void carregarRegistros(String cpf) {
            try {
                
                //Tabela e as colunas presentes pro usuario
                // Limpar dados da tabela
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("CPF");
                model.addColumn("Data");
                model.addColumn("Peso");
                model.addColumn("IMC");
                model.addColumn("Interpretação IMC");

                // Consultar registros de histórico de peso para o aluno
                List<HistoricoPeso> historicoPesoList = historicoPesoDAO.consultar(cpf);
                
                for (HistoricoPeso historicoPeso : historicoPesoList) {
                    
                    AlunoDAO alunoDAO = new AlunoDAO();
                    
                    Aluno aluno = alunoDAO.consultar(cpf);
                //Puxa os dados necessarios pra preencher a tabela    
                    model.addRow(new Object[]{
                        historicoPeso.getCpfAluno(),
                        historicoPeso.getDataPeso(),
                        historicoPeso.getPeso(),
                        String.format("%.2f",alunoDAO.consultar(cpf).calcularIMC(historicoPeso.getPeso(), aluno.getAltura())),
                        alunoDAO.consultar(cpf).interpretarIMC(historicoPeso.getPeso(), aluno.getAltura())
                            
                    });
                }

                // Definir modelo da tabela
                table.setModel(model);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao carregar registros de histórico de peso: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

        //Insere novos dados de peso, contendo data e peso do aluno nesse dia
    private void abrirDialogoInserir() {
        JFrame frame = new JFrame("Inserir Novo Peso");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JLabel dataLabel = new JLabel("Data (yyyy/MM/dd):");
        JTextField dataField = new JTextField();
        panel.add(dataLabel);
        panel.add(dataField);

        JLabel pesoLabel = new JLabel("Peso:");
        JTextField pesoField = new JTextField();
        panel.add(pesoLabel);
        panel.add(pesoField);

        JButton inserirButton = new JButton("Inserir");
        inserirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String dataPeso = dataField.getText();
                    double peso = Double.parseDouble(pesoField.getText());

                    HistoricoPeso novoHistorico = new HistoricoPeso(0, cpfField.getText(), dataPeso, peso);
                    historicoPesoDAO.inserir(novoHistorico);

                    carregarRegistros(cpfField.getText()); // Recarregar com o CPF atual
                    frame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(HistoricoPesoCRUDGUI.this, "Erro: Peso deve ser um número.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(HistoricoPesoCRUDGUI.this, "Erro ao inserir o registro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(inserirButton);

        frame.add(panel);
        frame.setVisible(true);
    }


    private void abrirDialogoAtualizar(HistoricoPeso historicoPeso) {
        JFrame frame = new JFrame("Atualizar Peso");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JLabel dataLabel = new JLabel("Data (yyyy/MM/dd):");
        JTextField dataField = new JTextField(historicoPeso.getDataPeso());
        panel.add(dataLabel);
        panel.add(dataField);

        JLabel pesoLabel = new JLabel("Peso:");
        JTextField pesoField = new JTextField(String.valueOf(historicoPeso.getPeso()));
        panel.add(pesoLabel);
        panel.add(pesoField);

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try {

                    historicoPeso.setDataPeso(dataField.getText());
                    historicoPeso.setPeso(Double.parseDouble(pesoField.getText()));

                    historicoPesoDAO.atualizar(historicoPeso);
                    

                    carregarRegistros(cpfField.getText()); // Recarregar com o CPF atual
                    frame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(HistoricoPesoCRUDGUI.this, "Erro: Peso deve ser um número.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(HistoricoPesoCRUDGUI.this, "Erro ao atualizar o registro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(atualizarButton);

        frame.add(panel);
        frame.setVisible(true);
    }


            private void gerarArquivoHistorico() throws IOException {
                List<HistoricoPeso> historicoPesoList = historicoPesoDAO.consultar(cpfField.getText());

            StringBuilder sb = new StringBuilder();
            sb.append("Histórico de Peso do Aluno\n\n");
            sb.append("CPF do Aluno: ").append(cpfField.getText()).append("\n\n");
            sb.append(String.format("%-12s %-10s %-10s %-20s\n", "Data", "Peso", "IMC", "Interpretação IMC"));
            sb.append("-----------------------------------------------------------------\n");

            AlunoDAO alunoDAO = new AlunoDAO();
            Aluno aluno = alunoDAO.consultar(cpfField.getText());

            for (HistoricoPeso historicoPeso : historicoPesoList) {
                double imc = aluno.calcularIMC(historicoPeso.getPeso(), aluno.getAltura());
                String interpretacaoIMC = aluno.interpretarIMC(historicoPeso.getPeso(), aluno.getAltura());

                sb.append(String.format("%-12s %-10.1f %-10.2f %-20s\n",
                        
                        historicoPeso.getDataPeso(),
                        historicoPeso.getPeso(),
                        imc,
                        interpretacaoIMC));
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Escolha onde salvar o arquivo");
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                Files.write(Paths.get(fileToSave.getAbsolutePath()), sb.toString().getBytes(), StandardOpenOption.CREATE);
            }
        }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HistoricoPesoCRUDGUI().setVisible(true);
            }
        });
    }
}
