package view;

import controller.ClienteController;
import javax.swing.Timer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.text.JTextComponent;

public class ClienteView extends JFrame {

    private final ClienteController clienteController;

    private JTabbedPane tabbedPane;

    // Dispositivos
    private JTable tabelaDispositivos;
    private JTextField txtNomeDispositivo, txtModelo, txtMarca;
    private JComboBox<String> cmbCategoria;
    private JTextArea txtDescricao;
    private JButton btnAdicionarDispositivo, btnEditarDispositivo, btnExcluirDispositivo;

    // Atendimentos
    private JTable tabelaAtendimentos;
    private JComboBox<String> cmbDispositivosAtendimento;
    private JTextArea txtProblemaAtendimento;
    private JButton btnSolicitarAtendimento, btnEditarAtendimento, btnExcluirAtendimento, btnFeedback;

    ;

    public ClienteView(String clienteEmail) {
        this.clienteController = new ClienteController(clienteEmail);
        JPanel painelFundo = criarPainelFundo();
        setContentPane(painelFundo);

        setTitle("Painel do Cliente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

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

        tabbedPane.addTab("Dispositivos", criarPainelDispositivos());
        tabbedPane.addTab("Atendimentos", criarPainelAtendimentos());

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

        String[] colunas = {"Nome", "Categoria", "Marca", "Modelo", "Descrição"};
        tabelaDispositivos = criarTabela(colunas);

        JScrollPane scrollPane = new JScrollPane(tabelaDispositivos);
        scrollPane.getViewport().setBackground(new Color(80, 80, 80));
        panel.add(scrollPane, BorderLayout.CENTER);

        txtNomeDispositivo = new JTextField();
        cmbCategoria = new JComboBox<>(new String[]{"Celular", "Notebook", "Tablet", "Outros"});
        txtMarca = new JTextField();
        txtModelo = new JTextField();
        txtDescricao = new JTextArea(3, 20);

        estilizarComboBox(cmbCategoria);
        estilizarTextArea(txtDescricao);

        JComponent[] campos = {txtNomeDispositivo, cmbCategoria, txtMarca, txtModelo, new JScrollPane(txtDescricao)};
        JPanel panelFormulario = criarFormulario(new String[]{"Nome do Dispositivo:", "Categoria:", "Marca:", "Modelo:", "Descrição:"}, campos);
        panelFormulario.setBackground(new Color(85, 85, 85));

        panel.add(panelFormulario, BorderLayout.WEST);

        btnAdicionarDispositivo = new JButton("Adicionar Dispositivo");
        btnEditarDispositivo = new JButton("Editar Dispositivo");
        btnExcluirDispositivo = new JButton("Excluir Dispositivo");

        estilizarBotao(btnAdicionarDispositivo);
        estilizarBotao(btnEditarDispositivo);
        estilizarBotao(btnExcluirDispositivo);

        JPanel panelBotoes = new JPanel();
        panelBotoes.setBackground(new Color(50, 50, 50));
        panelBotoes.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        panelBotoes.add(btnAdicionarDispositivo);
        panelBotoes.add(btnEditarDispositivo);
        panelBotoes.add(btnExcluirDispositivo);

        panel.add(panelBotoes, BorderLayout.SOUTH);

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

        String[] colunas = {"ID", "Dispositivo", "Problema", "Status", "Feedback"};
        tabelaAtendimentos = criarTabela(colunas);

        JScrollPane scrollPane = new JScrollPane(tabelaAtendimentos);
        scrollPane.getViewport().setBackground(new Color(80, 80, 80));
        panel.add(scrollPane, BorderLayout.CENTER);

        String[] labels = {"Selecionar Dispositivo:", "Descrição do Problema:"};
        cmbDispositivosAtendimento = new JComboBox<>();
        estilizarComboBox(cmbDispositivosAtendimento);
        txtProblemaAtendimento = new JTextArea(3, 20);
        estilizarTextArea(txtProblemaAtendimento);

        JComponent[] campos = {cmbDispositivosAtendimento, new JScrollPane(txtProblemaAtendimento)};
        JPanel panelFormulario = criarFormulario(labels, campos);
        panelFormulario.setBackground(new Color(85, 85, 85));

        panel.add(panelFormulario, BorderLayout.WEST);

        btnSolicitarAtendimento = new JButton("Criar Atendimento");
        btnEditarAtendimento = new JButton("Editar Atendimento");
        btnExcluirAtendimento = new JButton("Excluir Atendimento");
        btnFeedback = new JButton("Enviar Feedback");

        estilizarBotao(btnSolicitarAtendimento);
        estilizarBotao(btnEditarAtendimento);
        estilizarBotao(btnExcluirAtendimento);
        estilizarBotao(btnFeedback);

        JPanel panelBotoes = new JPanel();
        panelBotoes.setBackground(new Color(50, 50, 50));
        panelBotoes.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        panelBotoes.add(btnSolicitarAtendimento);
        panelBotoes.add(btnEditarAtendimento);
        panelBotoes.add(btnExcluirAtendimento);
        panelBotoes.add(btnFeedback);

        panel.add(panelBotoes, BorderLayout.SOUTH);

        return panel;
    }

    private void configurarListeners() {
        btnAdicionarDispositivo.addActionListener(e -> {
            String mensagem = clienteController.adicionarDispositivo(
                    txtNomeDispositivo.getText(),
                    (String) cmbCategoria.getSelectedItem(),
                    txtMarca.getText(),
                    txtModelo.getText(),
                    txtDescricao.getText()
            );
            mostrarMensagem(mensagem);
            carregarDados();
        });

        btnEditarDispositivo.addActionListener(e -> {
            int linhaSelecionada = tabelaDispositivos.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione um dispositivo para editar.");
                return;
            }

            String nomeDispositivo = (String) tabelaDispositivos.getValueAt(linhaSelecionada, 0);
            Long idDispositivo = clienteController.getIdDispositivoPorNome(nomeDispositivo);

            if (idDispositivo != null) {
                String mensagem = clienteController.editarDispositivo(
                        idDispositivo,
                        txtNomeDispositivo.getText(),
                        (String) cmbCategoria.getSelectedItem(),
                        txtMarca.getText(),
                        txtModelo.getText(),
                        txtDescricao.getText()
                );
                mostrarMensagem(mensagem);
                carregarDados();
            } else {
                mostrarMensagem("Erro: Dispositivo não encontrado.");
            }
        });

        btnExcluirDispositivo.addActionListener(e -> {
            int linhaSelecionada = tabelaDispositivos.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione um dispositivo para excluir.");
                return;
            }

            String nomeDispositivo = (String) tabelaDispositivos.getValueAt(linhaSelecionada, 0);
            Long idDispositivo = clienteController.getIdDispositivoPorNome(nomeDispositivo);

            if (idDispositivo != null) {
                String mensagem = clienteController.excluirDispositivo(idDispositivo);
                mostrarMensagem(mensagem);
                carregarDados();
            } else {
                mostrarMensagem("Erro: Dispositivo não encontrado.");
            }
        });

