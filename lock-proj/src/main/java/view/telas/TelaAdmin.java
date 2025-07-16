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

    private JTable tabelaUsuarios;
    private DefaultTableModel modeloUsuarios;
    private JTextField campoBuscaUsuario;

    private JTable tabelaFilmes;
    private DefaultTableModel modeloFilmes;
    private JTextField campoBuscaFilme;

    Color transparentBorder = new Color(255, 255, 255, 40);

    public TelaAdmin(JDesktopPane desktop, User adminUser) {
        super("Painel do Administrador", true, true, true, true);
        this.adminUser = adminUser;
        setSize(1000, 700);
        setLocation(50, 50);
        setLayout(new BorderLayout());

        Color bgColor = new Color(24, 32, 48);
        Color panelColor = new Color(34, 45, 65);
        Color borderColor = new Color(44, 62, 80);
        Color labelColor = new Color(180, 200, 230);
        Color userBtnColor = new Color(255, 200, 120, 180);
        Color userBtnHover = new Color(255, 220, 170, 200);
        Color userBtnBorder = new Color(255, 180, 80, 120);

        Color movieBtnColor = new Color(0, 180, 220);
        Color movieBtnHover = new Color(80, 220, 255);
        Color movieBtnBorder = new Color(0, 120, 180, 180);

        JPanel painelCentral = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int arc = 32;
                int margin = 10;
                g2.setColor(panelColor);
                g2.fillRoundRect(margin, margin, getWidth() - 2 * margin, getHeight() - 2 * margin, arc, arc);
                g2.setColor(borderColor);
                g2.drawRoundRect(margin, margin, getWidth() - 2 * margin, getHeight() - 2 * margin, arc, arc);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        painelCentral.setOpaque(false);
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.setOpaque(false);

        JToolBar barraFerramentas = new JToolBar();
        barraFerramentas.setFloatable(false);
        barraFerramentas.setBackground(panelColor);

        JLabel lblUsuarios = new JLabel("Usuários");
        lblUsuarios.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblUsuarios.setForeground(Color.WHITE);
        lblUsuarios.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        barraFerramentas.add(lblUsuarios);
        barraFerramentas.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton btnAddUsuario = criarBotaoPreenchido("Adicionar Usuário", userBtnColor, userBtnHover, Color.WHITE, userBtnBorder);
        btnAddUsuario.addActionListener(e -> adicionarUsuario());
        barraFerramentas.add(btnAddUsuario);
        barraFerramentas.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton btnDeleteUsuario = criarBotaoPreenchido("Deletar Usuário", userBtnColor, userBtnHover, Color.WHITE, userBtnBorder);
        btnDeleteUsuario.addActionListener(e -> deletarUsuario());
        barraFerramentas.add(btnDeleteUsuario);
        barraFerramentas.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton btnFilmesAlugados = criarBotaoPreenchido("Filmes Alugados", userBtnColor, userBtnHover, Color.WHITE, userBtnBorder);
        btnFilmesAlugados.addActionListener(e -> visualizarFilmesAlugados());
        barraFerramentas.add(btnFilmesAlugados);
        barraFerramentas.add(Box.createRigidArea(new Dimension(20, 0)));

        JLabel lblFilmes = new JLabel("Filmes");
        lblFilmes.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFilmes.setForeground(Color.WHITE);
        lblFilmes.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        barraFerramentas.add(lblFilmes);
        barraFerramentas.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton btnAddFilme = criarBotaoPreenchido("Adicionar Filme", movieBtnColor, movieBtnHover, Color.WHITE, movieBtnBorder);
        btnAddFilme.addActionListener(e -> adicionarFilme());
        barraFerramentas.add(btnAddFilme);
        barraFerramentas.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton btnEditFilme = criarBotaoPreenchido("Editar Filme", movieBtnColor, movieBtnHover, Color.WHITE, movieBtnBorder);
        btnEditFilme.addActionListener(e -> editarFilme());
        barraFerramentas.add(btnEditFilme);
        barraFerramentas.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton btnDeleteFilme = criarBotaoPreenchido("Deletar Filme", movieBtnColor, movieBtnHover, Color.WHITE, movieBtnBorder);
        btnDeleteFilme.addActionListener(e -> deletarFilme());
        barraFerramentas.add(btnDeleteFilme);

        painelConteudo.add(barraFerramentas, BorderLayout.NORTH);

        JTabbedPane abas = new JTabbedPane();
        abas.setBackground(panelColor);
        abas.setForeground(labelColor);
        abas.setBorder(BorderFactory.createEmptyBorder());
        abas.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                // Não preenche o fundo da aba selecionada, só desenha a borda
                if (isSelected) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(borderColor);
                    g2.setStroke(new BasicStroke(3f));
                    g2.drawRoundRect(x + 2, y + 2, w - 5, h - 5, 18, 18);
                    g2.dispose();
                }
            }
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                // Não desenha borda
            }
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                // Remove borda padrão
            }
        });

        JPanel painelUsuarios = new JPanel(new BorderLayout());
        painelUsuarios.setOpaque(false);
        campoBuscaUsuario = criarCampoBusca(panelColor, userBtnColor, "Buscar...");
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
        estilizarTabela(tabelaUsuarios, panelColor, labelColor, borderColor);

        JScrollPane scrollUsuarios = new JScrollPane(tabelaUsuarios);
        scrollUsuarios.getViewport().setBackground(panelColor);
        scrollUsuarios.setBackground(panelColor);
        scrollUsuarios.setOpaque(true);
        scrollUsuarios.setBorder(BorderFactory.createEmptyBorder());
        tabelaUsuarios.setOpaque(false);
        scrollUsuarios.getViewport().setOpaque(true);
        painelUsuarios.add(scrollUsuarios, BorderLayout.CENTER);

        abas.addTab("Usuários", painelUsuarios);

        JPanel painelFilmes = new JPanel(new BorderLayout());
        painelFilmes.setOpaque(false);
        campoBuscaFilme = criarCampoBusca(panelColor, movieBtnColor, "Buscar...");
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
        tabelaFilmes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tabelaFilmes.getSelectedRow() != -1) {
                    int row = tabelaFilmes.getSelectedRow();
                    String nomeFilme = (String) modeloFilmes.getValueAt(row, 1); // Coluna "Nome"
                    // Busca o filme pelo nome (ajuste se houver filmes com nomes repetidos, use ID se possível)
                    Filme filme = gerenciadorFilmes.listarFilmes().stream()
                        .filter(f -> f.getNome().equals(nomeFilme))
                        .findFirst()
                        .orElse(null);
                    if (filme != null) {
                        // Abre a tela de detalhes do filme
                        TelaFilme telaFilme = new TelaFilme(filme);
                        JDesktopPane desktop = (JDesktopPane) SwingUtilities.getAncestorOfClass(JDesktopPane.class, TelaAdmin.this);
                        if (desktop != null) {
                            desktop.add(telaFilme);
                            telaFilme.setVisible(true);
                            try {
                                telaFilme.setSelected(true);
                            } catch (java.beans.PropertyVetoException ex) {
                                // Ignora
                            }
                        } else {
                            // Fallback: mostra em um JDialog se não estiver em um JDesktopPane
                            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(TelaAdmin.this), "Detalhes do Filme", true);
                            dialog.setContentPane(new TelaFilme(filme).getContentPane());
                            dialog.setSize(420, 350);
                            dialog.setLocationRelativeTo(TelaAdmin.this);
                            dialog.setVisible(true);
                        }
                    }
                }
            }
        });
        estilizarTabela(tabelaFilmes, panelColor, labelColor, borderColor);

        JScrollPane scrollFilmes = new JScrollPane(tabelaFilmes);
        scrollFilmes.getViewport().setBackground(panelColor);
        scrollFilmes.setBackground(panelColor);
        scrollFilmes.setOpaque(true);
        scrollFilmes.setBorder(BorderFactory.createEmptyBorder());
        tabelaFilmes.setOpaque(false);
        scrollFilmes.getViewport().setOpaque(true);
        painelFilmes.add(scrollFilmes, BorderLayout.CENTER);

        abas.addTab("Filmes", painelFilmes);

        painelConteudo.add(abas, BorderLayout.CENTER);

        painelCentral.add(painelConteudo, BorderLayout.CENTER);

        JPanel painelCentralizador = new JPanel(new GridBagLayout());
        painelCentralizador.setBackground(bgColor);
        GridBagConstraints gbcCentral = new GridBagConstraints();
        gbcCentral.gridx = 0; gbcCentral.gridy = 0;
        gbcCentral.weightx = 1.0; gbcCentral.weighty = 1.0;
        gbcCentral.anchor = GridBagConstraints.CENTER;
        gbcCentral.fill = GridBagConstraints.BOTH;
        painelCentral.setPreferredSize(new Dimension(900, 600));
        painelCentralizador.add(painelCentral, gbcCentral);

        setContentPane(painelCentralizador);

        SwingUtilities.invokeLater(() -> {
            try {
                setMaximum(true);
            } catch (Exception ignored) {}
            setVisible(true);
        });

        mostrarUsuarios();
        mostrarFilmes();
    }

    private JButton criarBotaoPreenchido(String texto, Color bg, Color hover, Color fg, Color border) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.setColor(border);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(hover);
                btn.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
                btn.repaint();
            }
        });
        return btn;
    }

    private void estilizarTabela(JTable tabela, Color bg, Color fg, Color border) {
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.setBackground(bg);
        tabela.setForeground(fg);
        tabela.setSelectionBackground(new Color(60, 179, 113, 180));
        tabela.setSelectionForeground(Color.WHITE);
        tabela.setGridColor(new Color(0,0,0,0));
        tabela.setRowHeight(28);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(border);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createEmptyBorder());

        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(fg);
                if (isSelected) {
                    c.setBackground(new Color(60, 179, 113, 180));
                } else {
                    c.setBackground(row % 2 == 0 ? bg : new Color(28, 38, 58));
                }
                return c;
            }
        });

        JScrollPane scroll = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, tabela);
        if (scroll != null) {
            scroll.getViewport().setBackground(bg);
            scroll.setBackground(bg);
            scroll.setBorder(BorderFactory.createEmptyBorder());
        }

        tabela.setOpaque(false);
        tabela.setFillsViewportHeight(true);
        if (tabela.getParent() != null) {
            tabela.getParent().setBackground(bg);
        }
    }

    private void mostrarUsuarios() {
        modeloUsuarios.setRowCount(0);
        List<User> users = gerenciadorUsers.listarUsuarios(adminUser);
        for (User u : users) {
            modeloUsuarios.addRow(new Object[]{
                u.getId(), u.getUsername(), u.getEmail(), u.getTipo()
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
                u.getId(), u.getUsername(), u.getEmail(), u.getTipo()
            });
        }
    }

    private JLabel criarLabelBranca(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        return lbl;
    }

    private JTextField criarCampoDialog(Color bg, Color fg, Color border) {
        JTextField campo = new JTextField();
        campo.setBackground(bg);
        campo.setForeground(fg);
        campo.setCaretColor(fg);
        campo.setBorder(BorderFactory.createLineBorder(new Color(200, 220, 255), 2, false));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setOpaque(true);
        return campo;
    }

    private JPanel criarPainelDialog() {
        JPanel painel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(34, 45, 65));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(200, 220, 255)); // borda clara
                g2.setStroke(new BasicStroke(3f));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        painel.setOpaque(false);
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        return painel;
    }

    private JPanel criarPainelDialogExterno(JPanel painelConteudo, JPanel painelBotoes) {
        JPanel painelDialog = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(34, 45, 65)); // fundo escuro
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(200, 220, 255)); // borda clara
                g2.setStroke(new BasicStroke(3f));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        painelDialog.setOpaque(false);
        painelDialog.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        painelDialog.add(painelConteudo, BorderLayout.CENTER);
        if (painelBotoes != null) painelDialog.add(painelBotoes, BorderLayout.SOUTH);
        return painelDialog;
    }

    private void adicionarUsuario() {
        Color panelColor = new Color(34, 45, 65);
        Color borderColor = new Color(44, 62, 80);
        Color labelColor = new Color(180, 200, 230);
        Color btnColor = new Color(0, 180, 220);
        Color btnHover = new Color(80, 220, 255);
        Color btnBorder = new Color(0, 120, 180, 180);

        JTextField campoEmail = criarCampoDialog(panelColor, labelColor, borderColor);
        JTextField campoUsername = criarCampoDialog(panelColor, labelColor, borderColor);
        JPasswordField campoSenha = new JPasswordField();
        campoSenha.setBackground(panelColor);
        campoSenha.setForeground(labelColor);
        campoSenha.setCaretColor(labelColor);
        campoSenha.setBorder(BorderFactory.createLineBorder(borderColor, 2, true));
        campoSenha.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campoSenha.setOpaque(true);

        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"ADMIN", "COMUM"});
        comboTipo.setBackground(panelColor);
        comboTipo.setForeground(labelColor);
        comboTipo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        comboTipo.setBorder(BorderFactory.createLineBorder(borderColor, 2, true));

        JPanel painel = criarPainelDialog();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(criarLabelBranca("Email:"), gbc);
        gbc.gridx = 1;
        painel.add(campoEmail, gbc);
        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Username:"), gbc);
        gbc.gridx = 1;
        painel.add(campoUsername, gbc);
        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Senha:"), gbc);
        gbc.gridx = 1;
        painel.add(campoSenha, gbc);
        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Tipo:"), gbc);
        gbc.gridx = 1;
        painel.add(comboTipo, gbc);

        JButton btnOk = criarBotaoPreenchido("OK", btnColor, btnHover, Color.WHITE, btnBorder);
        JButton btnCancel = criarBotaoPreenchido("Cancelar", btnColor, btnHover, Color.WHITE, btnBorder);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.add(btnOk);
        painelBotoes.add(Box.createRigidArea(new Dimension(10, 0)));
        painelBotoes.add(btnCancel);

        JPanel painelDialog = criarPainelDialogExterno(painel, painelBotoes);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adicionar Usuário", true);
        dialog.setUndecorated(true); 
        dialog.setContentPane(painelDialog);
        dialog.setSize(420, 300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        btnOk.addActionListener(e -> {
            try {
                gerenciadorUsers.cadastrarUserAsADM(adminUser, campoEmail.getText(), campoUsername.getText(),
                    new String(campoSenha.getPassword()),
                    me.model.enums.TipoUser.valueOf((String) comboTipo.getSelectedItem()));
                mostrarUsuarios();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void deletarUsuario() {
        int row = tabelaUsuarios.getSelectedRow();
        if (row == -1) return;
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
        if (row == -1) return;
        String id = (String) modeloUsuarios.getValueAt(row, 0);
        User user = gerenciadorUsers.buscarPorId(id);
        List<Filme> filmes = gerenciadorUsers.listarFilmesAlugados(user);
        StringBuilder sb = new StringBuilder("Filmes alugados:\n");
        for (Filme f : filmes) {
            sb.append(f.getNome()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.length() > 0 ? sb.toString() : "Nenhum filme alugado.");
    }

    private void mostrarFilmes() {
        modeloFilmes.setRowCount(0);
        List<Filme> filmes = gerenciadorFilmes.listarFilmes();
        for (Filme f : filmes) {
            modeloFilmes.addRow(new Object[]{
                f.getId(),
                f.getNome(),
                generosToStringAmigavel(f.getGeneros()),
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
                f.getId(), f.getNome(), f.getGeneros(), f.getPreco(), f.getQualidade()
            });
        }
    }

    private void adicionarFilme() {
        Color panelColor = new Color(34, 45, 65);
        Color labelColor = new Color(180, 200, 230);
        Color btnColor = new Color(0, 180, 220);
        Color btnHover = new Color(80, 220, 255);
        Color btnBorder = new Color(0, 120, 180, 180);

        JTextField campoNome = criarCampoDialog(panelColor, labelColor, panelColor);

        JTextArea campoDescricao = new JTextArea(3, 20);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        campoDescricao.setBackground(panelColor);
        campoDescricao.setForeground(labelColor);
        campoDescricao.setCaretColor(labelColor);
        campoDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campoDescricao.setBorder(BorderFactory.createEmptyBorder());
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        scrollDescricao.setBorder(BorderFactory.createEmptyBorder());
        scrollDescricao.getViewport().setBackground(panelColor);
        scrollDescricao.setPreferredSize(new Dimension(220, 60));
        scrollDescricao.setMinimumSize(new Dimension(220, 60));

        JTextField campoDuracao = criarCampoDialog(panelColor, labelColor, panelColor);
        JTextField campoPreco = criarCampoDialog(panelColor, labelColor, panelColor);

        me.model.enums.Qualidade[] qualidades = me.model.enums.Qualidade.values();
        JComboBox<String> comboQualidade = new JComboBox<>();
        for (me.model.enums.Qualidade q : qualidades) {
            comboQualidade.addItem(q.toString());
        }
        comboQualidade.setBackground(panelColor);
        comboQualidade.setForeground(labelColor);
        comboQualidade.setFont(new Font("Segoe UI", Font.BOLD, 15));
        comboQualidade.setBorder(BorderFactory.createEmptyBorder());

        DefaultListModel<String> modeloGeneros = new DefaultListModel<>();
        for (me.model.enums.Genero g : me.model.enums.Genero.values()) {
            modeloGeneros.addElement(g.name());
        }
        JList<String> listaGeneros = new JList<>(modeloGeneros);
        listaGeneros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaGeneros.setVisibleRowCount(Math.min(me.model.enums.Genero.values().length, 6));
        listaGeneros.setBackground(panelColor);
        listaGeneros.setForeground(labelColor);
        listaGeneros.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JScrollPane scrollGeneros = new JScrollPane(listaGeneros);
        scrollGeneros.setBorder(BorderFactory.createEmptyBorder());
        scrollGeneros.getViewport().setBackground(panelColor);
        scrollGeneros.setPreferredSize(new Dimension(220, 80));
        scrollGeneros.setMinimumSize(new Dimension(220, 80));

        JPanel painel = criarPainelDialog();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(criarLabelBranca("Nome:"), gbc);
        gbc.gridx = 1;
        painel.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Descrição:"), gbc);
        gbc.gridx = 1;
        painel.add(scrollDescricao, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Duração (min):"), gbc);
        gbc.gridx = 1;
        painel.add(campoDuracao, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Preço:"), gbc);
        gbc.gridx = 1;
        painel.add(campoPreco, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Qualidade:"), gbc);
        gbc.gridx = 1;
        painel.add(comboQualidade, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Gêneros:"), gbc);
        gbc.gridx = 1;
        painel.add(scrollGeneros, gbc);

        JButton btnOk = criarBotaoPreenchido("OK", btnColor, btnHover, Color.WHITE, btnBorder);
        JButton btnCancel = criarBotaoPreenchido("Cancelar", btnColor, btnHover, Color.WHITE, btnBorder);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.add(btnOk);
        painelBotoes.add(Box.createRigidArea(new Dimension(10, 0)));
        painelBotoes.add(btnCancel);

        JPanel painelDialog = criarPainelDialogExterno(painel, painelBotoes);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adicionar Filme", true);
        dialog.setUndecorated(true);
        dialog.setContentPane(painelDialog);
        dialog.setSize(420, 420);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        btnOk.addActionListener(e -> {
            try {
                List<me.model.enums.Genero> generosSelecionados = listaGeneros.getSelectedValuesList().stream()
                    .map(me.model.enums.Genero::valueOf)
                    .collect(Collectors.toList());
                Filme novo = new Filme(
                    campoNome.getText().trim(),
                    campoDescricao.getText().trim(),
                    java.time.Duration.ofMinutes(Integer.parseInt(campoDuracao.getText().trim())),
                    java.math.BigDecimal.valueOf(Double.parseDouble(campoPreco.getText().trim())),
                    qualidades[comboQualidade.getSelectedIndex()],
                    new java.util.HashSet<>(generosSelecionados)
                );
                gerenciadorFilmes.cadastrarFilme(adminUser, novo);
                mostrarFilmes();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Filme cadastrado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar filme: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void editarFilme() {
        Color panelColor = new Color(34, 45, 65);
        Color borderColor = new Color(44, 62, 80);
        Color labelColor = new Color(180, 200, 230);
        Color btnColor = new Color(0, 180, 220);
        Color btnHover = new Color(80, 220, 255);
        Color btnBorder = new Color(0, 120, 180, 180);

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

        JTextField campoNome = criarCampoDialog(panelColor, labelColor, borderColor);
        campoNome.setText(filme.getNome());

        JTextArea campoDescricao = new JTextArea(filme.getDescricao(), 3, 20);
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);
        campoDescricao.setBackground(panelColor);
        campoDescricao.setForeground(labelColor);
        campoDescricao.setCaretColor(labelColor);
        campoDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campoDescricao.setBorder(BorderFactory.createEmptyBorder());
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        scrollDescricao.setBorder(BorderFactory.createEmptyBorder());
        scrollDescricao.getViewport().setBackground(panelColor);
        scrollDescricao.setPreferredSize(new Dimension(220, 60));
        scrollDescricao.setMinimumSize(new Dimension(220, 60));

        JTextField campoDuracao = criarCampoDialog(panelColor, labelColor, borderColor);
        campoDuracao.setText(String.valueOf(filme.getDuracao().toMinutes()));
        JTextField campoPreco = criarCampoDialog(panelColor, labelColor, borderColor);
        campoPreco.setText(String.valueOf(filme.getPreco()));

        me.model.enums.Qualidade[] qualidades = me.model.enums.Qualidade.values();
        JComboBox<String> comboQualidade = new JComboBox<>();
        for (me.model.enums.Qualidade q : qualidades) {
            comboQualidade.addItem(q.toString());
        }
        comboQualidade.setBackground(panelColor);
        comboQualidade.setForeground(labelColor);
        comboQualidade.setFont(new Font("Segoe UI", Font.BOLD, 15));
        comboQualidade.setBorder(BorderFactory.createEmptyBorder());
        comboQualidade.setSelectedIndex(filme.getQualidade().ordinal());

        DefaultListModel<String> modeloGeneros = new DefaultListModel<>();
        for (me.model.enums.Genero g : me.model.enums.Genero.values()) {
            modeloGeneros.addElement(g.name());
        }
        JList<String> listaGeneros = new JList<>(modeloGeneros);
        listaGeneros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaGeneros.setVisibleRowCount(Math.min(me.model.enums.Genero.values().length, 6));
        listaGeneros.setBackground(panelColor);
        listaGeneros.setForeground(labelColor);
        listaGeneros.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JScrollPane scrollGeneros = new JScrollPane(listaGeneros);
        scrollGeneros.setBorder(BorderFactory.createEmptyBorder());
        scrollGeneros.getViewport().setBackground(panelColor);
        scrollGeneros.setPreferredSize(new Dimension(220, 80));
        scrollGeneros.setMinimumSize(new Dimension(220, 80));

        // Seleciona os gêneros do filme atual
        List<String> generosFilme = filme.getGeneros().stream()
            .map(Enum::name)
            .collect(Collectors.toList());
        int[] indices = generosFilme.stream()
            .mapToInt(s -> modeloGeneros.indexOf(s))
            .filter(i -> i >= 0)
            .toArray();
        listaGeneros.setSelectedIndices(indices);

        JPanel painel = criarPainelDialog();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(criarLabelBranca("Nome:"), gbc);
        gbc.gridx = 1;
        painel.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Descrição:"), gbc);
        gbc.gridx = 1;
        painel.add(scrollDescricao, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Duração (min):"), gbc);
        gbc.gridx = 1;
        painel.add(campoDuracao, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Preço:"), gbc);
        gbc.gridx = 1;
        painel.add(campoPreco, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Qualidade:"), gbc);
        gbc.gridx = 1;
        painel.add(comboQualidade, gbc);

        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Gêneros:"), gbc);
        gbc.gridx = 1;
        painel.add(scrollGeneros, gbc);

        JButton btnOk = criarBotaoPreenchido("OK", btnColor, btnHover, Color.WHITE, btnBorder);
        JButton btnCancel = criarBotaoPreenchido("Cancelar", btnColor, btnHover, Color.WHITE, btnBorder);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.add(btnOk);
        painelBotoes.add(Box.createRigidArea(new Dimension(10, 0)));
        painelBotoes.add(btnCancel);

        JPanel painelDialog = criarPainelDialogExterno(painel, painelBotoes);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Filme", true);
        dialog.setUndecorated(true);
        dialog.setContentPane(painelDialog);
        dialog.setSize(420, 420);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        btnOk.addActionListener(e -> {
            try {
                List<me.model.enums.Genero> generosSelecionadosEdit = listaGeneros.getSelectedValuesList().stream()
                    .map(str -> {
                        for (me.model.enums.Genero g : me.model.enums.Genero.values()) {
                            if (g.toString().equals(str)) return g;
                        }
                        return null;
                    })
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toList());
                Filme editado = new Filme(
                    filme.getId(),
                    campoNome.getText().trim(),
                    campoDescricao.getText().trim(),
                    java.time.Duration.ofMinutes(Integer.parseInt(campoDuracao.getText().trim())),
                    java.math.BigDecimal.valueOf(Double.parseDouble(campoPreco.getText().trim())),
                    qualidades[comboQualidade.getSelectedIndex()],
                    new java.util.HashSet<>(generosSelecionadosEdit)
                );
                gerenciadorFilmes.editarFilme(adminUser, editado);
                mostrarFilmes();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Filme editado com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao editar filme: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
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

    private JTextField criarCampoBusca(Color bg, Color border, String prompt) {
        JTextField campo = new PromptTextField(prompt);
        campo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        campo.setBackground(bg.brighter());
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createLineBorder(border, 2, true));
        campo.setPreferredSize(new Dimension(0, 38));
        campo.setOpaque(true);
        return campo;
    }

    private static class PromptTextField extends JTextField {
        private final String prompt;
        public PromptTextField(String prompt) {
            this.prompt = prompt;
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setFont(getFont().deriveFont(Font.ITALIC));
                g2.setColor(new Color(255, 255, 255, 120));
                Insets insets = getInsets();
                g2.drawString(prompt, insets.left + 4, getHeight() / 2 + getFont().getSize() / 2 - 2);
                g2.dispose();
            }
        }
    }

    private String generosToStringAmigavel(java.util.Set<me.model.enums.Genero> generos) {
        return generos.stream().map(me.model.enums.Genero::toString).collect(Collectors.joining(", "));
    }
}
