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

public class TelaAdmin extends JInternalFrame {
    private final GerenciadorUsers gerenciadorUsers = new GerenciadorUsers();
    private final GerenciadorFilmes gerenciadorFilmes = new GerenciadorFilmes();
    private final User adminUser;

    // Usuários
    private JTable tabelaUsuarios;
    private DefaultTableModel modeloUsuarios;
    private JTextField campoBuscaUsuario;

    // Filmes
    private JTable tabelaFilmes;
    private DefaultTableModel modeloFilmes;
    private JTextField campoBuscaFilme;

    public TelaAdmin(JDesktopPane desktop, User adminUser) {
        super("Painel do Administrador", true, true, true, true);
        this.adminUser = adminUser;
        setSize(1000, 700);
        setLocation(50, 50);
        setLayout(new BorderLayout());
        initUI();
        SwingUtilities.invokeLater(() -> {
            try { setMaximum(true); } catch (Exception ex) { ex.printStackTrace(); }
        });
    }

    private void initUI() {
        JToolBar barraFerramentas = new JToolBar();
        barraFerramentas.setFloatable(true);

        JButton btnUsuarios = new JButton("Usuários");
        btnUsuarios.addActionListener(e -> mostrarUsuarios());
        barraFerramentas.add(btnUsuarios);

        JButton btnAddUsuario = new JButton("Adicionar Usuário");
        btnAddUsuario.addActionListener(e -> adicionarUsuario());
        barraFerramentas.add(btnAddUsuario);

        JButton btnDeleteUsuario = new JButton("Deletar Usuário");
        btnDeleteUsuario.addActionListener(e -> deletarUsuario());
        barraFerramentas.add(btnDeleteUsuario);

        JButton btnFilmesAlugados = new JButton("Filmes Alugados");
        btnFilmesAlugados.addActionListener(e -> visualizarFilmesAlugados());
        barraFerramentas.add(btnFilmesAlugados);

        barraFerramentas.addSeparator();
        JButton btnFilmes = new JButton("Filmes");
        btnFilmes.addActionListener(e -> mostrarFilmes());
        barraFerramentas.add(btnFilmes);

        JButton btnAddFilme = new JButton("Adicionar Filme");
        btnAddFilme.addActionListener(e -> adicionarFilme());
        barraFerramentas.add(btnAddFilme);

        JButton btnEditFilme = new JButton("Editar Filme");
        btnEditFilme.addActionListener(e -> editarFilme());
        barraFerramentas.add(btnEditFilme);

        JButton btnDeleteFilme = new JButton("Deletar Filme");
        btnDeleteFilme.addActionListener(e -> deletarFilme());
        barraFerramentas.add(btnDeleteFilme);

        add(barraFerramentas, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();

        // Usuários
        JPanel painelUsuarios = new JPanel(new BorderLayout());
        campoBuscaUsuario = new JTextField();
        campoBuscaUsuario.setToolTipText("Buscar usuário...");
        campoBuscaUsuario.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarUsuarios(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarUsuarios(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarUsuarios(); }
        });
        painelUsuarios.add(campoBuscaUsuario, BorderLayout.NORTH);

        modeloUsuarios = new DefaultTableModel(new Object[]{"ID", "Username", "Email", "Tipo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaUsuarios = new JTable(modeloUsuarios);
        tabelaUsuarios.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        painelUsuarios.add(new JScrollPane(tabelaUsuarios), BorderLayout.CENTER);

        abas.addTab("Usuários", painelUsuarios);

        // Filmes
        JPanel painelFilmes = new JPanel(new BorderLayout());
        campoBuscaFilme = new JTextField();
        campoBuscaFilme.setToolTipText("Buscar filme...");
        campoBuscaFilme.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarFilmes(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarFilmes(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarFilmes(); }
        });
        painelFilmes.add(campoBuscaFilme, BorderLayout.NORTH);

        modeloFilmes = new DefaultTableModel(new Object[]{"ID", "Nome", "Gêneros", "Preço", "Qualidade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaFilmes = new JTable(modeloFilmes);
        tabelaFilmes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Duplo clique para abrir detalhes do filme
        tabelaFilmes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tabelaFilmes.getSelectedRow() != -1) {
                    String id = (String) modeloFilmes.getValueAt(tabelaFilmes.getSelectedRow(), 0);
                    Filme filme = gerenciadorFilmes.listarFilmes().stream()
                        .filter(f -> f.getId().equals(id)).findFirst().orElse(null);
                    if (filme != null) {
                        JInternalFrame tela = new TelaFilme(filme);
                        ((JDesktopPane) TelaAdmin.this.getParent()).add(tela);
                        tela.setVisible(true);
                    }
                }
            }
        });

