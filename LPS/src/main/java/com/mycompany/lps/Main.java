package com.mycompany.lps;

import view.TelaPrincipalView;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {

            TelaPrincipalView telaPrincipal = new TelaPrincipalView();
            telaPrincipal.setVisible(true);
        });
    }
}
