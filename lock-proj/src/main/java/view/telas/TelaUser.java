package view.telas;

import me.control.GerenciadorFilmes;
import me.control.GerenciadorUsers;
import me.model.entidades.Filme;
import me.model.entidades.User;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class TelaUser extends JInternalFrame {
    private final User user;
    private final GerenciadorUsers gerenciadorUsers = new GerenciadorUsers();
    private final GerenciadorFilmes gerenciadorFilmes = new GerenciadorFilmes();

    // Filmes disponíveis
    private JTable tabelaFilmes;
    private DefaultTableModel modeloFilmes;
    private JTextField campoBuscaFilme;
    private JComboBox<String> comboGenero;
    private JComboBox<String> comboOrdenar;

    // Filmes alugados
    private JTable tabelaAlugados;
    private DefaultTableModel modeloAlugados;

    public TelaUser(User user) {
        super("Painel do Usuário", true, true, true, true);
        this.user = user;
        setSize(900, 600);
        setLocation(100, 100);
        setLayout(new BorderLayout());
        initUI();
        SwingUtilities.invokeLater(() -> {
            try {
                setMaximum(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void initUI() {
        JToolBar barraFerramentas = new JToolBar();
        barraFerramentas.setFloatable(true);

        JButton btnEditarDados = new JButton("Editar Dados");
        btnEditarDados.addActionListener(e -> editarDadosUsuario());
        barraFerramentas.add(btnEditarDados);

        add(barraFerramentas, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();

        // Painel de filmes disponíveis
        JPanel painelFilmes = new JPanel(new BorderLayout());

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        campoBuscaFilme = new JTextField(18);
        campoBuscaFilme.setToolTipText("Buscar filme por nome...");
        campoBuscaFilme.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarFilmes(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarFilmes(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarFilmes(); }
        });
        painelBusca.add(new JLabel("Buscar:"));
        painelBusca.add(campoBuscaFilme);

        comboGenero = new JComboBox<>(new String[]{"Todos", "Ação", "Comédia", "Drama", "Terror", "Romance"});
        comboGenero.addActionListener(e -> filtrarFilmes());
        painelBusca.add(new JLabel("Gênero:"));
        painelBusca.add(comboGenero);

        comboOrdenar = new JComboBox<>(new String[]{"Nome", "Preço", "Qualidade"});
        comboOrdenar.addActionListener(e -> ordenarFilmes());
        painelBusca.add(new JLabel("Ordenar por:"));
        painelBusca.add(comboOrdenar);

        // Botão de alugar mais destacado
        JButton btnAlugar = new JButton("Alugar Filme Selecionado");
        btnAlugar.setFont(btnAlugar.getFont().deriveFont(Font.BOLD, 14f));
        btnAlugar.setBackground(new Color(60, 179, 113));
        btnAlugar.setForeground(Color.WHITE);
        btnAlugar.setFocusPainted(false);
        btnAlugar.addActionListener(e -> alugarFilmeSelecionado());
        painelBusca.add(btnAlugar);

        painelFilmes.add(painelBusca, BorderLayout.NORTH);

        modeloFilmes = new DefaultTableModel(new Object[]{"Nome", "Gêneros", "Preço", "Qualidade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaFilmes = new JTable(modeloFilmes);
        tabelaFilmes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tabelaFilmes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tabelaFilmes.getSelectedRow() != -1) {
                    String nome = (String) modeloFilmes.getValueAt(tabelaFilmes.getSelectedRow(), 0);
                    Filme filme = gerenciadorFilmes.listarFilmes().stream()
                        .filter(f -> f.getNome().equals(nome)).findFirst().orElse(null);
                    if (filme != null) {
                        JInternalFrame tela = new TelaFilme(filme);
                        ((JDesktopPane) TelaUser.this.getParent()).add(tela);
                        tela.setVisible(true);
                    }
                }
            }
        });

        painelFilmes.add(new JScrollPane(tabelaFilmes), BorderLayout.CENTER);

        abas.addTab("Catálogo de Filmes", painelFilmes);

        // Painel de filmes alugados
        JPanel painelAlugados = new JPanel(new BorderLayout());
        modeloAlugados = new DefaultTableModel(new Object[]{"Nome", "Gêneros", "Preço", "Qualidade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaAlugados = new JTable(modeloAlugados);
        tabelaAlugados.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tabelaAlugados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tabelaAlugados.getSelectedRow() != -1) {
                    String nome = (String) modeloAlugados.getValueAt(tabelaAlugados.getSelectedRow(), 0);
                    Filme filme = gerenciadorUsers.listarFilmesAlugados(user).stream()
                        .filter(f -> f.getNome().equals(nome)).findFirst().orElse(null);
                    if (filme != null) {
                        JInternalFrame tela = new TelaFilme(filme);
                        SwingUtilities.getWindowAncestor(TelaUser.this).setVisible(true);
                        ((JDesktopPane) TelaUser.this.getParent()).add(tela);
                        tela.setVisible(true);
                    }
                }
            }
        });

        JButton btnDesalugar = new JButton("Devolver Filme Selecionado");
        btnDesalugar.addActionListener(e -> desalugarFilmeSelecionado());
        painelAlugados.add(new JScrollPane(tabelaAlugados), BorderLayout.CENTER);
        painelAlugados.add(btnDesalugar, BorderLayout.SOUTH);

        abas.addTab("Meus Filmes Alugados", painelAlugados);

        add(abas, BorderLayout.CENTER);

        mostrarFilmes();
        mostrarFilmesAlugados();
    }

    // Filmes disponíveis
    private void mostrarFilmes() {
        modeloFilmes.setRowCount(0);
        List<Filme> filmes = gerenciadorFilmes.listarFilmes();
        for (Filme f : filmes) {
            modeloFilmes.addRow(new Object[]{
                f.getNome(),
                f.getGeneros(),
                f.getPreco(),
                f.getQualidade()
            });
        }
    }

    private void filtrarFilmes() {
        String filtro = campoBuscaFilme.getText().trim().toLowerCase();
        String genero = (String) comboGenero.getSelectedItem();
        modeloFilmes.setRowCount(0);
        List<Filme> filmes = gerenciadorFilmes.listarFilmes();
        List<Filme> filtrados = filmes.stream()
            .filter(f -> f.getNome().toLowerCase().contains(filtro))
            .filter(f -> genero.equals("Todos") || f.getGeneros().toString().contains(genero))
            .collect(Collectors.toList());
        for (Filme f : filtrados) {
            modeloFilmes.addRow(new Object[]{
                f.getNome(),
                f.getGeneros(),
                f.getPreco(),
                f.getQualidade()
            });
        }
    }

    private void ordenarFilmes() {
        String criterio = (String) comboOrdenar.getSelectedItem();
        List<Filme> filmes = gerenciadorFilmes.listarFilmes();
        if ("Preço".equals(criterio)) {
            filmes = filmes.stream().sorted((a, b) -> a.getPreco().compareTo(b.getPreco())).collect(Collectors.toList());
        } else if ("Qualidade".equals(criterio)) {
            filmes = filmes.stream().sorted((a, b) -> a.getQualidade().compareTo(b.getQualidade())).collect(Collectors.toList());
        } else {
            filmes = filmes.stream().sorted((a, b) -> a.getNome().compareToIgnoreCase(b.getNome())).collect(Collectors.toList());
        }
        modeloFilmes.setRowCount(0);
        for (Filme f : filmes) {
            modeloFilmes.addRow(new Object[]{
                f.getNome(),
                f.getGeneros(),
                f.getPreco(),
                f.getQualidade()
            });
        }
    }

    private void alugarFilmeSelecionado() {
        int row = tabelaFilmes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um filme para alugar.");
            return;
        }
        String nome = (String) modeloFilmes.getValueAt(row, 0);
        Filme filme = gerenciadorFilmes.listarFilmes().stream()
            .filter(f -> f.getNome().equals(nome)).findFirst().orElse(null);

        // Verifica se já está alugado
        List<Filme> alugados = gerenciadorUsers.listarFilmesAlugados(user);
        boolean jaAlugado = alugados.stream().anyMatch(f -> f.getId().equals(filme.getId()));
        if (jaAlugado) {
            JOptionPane.showMessageDialog(this, "Você já alugou este filme!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (filme != null) {
            gerenciadorUsers.alugarFilme(user, filme);
            mostrarFilmesAlugados();
            JOptionPane.showMessageDialog(this, "Filme alugado com sucesso!");
        }
    }

    // Filmes alugados
    private void mostrarFilmesAlugados() {
        modeloAlugados.setRowCount(0);
        List<Filme> alugados = gerenciadorUsers.listarFilmesAlugados(user);
        for (Filme f : alugados) {
            modeloAlugados.addRow(new Object[]{
                f.getNome(),
                f.getGeneros(),
                f.getPreco(),
                f.getQualidade()
            });
        }
    }

    private void desalugarFilmeSelecionado() {
        int row = tabelaAlugados.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um filme para devolver.");
            return;
        }
        String nome = (String) modeloAlugados.getValueAt(row, 0);
        Filme filme = gerenciadorUsers.listarFilmesAlugados(user).stream()
            .filter(f -> f.getNome().equals(nome)).findFirst().orElse(null);
        if (filme != null) {
            gerenciadorUsers.desalugarFilme(user, filme);
            mostrarFilmesAlugados();
            JOptionPane.showMessageDialog(this, "Filme devolvido com sucesso!");
        }
    }

    // Editar dados do usuário
    private void editarDadosUsuario() {
        JPanel painel = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField campoEmail = new JTextField(user.getEmail());
        JTextField campoUsername = new JTextField(user.getUsername());
        JPasswordField campoSenhaAtual = new JPasswordField();
        JPasswordField campoNovaSenha = new JPasswordField();

        painel.add(new JLabel("Novo Email:"));
        painel.add(campoEmail);
        painel.add(new JLabel("Novo Username:"));
        painel.add(campoUsername);
        painel.add(new JLabel("Senha Atual:"));
        painel.add(campoSenhaAtual);
        painel.add(new JLabel("Nova Senha (opcional):"));
        painel.add(campoNovaSenha);

        int op = JOptionPane.showConfirmDialog(this, painel, "Editar Dados", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            String email = campoEmail.getText().trim();
            String username = campoUsername.getText().trim();
            String senhaAtual = new String(campoSenhaAtual.getPassword());
            String novaSenha = new String(campoNovaSenha.getPassword());

            if (!senhaAtual.equals(user.getPassword())) {
                JOptionPane.showMessageDialog(this, "Senha atual incorreta.");
                return;
            }
            user.setEmail(email);
            user.setUsername(username);
            if (!novaSenha.isEmpty()) {
                user.setPassword(novaSenha);
            }
            gerenciadorUsers.editarUser(user);
            JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
        }
    }
}
