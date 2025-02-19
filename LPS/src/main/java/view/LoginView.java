package view;

import controller.LoginController;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private final JTextField edtEmail;
    private final JPasswordField edtSenha;
    private final JButton btnLogin, btnCriarConta;
    private final LoginController controller;

    public LoginView() {
        setTitle("Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel painelFundo = criarPainelFundo();
        setContentPane(painelFundo);
        painelFundo.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        edtEmail = new JTextField();
        edtSenha = new JPasswordField();

        estilizarCampoEntrada(edtEmail);
        estilizarCampoEntrada(edtSenha);

        btnLogin = new JButton("Entrar");
        btnCriarConta = new JButton("Criar Conta");
        estilizarBotao(btnLogin);
        estilizarBotao(btnCriarConta);

        adicionarCampo("Email:", edtEmail, gbc, painelFundo);
        adicionarCampo("Senha:", edtSenha, gbc, painelFundo);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        panelBotoes.setBackground(new Color(50, 50, 50));
        panelBotoes.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));
        panelBotoes.add(btnLogin);
        panelBotoes.add(btnCriarConta);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        painelFundo.add(panelBotoes, gbc);

        controller = new LoginController(this);

        btnLogin.addActionListener(e -> controller.autenticarUsuario());
        btnCriarConta.addActionListener(e -> controller.redirecionarParaCriarConta());
    }

    private void adicionarCampo(String label, JComponent campo, GridBagConstraints gbc, JPanel painel) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        estilizarLabel(lbl);
        painel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        painel.add(campo, gbc);
    }

    public String getEmail() {
        return edtEmail.getText();
    }

    public String getSenha() {
        return new String(edtSenha.getPassword());
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

        if (campo instanceof JTextField || campo instanceof JPasswordField) {
            ((JTextField) campo).setCaretColor(Color.LIGHT_GRAY);
        }
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
}