        painelFilmes.add(new JScrollPane(tabelaFilmes), BorderLayout.CENTER);

        abas.addTab("Filmes", painelFilmes);

        add(abas, BorderLayout.CENTER);

        mostrarUsuarios();
        mostrarFilmes();
    }

    // Usuários
    private void mostrarUsuarios() {
        modeloUsuarios.setRowCount(0);
        List<User> users = gerenciadorUsers.listarUsuarios(adminUser);
        for (User u : users) {
            modeloUsuarios.addRow(new Object[]{
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getTipo() != null ? u.getTipo().name() : ""
            });
        }
    }

    private void filtrarUsuarios() {
        String filtro = campoBuscaUsuario.getText().trim().toLowerCase();
        modeloUsuarios.setRowCount(0);
        List<User> users = gerenciadorUsers.listarUsuarios(adminUser);
        List<User> filtrados = users.stream()
            .filter(u -> u.getUsername().toLowerCase().contains(filtro) ||
                         u.getEmail().toLowerCase().contains(filtro))
            .collect(Collectors.toList());
        for (User u : filtrados) {
            modeloUsuarios.addRow(new Object[]{
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getTipo() != null ? u.getTipo().name() : ""
            });
        }
    }

    private void adicionarUsuario() {
        JTextField campoEmail = new JTextField();
        JTextField campoUsername = new JTextField();
        JPasswordField campoSenha = new JPasswordField();
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"ADMIN", "COMUM"});
        JPanel painel = new JPanel(new GridLayout(0, 2, 8, 8));
        painel.add(new JLabel("Email:"));
        painel.add(campoEmail);
        painel.add(new JLabel("Username:"));
        painel.add(campoUsername);
        painel.add(new JLabel("Senha:"));
        painel.add(campoSenha);
        painel.add(new JLabel("Tipo:"));
        painel.add(comboTipo);

        int op = JOptionPane.showConfirmDialog(this, painel, "Adicionar Usuário", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            try {
                gerenciadorUsers.cadastrarUserAsADM(adminUser, campoEmail.getText(), campoUsername.getText(),
                    new String(campoSenha.getPassword()),
                    me.model.enums.TipoUser.valueOf((String) comboTipo.getSelectedItem()));
                mostrarUsuarios();
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deletarUsuario() {
        int row = tabelaUsuarios.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para deletar.");
            return;
        }
        String id = (String) modeloUsuarios.getValueAt(row, 0);
        User user = gerenciadorUsers.buscarPorId(id);
        int op = JOptionPane.showConfirmDialog(this, "Confirma a exclusão do usuário?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            gerenciadorUsers.deletarConta(user);
            mostrarUsuarios();
            JOptionPane.showMessageDialog(this, "Usuário deletado.");
        }
    }

    private void visualizarFilmesAlugados() {
        int row = tabelaUsuarios.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário.");
            return;
        }
        String id = (String) modeloUsuarios.getValueAt(row, 0);
        User user = gerenciadorUsers.buscarPorId(id);
        List<Filme> filmes = gerenciadorUsers.listarFilmesAlugados(user);
        StringBuilder sb = new StringBuilder("Filmes alugados:\n");
        for (Filme f : filmes) {
            sb.append(f.getNome()).append(" (").append(f.getQualidade()).append(")\n");
        }
        JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "Nenhum filme alugado.");
    }

    // Filmes
    private void mostrarFilmes() {
        modeloFilmes.setRowCount(0);
        List<Filme> filmes = gerenciadorFilmes.listarFilmes();
        for (Filme f : filmes) {
            modeloFilmes.addRow(new Object[]{
                f.getId(),
                f.getNome(),
                f.getGeneros(),
                f.getPreco(),
                f.getQualidade()
            });
        }
    }

    private void filtrarFilmes() {
        String filtro = campoBuscaFilme.getText().trim().toLowerCase();
        modeloFilmes.setRowCount(0);
        List<Filme> filmes = gerenciadorFilmes.listarFilmes();
        List<Filme> filtrados = filmes.stream()
            .filter(f -> f.getNome().toLowerCase().contains(filtro) ||
                         f.getGeneros().toString().toLowerCase().contains(filtro))
            .collect(Collectors.toList());
        for (Filme f : filtrados) {
            modeloFilmes.addRow(new Object[]{
                f.getId(),
                f.getNome(),
                f.getGeneros(),
                f.getPreco(),
                f.getQualidade()
            });
        }
    }

    private void adicionarFilme() {
        // Campos
        JTextField campoNome = new JTextField();
        JTextArea campoDescricao = new JTextArea(3, 20);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        JTextField campoDuracao = new JTextField(); // minutos
        JTextField campoPreco = new JTextField();

        // ComboBox de qualidade com nomes amigáveis
        me.model.enums.Qualidade[] qualidades = me.model.enums.Qualidade.values();
        JComboBox<String> comboQualidade = new JComboBox<>();
        for (me.model.enums.Qualidade q : qualidades) {
            comboQualidade.addItem(q.toString());
        }

        // JList de gêneros com múltipla seleção
        me.model.enums.Genero[] generos = me.model.enums.Genero.values();
        DefaultListModel<String> modeloGeneros = new DefaultListModel<>();
        for (me.model.enums.Genero g : generos) {
            modeloGeneros.addElement(g.toString());
        }
        JList<String> listaGeneros = new JList<>(modeloGeneros);
        listaGeneros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaGeneros.setVisibleRowCount(Math.min(generos.length, 6));
        JScrollPane scrollGeneros = new JScrollPane(listaGeneros);

        // Painel responsivo
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        painel.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        painel.add(scrollDescricao, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(new JLabel("Duração (min):"), gbc);
        gbc.gridx = 1;
        painel.add(campoDuracao, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(new JLabel("Preço:"), gbc);
        gbc.gridx = 1;
        painel.add(campoPreco, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(new JLabel("Qualidade:"), gbc);
        gbc.gridx = 1;
        painel.add(comboQualidade, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(new JLabel("Gêneros:"), gbc);
        gbc.gridx = 1;
        painel.add(scrollGeneros, gbc);

        // Diálogo modal com tamanho fixo
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adicionar Filme", true);
        dialog.setContentPane(painel);
        dialog.setSize(420, 420);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        int op = JOptionPane.showConfirmDialog(dialog, painel, "Adicionar Filme", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (op == JOptionPane.OK_OPTION) {
            try {
                String nome = campoNome.getText().trim();
                String descricao = campoDescricao.getText().trim();
                int duracaoMin = Integer.parseInt(campoDuracao.getText().trim());
                java.time.Duration duracao = java.time.Duration.ofMinutes(duracaoMin);
                double preco = Double.parseDouble(campoPreco.getText().trim());
                me.model.enums.Qualidade qualidade = qualidades[comboQualidade.getSelectedIndex()];
                List<me.model.enums.Genero> generosSelecionados = listaGeneros.getSelectedValuesList().stream()
                    .map(s -> me.model.enums.Genero.valueOf(s))
                    .collect(Collectors.toList());

                Filme novo = new Filme(nome, descricao, duracao, java.math.BigDecimal.valueOf(preco), qualidade, new java.util.HashSet<>(generosSelecionados));
                gerenciadorFilmes.cadastrarFilme(adminUser, novo);
                mostrarFilmes();
                JOptionPane.showMessageDialog(this, "Filme cadastrado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar filme: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        dialog.dispose();
    }

    private void editarFilme() {
        int row = tabelaFilmes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um filme para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) modeloFilmes.getValueAt(row, 0);
        Filme filme = gerenciadorFilmes.listarFilmes().stream()
            .filter(f -> f.getId().equals(id)).findFirst().orElse(null);
        if (filme == null) {
            JOptionPane.showMessageDialog(this, "Filme não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField campoNome = new JTextField(filme.getNome());
        JTextArea campoDescricao = new JTextArea(filme.getDescricao(), 3, 20);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        JTextField campoDuracao = new JTextField(String.valueOf(filme.getDuracao().toMinutes()));
        JTextField campoPreco = new JTextField(String.valueOf(filme.getPreco()));

        me.model.enums.Qualidade[] qualidades = me.model.enums.Qualidade.values();
        JComboBox<String> comboQualidade = new JComboBox<>();
        for (me.model.enums.Qualidade q : qualidades) {
            comboQualidade.addItem(q.toString());
        }
        comboQualidade.setSelectedIndex(filme.getQualidade().ordinal());

        me.model.enums.Genero[] generos = me.model.enums.Genero.values();
        DefaultListModel<String> modeloGeneros = new DefaultListModel<>();
        for (me.model.enums.Genero g : generos) {
            modeloGeneros.addElement(g.toString());
        }
        JList<String> listaGeneros = new JList<>(modeloGeneros);
        listaGeneros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaGeneros.setVisibleRowCount(Math.min(generos.length, 6));
        JScrollPane scrollGeneros = new JScrollPane(listaGeneros);

        // Seleciona os gêneros já presentes
        List<String> generosFilme = filme.getGeneros().stream().map(Object::toString).collect(Collectors.toList());
        int[] indices = generosFilme.stream()
            .mapToInt(s -> modeloGeneros.indexOf(s))
            .filter(i -> i >= 0)
            .toArray();
        listaGeneros.setSelectedIndices(indices);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        painel.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        painel.add(scrollDescricao, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(new JLabel("Duração (min):"), gbc);
        gbc.gridx = 1;
        painel.add(campoDuracao, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(new JLabel("Preço:"), gbc);
        gbc.gridx = 1;
        painel.add(campoPreco, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(new JLabel("Qualidade:"), gbc);
        gbc.gridx = 1;
        painel.add(comboQualidade, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(new JLabel("Gêneros:"), gbc);
        gbc.gridx = 1;
        painel.add(scrollGeneros, gbc);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Filme", true);
        dialog.setContentPane(painel);
        dialog.setSize(420, 420);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        int op = JOptionPane.showConfirmDialog(dialog, painel, "Editar Filme", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (op == JOptionPane.OK_OPTION) {
            try {
                String nome = campoNome.getText().trim();
                String descricao = campoDescricao.getText().trim();
                int duracaoMin = Integer.parseInt(campoDuracao.getText().trim());
                java.time.Duration duracao = java.time.Duration.ofMinutes(duracaoMin);
                double preco = Double.parseDouble(campoPreco.getText().trim());
                me.model.enums.Qualidade qualidade = qualidades[comboQualidade.getSelectedIndex()];
                List<me.model.enums.Genero> generosSelecionados = listaGeneros.getSelectedValuesList().stream()
                    .map(s -> me.model.enums.Genero.valueOf(s))
                    .collect(Collectors.toList());

                Filme editado = new Filme(filme.getId(), nome, descricao, duracao, java.math.BigDecimal.valueOf(preco), qualidade, new java.util.HashSet<>(generosSelecionados));
                gerenciadorFilmes.editarFilme(adminUser, editado);
                mostrarFilmes();
                JOptionPane.showMessageDialog(this, "Filme editado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao editar filme: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        dialog.dispose();
    }

    private void deletarFilme() {
        int row = tabelaFilmes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um filme para deletar.");
            return;
        }
        String id = (String) modeloFilmes.getValueAt(row, 0);
        Filme filme = gerenciadorFilmes.listarFilmes().stream()
            .filter(f -> f.getId().equals(id)).findFirst().orElse(null);
        if (filme == null) return;
        int op = JOptionPane.showConfirmDialog(this, "Confirma a exclusão do filme?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            gerenciadorFilmes.deletarFilme(adminUser, filme);
            mostrarFilmes();
            JOptionPane.showMessageDialog(this, "Filme deletado.");
        }
    }
}
