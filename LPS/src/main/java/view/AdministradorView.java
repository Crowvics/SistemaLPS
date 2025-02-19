package view;

import javax.swing.Timer;

import controller.AdministradorController;

import model.entities.Usuario;
import model.entities.Peca;
import model.entities.Dispositivo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.text.JTextComponent;
import model.entities.Atendimento;
import model.entities.Relatorio;

public class AdministradorView extends JFrame {

    private final AdministradorController administradorController;

    private JTabbedPane tabbedPane;

    // Usuários
    private JTable tabelaUsuarios;
    private JTextField txtNomeUsuario, txtEmailUsuario, txtCpfUsuario;
    private JPasswordField txtSenhaUsuario;
    private JComboBox<String> cmbTipoUsuario;
    private JButton btnNovoUsuario, btnEditarUsuario, btnExcluirUsuario;

    // Peças
    private JTable tabelaPecas;
    private JTextField txtCodigoPeca, txtNomePeca, txtCategoriaPeca, txtDescricaoPeca, txtQuantidadePeca;
    private JButton btnNovaPeca, btnEditarPeca, btnExcluirPeca;

    // Dispositivos
    private JTable tabelaDispositivos;
    private JTextField txtNomeDispositivo, txtMarcaDispositivo, txtModeloDispositivo, txtDescricaoDispositivo, txtClienteEmail;
    private JComboBox<String> cmbCategoriaDispositivo;
    private JButton btnNovoDispositivo, btnEditarDispositivo, btnExcluirDispositivo;

    // Atendimento
    private JTable tabelaAtendimentos;
    private JComboBox<String> cmbStatusAtendimento;
    private JButton btnEditarAtendimento, btnExcluirAtendimento;

    //Historico de Relatórios
    private JTable tabelaHistoricoRelatorios;
    private JButton btnExcluirRelatorio;

