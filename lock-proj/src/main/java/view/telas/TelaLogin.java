package view.telas;

import me.control.GerenciadorUsers;
import me.model.entidades.User;
import me.model.exceptions.UsuarioNaoEncontradoException;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {
    private JTextField campoLogin;
    private JPasswordField campoSenha;

    public TelaLogin() {
        setTitle("Login - Sistema de Locadora");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(420, 260));
        setLocationRelativeTo(null);

        Color bgColor = new Color(24, 32, 48);
        Color panelColor = new Color(34, 45, 65);
        Color borderColor = new Color(44, 62, 80);
        Color labelColor = new Color(180, 200, 230);
        Color btnColor = new Color(60, 179, 113);
        Color btnHover = new Color(80, 200, 140);

        class RoundedBorder extends javax.swing.border.AbstractBorder {
            private final int radius;
            private final Color color;
            public RoundedBorder(int radius, Color color) {
                this.radius = radius;
                this.color = color;
            }
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
                g2.dispose();
            }
            @Override
            public Insets getBorderInsets(Component c) { return new Insets(8, 12, 8, 12); }
            @Override
            public Insets getBorderInsets(Component c, Insets insets) {
                insets.left = insets.right = 12;
                insets.top = insets.bottom = 8;
                return insets;
            }
        }

        campoLogin = new JTextField();
        campoLogin.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campoLogin.setBackground(panelColor);
        campoLogin.setForeground(labelColor);
        campoLogin.setCaretColor(labelColor);
        campoLogin.setBorder(new RoundedBorder(18, borderColor));

        campoSenha = new JPasswordField();
        campoSenha.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campoSenha.setBackground(panelColor);
        campoSenha.setForeground(labelColor);
        campoSenha.setCaretColor(labelColor);
        campoSenha.setBorder(new RoundedBorder(18, borderColor));

        JLabel lblLogin = new JLabel("Usuário ou Email:");
        JLabel lblSenha = new JLabel("Senha:");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblLogin.setForeground(labelColor);
        lblSenha.setForeground(labelColor);

        JButton btnEntrar = new JButton("Entrar") {
            @Override
            protected void paintComponent(Graphics g) {
                if (isContentAreaFilled()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        btnEntrar.setBackground(btnColor);
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorder(new RoundedBorder(22, btnColor));
        btnEntrar.setContentAreaFilled(false);
        btnEntrar.setOpaque(false);
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEntrar.addActionListener(e -> autenticar());
        btnEntrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEntrar.setBackground(btnHover);
                btnEntrar.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEntrar.setBackground(btnColor);
                btnEntrar.repaint();
            }
        });

        JPanel painelCampos = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 28;
                int margin = 8;
                g2.setColor(panelColor);
                g2.fillRoundRect(margin, margin, getWidth() - 2 * margin, getHeight() - 2 * margin, arc, arc);
                g2.setColor(borderColor);
                g2.drawRoundRect(margin, margin, getWidth() - 2 * margin, getHeight() - 2 * margin, arc, arc);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        painelCampos.setOpaque(false);
        painelCampos.setPreferredSize(new Dimension(440, 220));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3; gbc.anchor = GridBagConstraints.EAST;
        painelCampos.add(lblLogin, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        campoLogin.setPreferredSize(new Dimension(220, 36));
        painelCampos.add(campoLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        painelCampos.add(lblSenha, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        campoSenha.setPreferredSize(new Dimension(220, 36));
        painelCampos.add(campoSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.weighty = 0.1; gbc.anchor = GridBagConstraints.CENTER;
        painelCampos.add(btnEntrar, gbc);

        JPanel painelCentralizador = new JPanel(new GridBagLayout());
        painelCentralizador.setBackground(bgColor);
        GridBagConstraints gbcCentral = new GridBagConstraints();
        gbcCentral.gridx = 0; gbcCentral.gridy = 0;
        gbcCentral.weightx = 1.0; gbcCentral.weighty = 1.0;
        gbcCentral.anchor = GridBagConstraints.CENTER;
        gbcCentral.fill = GridBagConstraints.NONE;
        painelCentralizador.add(painelCampos, gbcCentral);

        setContentPane(painelCentralizador);
        getContentPane().setBackground(bgColor);
        pack();
        setMinimumSize(new Dimension(420, 260));
        setSize(480, 320);
    }

    private void autenticar() {
        String login = campoLogin.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            GerenciadorUsers ger = new GerenciadorUsers();
            User user = ger.autenticar(login, senha);
            JOptionPane.showMessageDialog(this, "Login realizado com sucesso!", "Bem-vindo", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.invokeLater(() -> {
                new TelaPrincipal(user).setVisible(true);
                dispose();
            });
        } catch (UsuarioNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}
