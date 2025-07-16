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

        Color bgColor = new Color(24, 32, 48);
        Color panelColor = new Color(34, 45, 65);
        Color borderColor = new Color(44, 62, 80);

        desktop = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 32;
                int margin = 10;
                g2.setColor(panelColor);
                g2.fillRoundRect(margin, margin, getWidth() - 2 * margin, getHeight() - 2 * margin, arc, arc);
                g2.setColor(borderColor);
                g2.drawRoundRect(margin, margin, getWidth() - 2 * margin, getHeight() - 2 * margin, arc, arc);
                g2.dispose();
            }
        };
        desktop.setOpaque(false);

        JPanel painelCentralizador = new JPanel(new GridBagLayout());
        painelCentralizador.setBackground(bgColor);
        GridBagConstraints gbcCentral = new GridBagConstraints();
        gbcCentral.gridx = 0; gbcCentral.gridy = 0;
        gbcCentral.weightx = 1.0; gbcCentral.weighty = 1.0;
        gbcCentral.anchor = GridBagConstraints.CENTER;
        gbcCentral.fill = GridBagConstraints.BOTH;
        desktop.setPreferredSize(new Dimension(860, 540));
        painelCentralizador.add(desktop, gbcCentral);

        setContentPane(painelCentralizador);
        getContentPane().setBackground(bgColor);

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