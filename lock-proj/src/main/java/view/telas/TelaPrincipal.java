package view.telas;

import me.model.entidades.User;
import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {
    private JDesktopPane desktop;
    private User user;

    public TelaPrincipal(User user) {
        this.user = user;
        setTitle("Sistema de Locadora");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        desktop = new JDesktopPane();
        setContentPane(desktop);

        abrirTelaInicial();
        criarMenu();
        setVisible(true);
    }

    private void abrirTelaInicial() {
        desktop.removeAll();
        if (user.getTipo() == me.model.enums.TipoUser.ADMIN) {
            abrirJanela(new view.telas.TelaAdmin(desktop, user));
        } else {
            abrirJanela(new view.telas.TelaUser(user));
        }
    }

    public void abrirJanela(JInternalFrame tela) {
        desktop.add(tela);
        tela.setVisible(true);
        try {
            tela.setSelected(true);
        } catch (Exception ignored) {}
    }

    private void criarMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opções");

        JMenuItem itemReabrirPainel = new JMenuItem("Reabrir Painel");
        itemReabrirPainel.addActionListener(e -> {
            desktop.removeAll();
            abrirTelaInicial();
        });

        JMenuItem itemLogout = new JMenuItem("Logout");
        itemLogout.addActionListener(e -> logout());

        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));

        menu.add(itemReabrirPainel);
        menu.add(itemLogout);
        menu.add(itemSair);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void logout() {
        dispose();
        SwingUtilities.invokeLater(() -> new view.telas.TelaLogin().setVisible(true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new view.telas.TelaLogin().setVisible(true));
    }
}