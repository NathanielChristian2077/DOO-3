package view.telas;

import me.model.entidades.Filme;

import javax.swing.*;
import java.awt.*;

public class TelaFilme extends JInternalFrame {
    private final Filme filme;

    public TelaFilme(Filme filme) {
        super("Detalhes do Filme", true, true, true, true);
        this.filme = filme;
        setSize(400, 350);
        setLocation(150, 150);
        initUI();
    }

    private void initUI() {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        JLabel lblNome = new JLabel("Nome: " + filme.getNome());
        JLabel lblDescricao = new JLabel("<html><b>Descrição:</b> " + filme.getDescricao() + "</html>");
        JLabel lblDuracao = new JLabel("Duração: " + filme.getDuracao().toMinutes() + " min");
        JLabel lblPreco = new JLabel("Preço: R$" + filme.getPreco());
        JLabel lblQualidade = new JLabel("Qualidade: " + filme.getQualidade());
        JLabel lblGeneros = new JLabel("Gêneros: " + filme.getGeneros());

        lblNome.setFont(new Font("Arial", Font.BOLD, 16));
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 13));

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

        add(painel);
    }
}
