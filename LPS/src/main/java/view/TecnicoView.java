package view;

import controller.TecnicoController;
import model.entities.Peca;
import model.entities.Atendimento;
import model.entities.Relatorio;
import model.entities.Dispositivo;
import javax.swing.Timer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.text.JTextComponent;

public class TecnicoView extends JFrame {

    private final TecnicoController tecnicoController;
    private JTabbedPane tabbedPane;

    // Peças
    private JTable tabelaPecas;

    // Dispositivos
    private JTable tabelaDispositivos;

    // Atendimentos
    private JTable tabelaAtendimentos;

    // Histórico de Relatórios
    private JTable tabelaHistoricoRelatorios;
    private JButton btnExcluirRelatorio;
    ;
    


    private JTextArea txtDescricaoServico;
    private JComboBox<String> cmbAtendimentosPendentes;
    private JComboBox<String> cmbPecasDisponiveis;
    private JTextField txtQuantidadePeca;
    private JButton btnAdicionarPeca;
    private JButton btnRemoverPeca;
    private JButton btnEnviarRelatorio;
    private DefaultListModel<String> listaPecasSelecionadas;
    private JList<String> listPecasSelecionadas;

    public TecnicoView() {
        this.tecnicoController = new TecnicoController(this);
        setTitle("Painel do Técnico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        JPanel painelFundo = criarPainelFundo();
        setContentPane(painelFundo);

        JButton btnLogout = new JButton("Sair");
        estilizarBotao(btnLogout);

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        logoutPanel.setBackground(new Color(30, 30, 30));
        logoutPanel.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 1));
        logoutPanel.add(btnLogout);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(30, 30, 30));
        toolBar.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(logoutPanel);

        tabbedPane = new JTabbedPane();
        estilizarAbas(tabbedPane);

        tabbedPane.addTab("Peças", criarPainelPecas());
        tabbedPane.addTab("Dispositivos", criarPainelDispositivos());
        tabbedPane.addTab("Atendimentos", criarPainelAtendimentos());
        tabbedPane.addTab("Relatórios", criarPainelRelatorios());
        tabbedPane.addTab("Histórico de Relatórios", criarPainelHistoricoRelatorios());

        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        configurarListeners();
        carregarDados();

        Timer timer = new Timer(5000, e -> carregarDados());
        timer.start();

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new TelaPrincipalView().setVisible(true);
            }
        });
    }

    private JPanel criarPainelPecas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(80, 80, 80));

        JLabel titulo = new JLabel("Gerenciar Peças", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(60, 60, 60));
        panelTitulo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        panelTitulo.add(titulo);

        panel.add(panelTitulo, BorderLayout.NORTH);

        String[] colunas = {"Código", "Nome", "Categoria", "Descrição", "Quantidade"};
        tabelaPecas = criarTabela(colunas);

        JScrollPane scrollPane = new JScrollPane(tabelaPecas);
        scrollPane.getViewport().setBackground(new Color(80, 80, 80));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel criarPainelDispositivos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(80, 80, 80));

        JLabel titulo = new JLabel("Gerenciar Dispositivos", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(60, 60, 60));
        panelTitulo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        panelTitulo.add(titulo);

        panel.add(panelTitulo, BorderLayout.NORTH);

        String[] colunas = {"Nome", "Categoria", "Marca", "Modelo", "Cliente", "Descrição"};
        tabelaDispositivos = criarTabela(colunas);

        JScrollPane scrollPane = new JScrollPane(tabelaDispositivos);
        scrollPane.getViewport().setBackground(new Color(80, 80, 80));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel criarPainelAtendimentos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(80, 80, 80));

        JLabel titulo = new JLabel("Gerenciar Atendimentos", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(60, 60, 60));
        panelTitulo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        panelTitulo.add(titulo);

        panel.add(panelTitulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Dispositivo", "Cliente", "Problema", "Status"};
        tabelaAtendimentos = criarTabela(colunas);

        JScrollPane scrollPane = new JScrollPane(tabelaAtendimentos);
        scrollPane.getViewport().setBackground(new Color(80, 80, 80));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel criarPainelRelatorios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(80, 80, 80));

        JLabel titulo = new JLabel("Criar Relatório Técnico", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(60, 60, 60));
        panelTitulo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        panelTitulo.add(titulo);

        panel.add(panelTitulo, BorderLayout.NORTH);

        String[] labels = {"Atendimento Pendente:", "Peça Disponível:", "Quantidade:", "Descrição do Serviço:"};
        cmbAtendimentosPendentes = new JComboBox<>();
        cmbPecasDisponiveis = new JComboBox<>();
        txtQuantidadePeca = new JTextField();
        txtDescricaoServico = new JTextArea(3, 20);

        estilizarComboBox(cmbAtendimentosPendentes);
        estilizarComboBox(cmbPecasDisponiveis);
        estilizarCampoEntrada(txtQuantidadePeca);
        estilizarTextArea(txtDescricaoServico);

        JComponent[] campos = {cmbAtendimentosPendentes, cmbPecasDisponiveis, txtQuantidadePeca, new JScrollPane(txtDescricaoServico)};
        JPanel panelFormulario = criarFormulario(labels, campos);
        panelFormulario.setBackground(new Color(85, 85, 85));

        panel.add(panelFormulario, BorderLayout.WEST);

        listaPecasSelecionadas = new DefaultListModel<>();
        listPecasSelecionadas = new JList<>(listaPecasSelecionadas);
        JScrollPane scrollPecasSelecionadas = new JScrollPane(listPecasSelecionadas);

        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBackground(new Color(80, 80, 80));
        panelLista.setBorder(BorderFactory.createTitledBorder("Peças Selecionadas"));
        panelLista.add(scrollPecasSelecionadas, BorderLayout.CENTER);
        panel.add(panelLista, BorderLayout.CENTER);

        btnAdicionarPeca = new JButton("Adicionar Peça");
        btnRemoverPeca = new JButton("Remover Peça");
        btnEnviarRelatorio = new JButton("Enviar Relatório");

        estilizarBotao(btnAdicionarPeca);
        estilizarBotao(btnRemoverPeca);
        estilizarBotao(btnEnviarRelatorio);

        JPanel panelBotoes = new JPanel();
        panelBotoes.setBackground(new Color(50, 50, 50));
        panelBotoes.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        panelBotoes.add(btnAdicionarPeca);
        panelBotoes.add(btnRemoverPeca);
        panelBotoes.add(btnEnviarRelatorio);

        panel.add(panelBotoes, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel criarPainelHistoricoRelatorios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(80, 80, 80));

        JLabel titulo = new JLabel("Histórico de Relatórios", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(60, 60, 60));
        panelTitulo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        panelTitulo.add(titulo);

        panel.add(panelTitulo, BorderLayout.NORTH);

        String[] colunas = {"ID Relatório", "ID Atendimento", "Status do Atendimento", "Peças Utilizadas", "Descrição do Serviço"};
        tabelaHistoricoRelatorios = criarTabela(colunas);

        JScrollPane scrollPane = new JScrollPane(tabelaHistoricoRelatorios);
        scrollPane.getViewport().setBackground(new Color(80, 80, 80));
        panel.add(scrollPane, BorderLayout.CENTER);

        btnExcluirRelatorio = new JButton("Excluir Relatório");
        estilizarBotao(btnExcluirRelatorio);

        JPanel panelBotoes = new JPanel();
        panelBotoes.setBackground(new Color(50, 50, 50));
        panelBotoes.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        panelBotoes.add(btnExcluirRelatorio);

        panel.add(panelBotoes, BorderLayout.SOUTH);

        return panel;
    }

    private void configurarListeners() {

        btnAdicionarPeca.addActionListener(e -> {
            String nomePeca = (String) cmbPecasDisponiveis.getSelectedItem();
            String quantidadeStr = txtQuantidadePeca.getText();

            String mensagem = tecnicoController.adicionarPecaLista(nomePeca, quantidadeStr);
            mostrarMensagem(mensagem);
            atualizarListaPecasSelecionadas();
        });

        btnRemoverPeca.addActionListener(e -> {
            String nomePecaSelecionada = listPecasSelecionadas.getSelectedValue();
            if (nomePecaSelecionada != null) {
                String mensagem = tecnicoController.removerPecaDoRelatorio(nomePecaSelecionada.split(" - ")[0]);
                mostrarMensagem(mensagem);
                atualizarListaPecasSelecionadas();
            } else {
                mostrarMensagem("Erro - Nenhuma peça selecionada para remover.");
            }
        });

        btnEnviarRelatorio.addActionListener(e -> {
            String atendimentoSelecionado = (String) cmbAtendimentosPendentes.getSelectedItem();
            String descricao = txtDescricaoServico.getText();

            String mensagem = tecnicoController.enviarRelatorioManutencao(atendimentoSelecionado, descricao);
            mostrarMensagem(mensagem);
            atualizarListaPecasSelecionadas();
            carregarDados();
        });

        btnExcluirRelatorio.addActionListener(e -> {
            int selectedRow = tabelaHistoricoRelatorios.getSelectedRow();
            if (selectedRow == -1) {
                mostrarMensagem("Erro - Selecione um relatório para excluir.");
                return;
            }

            Long idRelatorio = (Long) tabelaHistoricoRelatorios.getValueAt(selectedRow, 0);

            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir este relatório?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                String mensagem = tecnicoController.excluirRelatorio(idRelatorio);
                mostrarMensagem(mensagem);
                carregarDados();
            }
        });
    }

    public void atualizarListaAtendimentosPendentes(List<Atendimento> atendimentos) {
        cmbAtendimentosPendentes.removeAllItems();
        for (Atendimento atendimento : atendimentos) {
            cmbAtendimentosPendentes.addItem(atendimento.getId() + " - " + atendimento.getDispositivo().getNomeDispositivo());
        }
    }

    public void atualizarListaPecasDisponiveis(List<Peca> pecas) {
        cmbPecasDisponiveis.removeAllItems();
        for (Peca peca : pecas) {
            cmbPecasDisponiveis.addItem(peca.getNome());
        }
    }

    public void atualizarTabelaHistoricoRelatorios(List<Relatorio> relatorios) {
        DefaultTableModel model = (DefaultTableModel) tabelaHistoricoRelatorios.getModel();
        model.setRowCount(0);

        for (Relatorio relatorio : relatorios) {
            model.addRow(new Object[]{
                relatorio.getId(),
                relatorio.getAtendimento().getId(),
                relatorio.getAtendimento().getStatus(),
                relatorio.getPecasUtilizadas(),
                relatorio.getDescricaoServico()
            });
        }
    }

    public void atualizarTabelaPecas(List<Peca> pecas) {
        DefaultTableModel model = (DefaultTableModel) tabelaPecas.getModel();
        model.setRowCount(0);

        for (Peca peca : pecas) {
            model.addRow(new Object[]{
                peca.getCodigoPeca(),
                peca.getNome(),
                peca.getCategoria(),
                peca.getDescricao(),
                peca.getQuantidade()
            });
        }
    }

    public void atualizarTabelaDispositivos(List<Dispositivo> dispositivos) {
        DefaultTableModel model = (DefaultTableModel) tabelaDispositivos.getModel();
        model.setRowCount(0);

        for (Dispositivo dispositivo : dispositivos) {
            model.addRow(new Object[]{
                dispositivo.getNomeDispositivo(),
                dispositivo.getCategoria(),
                dispositivo.getMarca(),
                dispositivo.getModelo(),
                dispositivo.getClienteEmail(),
                dispositivo.getDescricao()
            });
        }
    }

    public void atualizarTabelaAtendimentos(List<Atendimento> atendimentos) {
        DefaultTableModel model = (DefaultTableModel) tabelaAtendimentos.getModel();
        model.setRowCount(0);

        for (Atendimento atendimento : atendimentos) {
            model.addRow(new Object[]{
                atendimento.getId(),
                atendimento.getDispositivo().getNomeDispositivo(),
                atendimento.getDispositivo().getClienteEmail(),
                atendimento.getProblema(),
                atendimento.getStatus()
            });
        }
    }

    private void carregarDados() {
        tecnicoController.listarPecasParaView();
        tecnicoController.listarDispositivosParaView();
        tecnicoController.listarAtendimentosPendentesParaView();
        tecnicoController.listarPecasDisponiveisParaView();
        tecnicoController.listarHistoricoRelatoriosParaView();
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    public void atualizarListaPecasSelecionadas() {
        listaPecasSelecionadas.clear();
        listaPecasSelecionadas.addAll(tecnicoController.getPecasSelecionadasFormatada());
    }

    private void estilizarCampoEntrada(JComponent campo) {
        campo.setFont(new Font("Consolas", Font.PLAIN, 14));
        campo.setBackground(new Color(20, 20, 20));
        campo.setForeground(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        if (campo instanceof JTextComponent) {
            ((JTextComponent) campo).setCaretColor(Color.LIGHT_GRAY);
        }
    }

    private void estilizarComboBox(JComboBox<?> comboBox) {
        estilizarCampoEntrada(comboBox);
        comboBox.setFocusable(false);

        comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton botao = super.createArrowButton();
                botao.setBackground(new Color(25, 25, 25));
                botao.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
                return botao;
            }
        });
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Consolas", Font.BOLD, 14));
        botao.setBackground(new Color(25, 25, 25));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(new Color(40, 40, 40));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(new Color(25, 25, 25));
            }
        });
    }

    private void estilizarLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(200, 200, 200));
    }

    private JPanel criarFormulario(String[] labels, JComponent[] campos) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(70, 70, 70));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            estilizarLabel(label);
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(label, gbc);

            estilizarCampoEntrada(campos[i]);
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.BOTH;
            panel.add(campos[i], gbc);
        }

        return panel;
    }

    private JTable criarTabela(String[] colunas) {
        DefaultTableModel model = new DefaultTableModel(new Object[0][colunas.length], colunas);
        JTable tabela = new JTable(model);
        tabela.setFont(new Font("Consolas", Font.PLAIN, 14));
        tabela.setForeground(Color.WHITE);
        tabela.setBackground(new Color(80, 80, 80));
        return tabela;
    }

    private JPanel criarPainelFundo() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(50, 50, 50));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    private void estilizarAbas(JTabbedPane tabbedPane) {
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setBackground(new Color(40, 40, 40));

        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement,
                    int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(isSelected ? new Color(50, 50, 50) : new Color(30, 30, 30));
                g2d.fillRect(x, y, w, h);
            }

            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                g.setColor(new Color(50, 50, 50));
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                g.setColor(new Color(50, 50, 50));
                g.drawRect(x, y, w, h);
            }
        });
    }

    private void estilizarTextArea(JTextArea textArea) {
        estilizarCampoEntrada(textArea);
    }

}
