package view.telas;

import me.model.entidades.Filme;

import javax.swing.*;
import java.awt.*;

public class TelaFilme extends JInternalFrame {
    private final Filme filme;

    private final Color bgColor = new Color(24, 32, 48);
    private final Color panelColor = new Color(34, 45, 65);
    private final Color borderColor = new Color(44, 62, 80);

    public TelaFilme(Filme filme) {
        super("Detalhes do Filme", true, true, true, true);
        this.filme = filme;
        setSize(400, 350);
        setLocation(150, 150);
        setBackground(bgColor);
        setContentPane(criarPainelEstilizado());
    }

    private JPanel criarPainelEstilizado() {
        JPanel painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panelColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 32, 32);
                g2.dispose();
            }
        };
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setOpaque(false);
        painel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel lblNome = new JLabel("Nome: " + filme.getNome());
        JLabel lblDescricao = new JLabel("<html><b>Descrição:</b> " + filme.getDescricao() + "</html>");
        JLabel lblDuracao = new JLabel("Duração: " + filme.getDuracao().toMinutes() + " min");
        JLabel lblPreco = new JLabel("Preço: R$" + filme.getPreco());
        JLabel lblQualidade = new JLabel("Qualidade: " + filme.getQualidade());
        JLabel lblGeneros = new JLabel("Gêneros: " + filme.getGeneros());

        Font fontTitulo = new Font("Segoe UI", Font.BOLD, 18);
        Font fontNormal = new Font("Segoe UI", Font.PLAIN, 15);
        Color labelColor = new Color(180, 200, 230);

        lblNome.setFont(fontTitulo);
        lblNome.setForeground(labelColor);
        lblDescricao.setFont(fontNormal);
        lblDescricao.setForeground(labelColor);
        lblDuracao.setFont(fontNormal);
        lblDuracao.setForeground(labelColor);
        lblPreco.setFont(fontNormal);
        lblPreco.setForeground(labelColor);
        lblQualidade.setFont(fontNormal);
        lblQualidade.setForeground(labelColor);
        lblGeneros.setFont(fontNormal);
        lblGeneros.setForeground(labelColor);

        painel.add(Box.createVerticalStrut(10));
        painel.add(lblNome);
        painel.add(Box.createVerticalStrut(10));
        painel.add(lblDescricao);
        painel.add(Box.createVerticalStrut(10));
        painel.add(lblDuracao);
        painel.add(Box.createVerticalStrut(5));
        painel.add(lblPreco);
        painel.add(Box.createVerticalStrut(5));
        painel.add(lblQualidade);
        painel.add(Box.createVerticalStrut(5));
        painel.add(lblGeneros);

        return painel;
    }
}
