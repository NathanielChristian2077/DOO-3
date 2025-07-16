package view.telas;

import me.control.GerenciadorFilmes;
import me.control.GerenciadorUsers;
import me.model.entidades.Filme;
import me.model.entidades.User;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TelaUser extends JInternalFrame {
    private final User user;
    private final GerenciadorUsers gerenciadorUsers = new GerenciadorUsers();
    private final GerenciadorFilmes gerenciadorFilmes = new GerenciadorFilmes();

    private JTable tabelaFilmes;
    private DefaultTableModel modeloFilmes;
    private JTextField campoBuscaFilme;
    private JComboBox<String> comboGenero;
    private JComboBox<String> comboOrdenar;

    private JTable tabelaAlugados;
    private DefaultTableModel modeloAlugados;

    private final Color bgColor = new Color(24, 32, 48);
    private final Color panelColor = new Color(34, 45, 65);
    private final Color borderColor = new Color(44, 62, 80);
    private final Color labelColor = new Color(180, 200, 230);
    private final Color btnColor = new Color(60, 179, 113);
    private final Color btnHover = new Color(80, 220, 180);
    private final Color btnBorder = new Color(60, 179, 113, 120);

    public TelaUser(User user) {
        super("Painel do Usuário", true, true, true, true);
        this.user = user;
        setSize(900, 600);
        setLocation(100, 100);
        setLayout(new BorderLayout());
        setBackground(bgColor);

        JPanel painelCentralizador = new JPanel(new GridBagLayout());
        painelCentralizador.setBackground(bgColor);
        GridBagConstraints gbcCentral = new GridBagConstraints();
        gbcCentral.gridx = 0; gbcCentral.gridy = 0;
        gbcCentral.weightx = 1.0; gbcCentral.weighty = 1.0;
        gbcCentral.anchor = GridBagConstraints.CENTER;
        gbcCentral.fill = GridBagConstraints.BOTH;

        JPanel painelCentral = new JPanel(new BorderLayout()) {
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
        painelCentral.setOpaque(false);
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JToolBar barraFerramentas = new JToolBar();
        barraFerramentas.setFloatable(false);
        barraFerramentas.setBackground(panelColor);

        JLabel lblUser = new JLabel("Usuário");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblUser.setForeground(Color.WHITE);
        lblUser.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        barraFerramentas.add(lblUser);
        barraFerramentas.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton btnEditarDados = criarBotaoPreenchido("Editar Dados", btnColor, btnHover, Color.WHITE, btnBorder);
        btnEditarDados.addActionListener(e -> editarDadosUsuario());
        barraFerramentas.add(btnEditarDados);

        painelCentral.add(barraFerramentas, BorderLayout.NORTH);

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
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                // Remove borda padrão
            }
        });

        JPanel painelFilmes = new JPanel(new BorderLayout());
        painelFilmes.setOpaque(false);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.setOpaque(false);

        campoBuscaFilme = criarCampoBusca(panelColor, btnColor, "Buscar filme por nome...");
        campoBuscaFilme.setToolTipText("Buscar filme por nome...");
        campoBuscaFilme.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarFilmes(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarFilmes(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarFilmes(); }
        });
        painelBusca.add(criarLabelBranca("Buscar:"));
        painelBusca.add(campoBuscaFilme);

        comboGenero = new JComboBox<>();
        comboGenero.addItem("Todos");
        for (me.model.enums.Genero g : me.model.enums.Genero.values()) {
            comboGenero.addItem(g.name()); // Use name() para o valor interno
        }
        comboGenero.setBackground(panelColor);
        comboGenero.setForeground(labelColor);
        comboGenero.setFont(new Font("Segoe UI", Font.BOLD, 15));
        comboGenero.setBorder(BorderFactory.createEmptyBorder());
        comboGenero.addActionListener(e -> filtrarFilmes());
        painelBusca.add(criarLabelBranca("Gênero:"));
        painelBusca.add(comboGenero);

        comboOrdenar = new JComboBox<>(new String[]{"Nome", "Preço", "Qualidade"});
        comboOrdenar.setBackground(panelColor);
        comboOrdenar.setForeground(labelColor);
        comboOrdenar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        comboOrdenar.setBorder(BorderFactory.createEmptyBorder());
        comboOrdenar.addActionListener(e -> ordenarFilmes());
        painelBusca.add(criarLabelBranca("Ordenar por:"));
        painelBusca.add(comboOrdenar);

        JButton btnAlugar = criarBotaoPreenchido("Alugar Filme Selecionado", btnColor, btnHover, Color.WHITE, btnBorder);
        btnAlugar.setFont(btnAlugar.getFont().deriveFont(Font.BOLD, 14f));
        btnAlugar.addActionListener(e -> alugarFilmeSelecionado());
        painelBusca.add(btnAlugar);

        painelFilmes.add(painelBusca, BorderLayout.NORTH);

        modeloFilmes = new DefaultTableModel(new Object[]{"Nome", "Gêneros", "Preço", "Qualidade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaFilmes = new JTable(modeloFilmes);
        estilizarTabela(tabelaFilmes, panelColor, labelColor, borderColor);

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

        JScrollPane scrollFilmes = new JScrollPane(tabelaFilmes);
        scrollFilmes.getViewport().setBackground(panelColor);
        scrollFilmes.setBackground(panelColor);
        scrollFilmes.setOpaque(true);
        scrollFilmes.setBorder(BorderFactory.createEmptyBorder());
        tabelaFilmes.setOpaque(false);
        scrollFilmes.getViewport().setOpaque(true);
        painelFilmes.add(scrollFilmes, BorderLayout.CENTER);

        abas.addTab("Catálogo de Filmes", painelFilmes);

        JPanel painelAlugados = new JPanel(new BorderLayout());
        painelAlugados.setOpaque(false);
        modeloAlugados = new DefaultTableModel(new Object[]{"Nome", "Gêneros", "Preço", "Qualidade"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaAlugados = new JTable(modeloAlugados);
        estilizarTabela(tabelaAlugados, panelColor, labelColor, borderColor);

        tabelaAlugados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && tabelaAlugados.getSelectedRow() != -1) {
                    String nome = (String) modeloAlugados.getValueAt(tabelaAlugados.getSelectedRow(), 0);
                    Filme filme = gerenciadorUsers.listarFilmesAlugados(user).stream()
                        .filter(f -> f.getNome().equals(nome)).findFirst().orElse(null);
                    if (filme != null) {
                        JInternalFrame tela = new TelaFilme(filme);
                        ((JDesktopPane) TelaUser.this.getParent()).add(tela);
                        tela.setVisible(true);
                    }
                }
            }
        });

        JButton btnDesalugar = criarBotaoPreenchido("Devolver Filme Selecionado", btnColor, btnHover, Color.WHITE, btnBorder);
        btnDesalugar.addActionListener(e -> desalugarFilmeSelecionado());

        JScrollPane scrollAlugados = new JScrollPane(tabelaAlugados);
        scrollAlugados.getViewport().setBackground(panelColor);
        scrollAlugados.setBackground(panelColor);
        scrollAlugados.setOpaque(true);
        scrollAlugados.setBorder(BorderFactory.createEmptyBorder());
        tabelaAlugados.setOpaque(false);
        scrollAlugados.getViewport().setOpaque(true);

        painelAlugados.add(scrollAlugados, BorderLayout.CENTER);

        JPanel painelBotoesAlugados = new JPanel();
        painelBotoesAlugados.setOpaque(false);
        painelBotoesAlugados.add(btnDesalugar);
        painelAlugados.add(painelBotoesAlugados, BorderLayout.SOUTH);

        abas.addTab("Meus Filmes Alugados", painelAlugados);

        painelCentral.add(abas, BorderLayout.CENTER);

        painelCentral.setPreferredSize(new Dimension(900, 600));
        painelCentralizador.add(painelCentral, gbcCentral);

        setContentPane(painelCentralizador);

        SwingUtilities.invokeLater(() -> {
            try {
                setMaximum(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        mostrarFilmes();
        mostrarFilmesAlugados();
    }

    private JLabel criarLabelBranca(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        return lbl;
    }

    private JTextField criarCampoBusca(Color bg, Color border, String prompt) {
        JTextField campo = new PromptTextField(prompt);
        campo.setBackground(bg);
        campo.setForeground(labelColor);
        campo.setCaretColor(labelColor);
        campo.setBorder(BorderFactory.createLineBorder(new Color(200, 220, 255), 2, false));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campo.setOpaque(true);
        campo.setPreferredSize(new Dimension(260, 36));
        return campo;
    }

    private JButton criarBotaoPreenchido(String texto, Color bg, Color hover, Color fg, Color border) {
        JButton btn = new JButton(texto) {
            private boolean hoverState = false;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hoverState ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(border);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        setForeground(Color.WHITE);
                        hoverState = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        setForeground(Color.WHITE);
                        hoverState = false;
                        repaint();
                    }
                });
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
                c.setBackground(isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? bg : borderColor));
                c.setForeground(fg);
                setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
                return c;
            }
        });

        tabela.setOpaque(false);
        tabela.setFillsViewportHeight(true);
        if (tabela.getParent() != null) {
            tabela.getParent().setBackground(bg);
        }
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
                g2.setColor(new Color(180, 200, 230, 120));
                g2.drawString(prompt, 8, getHeight() / 2 + getFont().getSize() / 2 - 2);
                g2.dispose();
            }
        }
    }

    private void mostrarFilmes() {
        modeloFilmes.setRowCount(0);
        List<Filme> filmes = gerenciadorFilmes.listarFilmes();
        for (Filme f : filmes) {
            modeloFilmes.addRow(new Object[]{
                f.getNome(),
                generosToStringAmigavel(f.getGeneros()),
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
            .filter(f -> genero.equals("Todos") || f.getGeneros().stream().anyMatch(g -> g.name().equals(genero)))
            .collect(Collectors.toList());
        for (Filme f : filtrados) {
            modeloFilmes.addRow(new Object[]{
                f.getNome(),
                generosToStringAmigavel(f.getGeneros()),
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
                generosToStringAmigavel(f.getGeneros()),
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

    private void mostrarFilmesAlugados() {
        modeloAlugados.setRowCount(0);
        List<Filme> alugados = gerenciadorUsers.listarFilmesAlugados(user);
        for (Filme f : alugados) {
            modeloAlugados.addRow(new Object[]{
                f.getNome(),
                generosToStringAmigavel(f.getGeneros()),
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

    private void editarDadosUsuario() {
        JPanel painel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panelColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        painel.setOpaque(false);
        painel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        JTextField campoEmail = criarCampoBusca(panelColor, btnColor, "");
        campoEmail.setText(user.getEmail());
        JTextField campoUsername = criarCampoBusca(panelColor, btnColor, "");
        campoUsername.setText(user.getUsername());
        JPasswordField campoSenhaAtual = new JPasswordField();
        campoSenhaAtual.setBackground(panelColor);
        campoSenhaAtual.setForeground(labelColor);
        campoSenhaAtual.setCaretColor(labelColor);
        campoSenhaAtual.setBorder(BorderFactory.createLineBorder(borderColor, 2, false));
        campoSenhaAtual.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campoSenhaAtual.setOpaque(true);
        JPasswordField campoNovaSenha = new JPasswordField();
        campoNovaSenha.setBackground(panelColor);
        campoNovaSenha.setForeground(labelColor);
        campoNovaSenha.setCaretColor(labelColor);
        campoNovaSenha.setBorder(BorderFactory.createLineBorder(borderColor, 2, false));
        campoNovaSenha.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        campoNovaSenha.setOpaque(true);

        painel.add(criarLabelBranca("Novo Email:"), gbc);
        gbc.gridx = 1;
        painel.add(campoEmail, gbc);
        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Novo Username:"), gbc);
        gbc.gridx = 1;
        painel.add(campoUsername, gbc);
        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Senha Atual:"), gbc);
        gbc.gridx = 1;
        painel.add(campoSenhaAtual, gbc);
        gbc.gridx = 0; gbc.gridy++;
        painel.add(criarLabelBranca("Nova Senha (opcional):"), gbc);
        gbc.gridx = 1;
        painel.add(campoNovaSenha, gbc);

        JButton btnOk = criarBotaoPreenchido("OK", btnColor, btnHover, Color.WHITE, btnBorder);
        JButton btnCancel = criarBotaoPreenchido("Cancelar", btnColor, btnHover, Color.WHITE, btnBorder);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.add(btnOk);
        painelBotoes.add(Box.createRigidArea(new Dimension(10, 0)));
        painelBotoes.add(btnCancel);

        JPanel painelDialog = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(panelColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
            }
        };
        painelDialog.setOpaque(false);
        painelDialog.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        painelDialog.add(painel, BorderLayout.CENTER);
        painelDialog.add(painelBotoes, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Dados", true);
        dialog.setUndecorated(true);
        dialog.setContentPane(painelDialog);
        dialog.setSize(420, 320);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        btnOk.addActionListener(e -> {
            String email = campoEmail.getText().trim();
            String username = campoUsername.getText().trim();
            String senhaAtual = new String(campoSenhaAtual.getPassword());
            String novaSenha = new String(campoNovaSenha.getPassword());

            if (!senhaAtual.equals(user.getPassword())) {
                JOptionPane.showMessageDialog(dialog, "Senha atual incorreta.");
                return;
            }
            user.setEmail(email);
            user.setUsername(username);
            if (!novaSenha.isEmpty()) {
                user.setPassword(novaSenha);
            }
            gerenciadorUsers.editarUser(user);
            JOptionPane.showMessageDialog(dialog, "Dados atualizados com sucesso!");
            dialog.dispose();
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private String generosToStringAmigavel(Set<me.model.enums.Genero> generos) {
        return generos.stream().map(me.model.enums.Genero::toString).collect(Collectors.joining(", "));
    }
}