    public AdministradorView() {
        this.administradorController = new AdministradorController(this);
        setTitle("Painel do Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
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

        tabbedPane.addTab("Usuários", criarPainelUsuarios());
        tabbedPane.addTab("Peças", criarPainelPecas());
        tabbedPane.addTab("Dispositivos", criarPainelDispositivos());
        tabbedPane.addTab("Atendimentos", criarPainelAtendimentos());
        tabbedPane.addTab("Histórico de Relatórios", criarPainelHistoricoRelatorios());

        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        configurarListeners();
        carregarDados();
        carregarCategorias();

        Timer timer = new Timer(5000, e -> carregarDados());
        timer.start();

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new TelaPrincipalView().setVisible(true);
            }
        });
    }

    private JPanel criarPainelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(80, 80, 80));

        JLabel titulo = new JLabel("Gerenciar Usuários", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(60, 60, 60));
        panelTitulo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        panelTitulo.add(titulo);

        panel.add(panelTitulo, BorderLayout.NORTH);

        String[] colunas = {"Nome", "Email", "CPF", "Tipo"};
        tabelaUsuarios = criarTabela(colunas);

        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        scrollPane.getViewport().setBackground(new Color(80, 80, 80));
        panel.add(scrollPane, BorderLayout.CENTER);

        String[] labels = {"Nome:", "Email:", "Senha:", "CPF:", "Tipo de Usuário:"};
        txtNomeUsuario = new JTextField();
        txtEmailUsuario = new JTextField();
        txtSenhaUsuario = new JPasswordField();
        txtCpfUsuario = new JTextField();
        cmbTipoUsuario = new JComboBox<>(new String[]{"Administrador", "Técnico", "Cliente"});

        estilizarComboBox(cmbTipoUsuario);
        estilizarCampoEntrada(txtNomeUsuario);
        estilizarCampoEntrada(txtEmailUsuario);
        estilizarCampoEntrada(txtSenhaUsuario);
        estilizarCampoEntrada(txtCpfUsuario);

        JComponent[] campos = {txtNomeUsuario, txtEmailUsuario, txtSenhaUsuario, txtCpfUsuario, cmbTipoUsuario};
        JPanel panelFormulario = criarFormulario(labels, campos);
        panelFormulario.setBackground(new Color(85, 85, 85));

        panel.add(panelFormulario, BorderLayout.WEST);

        btnNovoUsuario = new JButton("Novo");
        btnEditarUsuario = new JButton("Editar");
        btnExcluirUsuario = new JButton("Excluir");

        estilizarBotao(btnNovoUsuario);
        estilizarBotao(btnEditarUsuario);
        estilizarBotao(btnExcluirUsuario);

        JPanel panelBotoes = new JPanel();
        panelBotoes.setBackground(new Color(50, 50, 50));
        panelBotoes.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        panelBotoes.add(btnNovoUsuario);
        panelBotoes.add(btnEditarUsuario);
        panelBotoes.add(btnExcluirUsuario);

        panel.add(panelBotoes, BorderLayout.SOUTH);
        return panel;
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

        String[] labels = {"Código:", "Nome:", "Categoria:", "Descrição:", "Quantidade:"};
        txtCodigoPeca = new JTextField();
        txtNomePeca = new JTextField();
        txtCategoriaPeca = new JTextField();
        txtDescricaoPeca = new JTextField();
        txtQuantidadePeca = new JTextField();

        estilizarCampoEntrada(txtCodigoPeca);
        estilizarCampoEntrada(txtNomePeca);
        estilizarCampoEntrada(txtCategoriaPeca);
        estilizarCampoEntrada(txtDescricaoPeca);
        estilizarCampoEntrada(txtQuantidadePeca);

        JComponent[] campos = {txtCodigoPeca, txtNomePeca, txtCategoriaPeca, txtDescricaoPeca, txtQuantidadePeca};
        JPanel panelFormulario = criarFormulario(labels, campos);
        panelFormulario.setBackground(new Color(85, 85, 85));

        panelFormulario.setPreferredSize(new Dimension(320, 1000));
        panel.add(panelFormulario, BorderLayout.WEST);

        // Criando botões e aplicando estilos
        btnNovaPeca = new JButton("Novo");
        btnEditarPeca = new JButton("Editar");
        btnExcluirPeca = new JButton("Excluir");

        estilizarBotao(btnNovaPeca);
        estilizarBotao(btnEditarPeca);
        estilizarBotao(btnExcluirPeca);

        JPanel panelBotoes = new JPanel();
        panelBotoes.setBackground(new Color(50, 50, 50));
        panelBotoes.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        panelBotoes.add(btnNovaPeca);
        panelBotoes.add(btnEditarPeca);
        panelBotoes.add(btnExcluirPeca);

        panel.add(panelBotoes, BorderLayout.SOUTH);

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

        cmbCategoriaDispositivo = new JComboBox<>();
        txtMarcaDispositivo = new JTextField();
        txtModeloDispositivo = new JTextField();
        txtDescricaoDispositivo = new JTextField();

        estilizarComboBox(cmbCategoriaDispositivo);
        estilizarCampoEntrada(txtMarcaDispositivo);
        estilizarCampoEntrada(txtModeloDispositivo);
        estilizarCampoEntrada(txtDescricaoDispositivo);

        String[] labels = {"Categoria:", "Marca:", "Modelo:", "Descrição:"};
        JComponent[] campos = {cmbCategoriaDispositivo, txtMarcaDispositivo, txtModeloDispositivo, txtDescricaoDispositivo};

        JPanel panelFormulario = criarFormulario(labels, campos);
        panelFormulario.setBackground(new Color(85, 85, 85));

        panel.add(panelFormulario, BorderLayout.WEST);

        btnEditarDispositivo = new JButton("Editar Dispositivo");
        btnExcluirDispositivo = new JButton("Excluir Dispositivo");

        estilizarBotao(btnEditarDispositivo);
        estilizarBotao(btnExcluirDispositivo);

        JPanel panelBotoes = new JPanel();
        panelBotoes.setBackground(new Color(50, 50, 50));
        panelBotoes.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
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

        String[] colunas = {"ID", "Dispositivo", "Cliente", "Problema", "Status", "Feedback"};
        tabelaAtendimentos = criarTabela(colunas);

        JScrollPane scrollPane = new JScrollPane(tabelaAtendimentos);
        scrollPane.getViewport().setBackground(new Color(80, 80, 80));
        panel.add(scrollPane, BorderLayout.CENTER);

        String[] labels = {"Novo Status:"};
        cmbStatusAtendimento = new JComboBox<>(new String[]{"Aguardando análise", "Em andamento", "Finalizado"});
        estilizarComboBox(cmbStatusAtendimento);

        JComponent[] campos = {cmbStatusAtendimento};
        JPanel panelFormulario = criarFormulario(labels, campos);
        panelFormulario.setBackground(new Color(85, 85, 85));

        panel.add(panelFormulario, BorderLayout.WEST);

        btnEditarAtendimento = new JButton("Alterar Status");
        btnExcluirAtendimento = new JButton("Excluir Atendimento");

        estilizarBotao(btnEditarAtendimento);
        estilizarBotao(btnExcluirAtendimento);

        JPanel panelBotoes = new JPanel();
        panelBotoes.setBackground(new Color(50, 50, 50));
        panelBotoes.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        panelBotoes.add(btnEditarAtendimento);
        panelBotoes.add(btnExcluirAtendimento);

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
        btnNovoUsuario.addActionListener(e -> {
            String mensagem = administradorController.adicionarUsuario(
                    txtNomeUsuario.getText(),
                    txtCpfUsuario.getText(),
                    txtEmailUsuario.getText(),
                    new String(txtSenhaUsuario.getPassword()),
                    (String) cmbTipoUsuario.getSelectedItem()
            );
            mostrarMensagem(mensagem);
            administradorController.listarUsuariosParaView();
        });

        btnEditarUsuario.addActionListener(e -> {
            int linhaSelecionada = tabelaUsuarios.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione um usuário para editar.");
                return;
            }

            try {
                Long idUsuario = Long.parseLong(tabelaUsuarios.getValueAt(linhaSelecionada, 0).toString());

                String mensagem = administradorController.editarUsuario(
                        idUsuario,
                        txtNomeUsuario.getText(),
                        txtCpfUsuario.getText(),
                        txtEmailUsuario.getText(),
                        new String(txtSenhaUsuario.getPassword()),
                        (String) cmbTipoUsuario.getSelectedItem()
                );
                mostrarMensagem(mensagem);
                administradorController.listarUsuariosParaView();
            } catch (NumberFormatException ex) {
                mostrarMensagem("Erro ao obter ID do usuário. ID inválido.");
            }
        });

        btnExcluirUsuario.addActionListener(e -> {
            int linhaSelecionada = tabelaUsuarios.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione um usuário para excluir.");
                return;
            }

            try {
                Long idUsuario = Long.parseLong(tabelaUsuarios.getValueAt(linhaSelecionada, 0).toString());
                String mensagem = administradorController.excluirUsuario(idUsuario);
                mostrarMensagem(mensagem);
                administradorController.listarUsuariosParaView();
            } catch (NumberFormatException ex) {
                mostrarMensagem("Erro ao obter ID do usuário. ID inválido.");
            }
        });

        btnNovaPeca.addActionListener(e -> {
            String mensagem = administradorController.adicionarPeca(
                    txtCodigoPeca.getText(),
                    txtNomePeca.getText(),
                    txtCategoriaPeca.getText(),
                    txtDescricaoPeca.getText(),
                    txtQuantidadePeca.getText()
            );
            mostrarMensagem(mensagem);
            administradorController.listarPecasParaView();
        });

        btnEditarPeca.addActionListener(e -> {
            int linhaSelecionada = tabelaPecas.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione uma peça para editar.");
                return;
            }

            try {
                Long idPeca = Long.parseLong(tabelaPecas.getValueAt(linhaSelecionada, 0).toString());

                String mensagem = administradorController.editarPeca(
                        idPeca,
                        txtCodigoPeca.getText(),
                        txtNomePeca.getText(),
                        txtCategoriaPeca.getText(),
                        txtDescricaoPeca.getText(),
                        txtQuantidadePeca.getText()
                );
                mostrarMensagem(mensagem);
                administradorController.listarPecasParaView();
            } catch (NumberFormatException ex) {
                mostrarMensagem("Erro ao obter ID da peça. ID inválido.");
            }
        });

        btnExcluirPeca.addActionListener(e -> {
            int linhaSelecionada = tabelaPecas.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione uma peça para excluir.");
                return;
            }

            try {
                Long idPeca = Long.parseLong(tabelaPecas.getValueAt(linhaSelecionada, 0).toString());
                String mensagem = administradorController.excluirPeca(idPeca);
                mostrarMensagem(mensagem);
                administradorController.listarPecasParaView();
            } catch (NumberFormatException ex) {
                mostrarMensagem("Erro ao obter ID da peça. ID inválido.");
            }
        });

        btnEditarDispositivo.addActionListener(e -> {
            int linhaSelecionada = tabelaDispositivos.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione um dispositivo para editar.");
                return;
            }

            try {

                Long idDispositivo = (Long) tabelaDispositivos.getValueAt(linhaSelecionada, 0);

                String mensagem = administradorController.editarDispositivo(
                        idDispositivo,
                        (String) cmbCategoriaDispositivo.getSelectedItem(),
                        txtMarcaDispositivo.getText(),
                        txtModeloDispositivo.getText(),
                        txtDescricaoDispositivo.getText()
                );

                mostrarMensagem(mensagem);
                administradorController.listarDispositivosParaView();
            } catch (ClassCastException ex) {
                mostrarMensagem("Erro ao obter ID do dispositivo. ID inválido.");
            } catch (Exception ex) {
                mostrarMensagem("Erro ao editar dispositivo: " + ex.getMessage());
            }
        });

        btnExcluirDispositivo.addActionListener(e -> {
            int linhaSelecionada = tabelaDispositivos.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione um dispositivo para excluir.");
                return;
            }

            try {

                Long idDispositivo = (Long) tabelaDispositivos.getValueAt(linhaSelecionada, 0);

                String mensagem = administradorController.excluirDispositivo(idDispositivo);

                mostrarMensagem(mensagem);
                administradorController.listarDispositivosParaView();
            } catch (ClassCastException ex) {
                mostrarMensagem("Erro ao obter ID do dispositivo. ID inválido.");
            } catch (Exception ex) {
                mostrarMensagem("Erro ao excluir dispositivo: " + ex.getMessage());
            }
        });

        btnEditarAtendimento.addActionListener(e -> {
            int linhaSelecionada = tabelaAtendimentos.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione um atendimento para alterar o status.");
                return;
            }

            Long idAtendimento = (Long) tabelaAtendimentos.getValueAt(linhaSelecionada, 0);
            String novoStatus = (String) cmbStatusAtendimento.getSelectedItem();

            String mensagem = administradorController.editarAtendimentoAdmin(idAtendimento, novoStatus);
            mostrarMensagem(mensagem);
            administradorController.listarAtendimentosParaView();
        });

        btnExcluirAtendimento.addActionListener(e -> {
            int linhaSelecionada = tabelaAtendimentos.getSelectedRow();
            if (linhaSelecionada == -1) {
                mostrarMensagem("Selecione um atendimento para excluir.");
                return;
            }

            Long idAtendimento = (Long) tabelaAtendimentos.getValueAt(linhaSelecionada, 0);
            String mensagem = administradorController.excluirAtendimentoAdmin(idAtendimento);
            mostrarMensagem(mensagem);
            administradorController.listarAtendimentosParaView();
        });

        btnExcluirRelatorio.addActionListener(e -> {
            int selectedRow = tabelaHistoricoRelatorios.getSelectedRow();
            if (selectedRow == -1) {
                mostrarMensagem("Erro - Selecione um relatório para excluir.");
                return;
            }

            Long idRelatorio = (Long) tabelaHistoricoRelatorios.getValueAt(selectedRow, 0);
            String statusAtendimento = (String) tabelaHistoricoRelatorios.getValueAt(selectedRow, 2);

            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir este relatório?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                String mensagem = administradorController.excluirRelatorioComoAdministrador(idRelatorio);
                mostrarMensagem(mensagem);
                administradorController.listarRelatoriosParaView();
            }
        });

    }

    private void carregarDados() {
        administradorController.listarUsuariosParaView();
        administradorController.listarPecasParaView();
        administradorController.listarDispositivosParaView();
        administradorController.listarAtendimentosParaView();
        administradorController.listarRelatoriosParaView();
    }

    private void carregarCategorias() {
        List<String> categorias = administradorController.listarCategorias();
        cmbCategoriaDispositivo.removeAllItems();
        for (String categoria : categorias) {
            cmbCategoriaDispositivo.addItem(categoria);
        }
    }

    public void mostrarMensagem(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem);
    }

    public void atualizarTabelaUsuarios(List<Usuario> usuarios) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nome", "Email", "CPF", "Tipo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Usuario usuario : usuarios) {
            model.addRow(new Object[]{
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getTipoUsuario()
            });
        }

        tabelaUsuarios.setModel(model);

        if (tabelaUsuarios.getColumnCount() > 0) {
            tabelaUsuarios.getColumnModel().getColumn(0).setMinWidth(0);
            tabelaUsuarios.getColumnModel().getColumn(0).setMaxWidth(0);
            tabelaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(0);
        }
    }

    public void atualizarTabelaPecas(List<Peca> pecas) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Código", "Nome", "Categoria", "Descrição", "Quantidade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Peca peca : pecas) {
            model.addRow(new Object[]{
                peca.getId(),
                peca.getCodigoPeca(),
                peca.getNome(),
                peca.getCategoria(),
                peca.getDescricao(),
                peca.getQuantidade()
            });
        }

        tabelaPecas.setModel(model);

        if (tabelaPecas.getColumnCount() > 0) {
            tabelaPecas.getColumnModel().getColumn(0).setMinWidth(0);
            tabelaPecas.getColumnModel().getColumn(0).setMaxWidth(0);
            tabelaPecas.getColumnModel().getColumn(0).setPreferredWidth(0);
        }
    }

    public void atualizarTabelaDispositivos(List<Dispositivo> dispositivos) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nome", "Categoria", "Marca", "Modelo", "Descrição", "Cliente"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Dispositivo dispositivo : dispositivos) {
            model.addRow(new Object[]{
                dispositivo.getId(),
                dispositivo.getNomeDispositivo(),
                dispositivo.getCategoria(),
                dispositivo.getMarca(),
                dispositivo.getModelo(),
                dispositivo.getDescricao(),
                dispositivo.getClienteEmail()
            });
        }

        tabelaDispositivos.setModel(model);

        if (tabelaDispositivos.getColumnCount() > 0) {
            tabelaDispositivos.getColumnModel().getColumn(0).setMinWidth(0);
            tabelaDispositivos.getColumnModel().getColumn(0).setMaxWidth(0);
            tabelaDispositivos.getColumnModel().getColumn(0).setPreferredWidth(0);
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
                atendimento.getStatus(),
                atendimento.getFeedbackNota()
            });
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

}