        btnSolicitarAtendimento.addActionListener(e -> {
            String dispositivoNome = (String) cmbDispositivosAtendimento.getSelectedItem();
            String problema = txtProblemaAtendimento.getText();

            String mensagem = clienteController.solicitarAtendimento(dispositivoNome, problema);
            mostrarMensagem(mensagem);
            carregarDados();
        });

        btnEditarAtendimento.addActionListener(e -> {
            int linhaSelecionada = tabelaAtendimentos.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione um atendimento para editar.");
                return;
            }

            Long idAtendimento = Long.parseLong(tabelaAtendimentos.getValueAt(linhaSelecionada, 0).toString());
            String novoProblema = txtProblemaAtendimento.getText();
            String novoDispositivoNome = (String) cmbDispositivosAtendimento.getSelectedItem();

            String mensagem = clienteController.editarAtendimento(idAtendimento, novoDispositivoNome, novoProblema);
            mostrarMensagem(mensagem);
            carregarDados();
        });

        btnExcluirAtendimento.addActionListener(e -> {
            int linhaSelecionada = tabelaAtendimentos.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione um atendimento para excluir.");
                return;
            }

            Long idAtendimento = Long.parseLong(tabelaAtendimentos.getValueAt(linhaSelecionada, 0).toString());

            String mensagem = clienteController.excluirAtendimento(idAtendimento);
            mostrarMensagem(mensagem);
            carregarDados();
        });

        btnFeedback.addActionListener(e -> {
            int linhaSelecionada = tabelaAtendimentos.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione um atendimento para enviar feedback.");
                return;
            }

            Long idAtendimento = Long.parseLong(tabelaAtendimentos.getValueAt(linhaSelecionada, 0).toString());

            JComboBox<String> cmbFeedbackNota = new JComboBox<>();
            for (int i = 1; i <= 10; i++) {
                cmbFeedbackNota.addItem(String.valueOf(i));
            }

            int resultado = JOptionPane.showConfirmDialog(this, cmbFeedbackNota, "Selecione a nota do atendimento", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                String notaSelecionada = (String) cmbFeedbackNota.getSelectedItem();
                String mensagem = clienteController.enviarFeedback(idAtendimento, notaSelecionada);
                mostrarMensagem(mensagem);
                carregarDados();
            }
        });

    }

    private void carregarDados() {
        atualizarTabelaDispositivos();
        atualizarComboBoxDispositivos(clienteController.getDispositivosParaComboBox());
        atualizarTabelaAtendimentos();
    }

    private void atualizarTabelaDispositivos() {
        DefaultTableModel model = (DefaultTableModel) tabelaDispositivos.getModel();
        {

        }
        model.setRowCount(0);
        clienteController.listarDispositivos().forEach(dispositivo
                -> model.addRow(new Object[]{
            dispositivo.getNomeDispositivo(),
            dispositivo.getCategoria(),
            dispositivo.getMarca(),
            dispositivo.getModelo(),
            dispositivo.getDescricao()
        })
        );
    }

    private void atualizarTabelaAtendimentos() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Dispositivo", "Problema", "Status", "Feedback"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        clienteController.listarAtendimentos().forEach(atendimento
                -> model.addRow(new Object[]{
            atendimento.getId(),
            atendimento.getDispositivo().getNomeDispositivo(),
            atendimento.getProblema(),
            atendimento.getStatus(),
            atendimento.getFeedbackNota()
        })
        );

        tabelaAtendimentos.setModel(model);

        if (tabelaAtendimentos.getColumnCount() > 0) {
            tabelaAtendimentos.getColumnModel().getColumn(0).setMinWidth(0);
            tabelaAtendimentos.getColumnModel().getColumn(0).setMaxWidth(0);
            tabelaAtendimentos.getColumnModel().getColumn(0).setPreferredWidth(0);
        }
    }

    private void atualizarComboBoxDispositivos(List<String> dispositivos) {
        cmbDispositivosAtendimento.removeAllItems();
        dispositivos.forEach(cmbDispositivosAtendimento::addItem);
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
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

    private void estilizarTextArea(JTextArea textArea) {
        estilizarCampoEntrada(textArea);
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

}
