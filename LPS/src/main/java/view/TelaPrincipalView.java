package view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipalView extends JFrame {

    public TelaPrincipalView() {
        setTitle("NeoTech - Tela Inicial");
        setSize(700, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelFundo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                ImageIcon iconeImagem = new ImageIcon(getClass().getResource("/images/img2.png"));
                Image imagem = iconeImagem.getImage();
                g.drawImage(imagem, 0, 0, getWidth(), getHeight(), this);
            }
        };
        painelFundo.setLayout(new BoxLayout(painelFundo, BoxLayout.Y_AXIS));

        int espacoAcimaTexto = 30;
        int espacoEntreTextoEBotoes = 190;
        int espacoEntreBotoes = 30;

        JPanel painelTexto = new JPanel();
        painelTexto.setBackground(new Color(0, 0, 0, 150));
        painelTexto.setLayout(new FlowLayout(FlowLayout.CENTER));
        painelTexto.setMaximumSize(new Dimension(250, 60));

        JLabel lblBemVindo = new JLabel("NeoTech");
        lblBemVindo.setFont(new Font("Serif", Font.BOLD, 40));
        lblBemVindo.setForeground(Color.WHITE);

        painelTexto.add(lblBemVindo);

        painelFundo.add(Box.createVerticalStrut(espacoAcimaTexto));
        painelFundo.add(painelTexto);
        painelFundo.add(Box.createVerticalStrut(espacoEntreTextoEBotoes));

        JButton btnIniciar = criarBotao("Iniciar");
        btnIniciar.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });
        painelFundo.add(btnIniciar);

        painelFundo.add(Box.createVerticalStrut(espacoEntreBotoes));

        JButton btnSair = criarBotao("Sair");
        btnSair.addActionListener(e -> System.exit(0));
        painelFundo.add(btnSair);

        add(painelFundo);
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setPreferredSize(new Dimension(150, 40));
        botao.setContentAreaFilled(false);
        botao.setFont(new Font("Serif", Font.CENTER_BASELINE, 30));
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setForeground(Color.WHITE);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return botao;
    }
}
