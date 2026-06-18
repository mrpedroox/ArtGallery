package artgallery.ui;

import artgallery.model.Exposicao;
import artgallery.model.Obra;
import artgallery.service.ArtGallery;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class ExposicaoPanel extends JPanel {

    private ArtGallery gallery;
    private JTextField txtNomeExpo = new JTextField(15);
    private JTextField txtObraExpo = new JTextField(15);
    private JComboBox<String> cbExposicoes = new JComboBox<>();
    private JTextArea areaResult = new JTextArea(15, 30);

    public ExposicaoPanel(ArtGallery gallery) {
        this.gallery = gallery;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel formCriar = new JPanel(new GridLayout(0, 2, 4, 4));
        formCriar.setBorder(BorderFactory.createTitledBorder("Nova Exposicao"));
        formCriar.add(new JLabel("Nome:")); formCriar.add(txtNomeExpo);
        JButton btnCriar = new JButton("Criar Exposicao");
        btnCriar.addActionListener(e -> criar());
        formCriar.add(new JLabel("")); formCriar.add(btnCriar);

        JPanel formAdd = new JPanel(new GridLayout(0, 2, 4, 4));
        formAdd.setBorder(BorderFactory.createTitledBorder("Adicionar Obra a Exposicao"));
        formAdd.add(new JLabel("Exposicao:")); formAdd.add(cbExposicoes);
        formAdd.add(new JLabel("Titulo da obra:")); formAdd.add(txtObraExpo);
        JButton btnAdd = new JButton("Adicionar");
        JButton btnVer = new JButton("Ver Obras");
        btnAdd.addActionListener(e -> adicionar());
        btnVer.addActionListener(e -> ver());
        formAdd.add(btnAdd); formAdd.add(btnVer);

        JPanel esquerda = new JPanel(new BorderLayout(4, 4));
        esquerda.add(formCriar, BorderLayout.NORTH);
        esquerda.add(formAdd, BorderLayout.CENTER);

        areaResult.setEditable(false);
        areaResult.setText("Selecione uma exposicao e clique em Ver Obras.");
        JScrollPane scroll = new JScrollPane(areaResult);
        scroll.setBorder(BorderFactory.createTitledBorder("Obras da Exposicao"));

        add(esquerda, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);

        atualizarCombo();
    }

    private void criar() {
        String nome = txtNomeExpo.getText().trim();
        if (nome.isEmpty()) { MainFrame.msgErro(this, "Digite o nome."); return; }
        try {
            gallery.criarExposicao(nome);
            MainFrame.msgSucesso(this, "Exposicao criada!");
            txtNomeExpo.setText("");
            atualizarCombo();
        } catch (RuntimeException ex) { MainFrame.msgErro(this, ex.getMessage()); }
    }

    private void adicionar() {
        String expo  = (String) cbExposicoes.getSelectedItem();
        String titulo = txtObraExpo.getText().trim();
        if (expo == null) { MainFrame.msgErro(this, "Nenhuma exposicao disponivel."); return; }
        if (titulo.isEmpty()) { MainFrame.msgErro(this, "Digite o titulo da obra."); return; }
        try {
            gallery.adicionarObraAExposicao(expo, titulo);
            MainFrame.msgSucesso(this, "Obra adicionada!");
            txtObraExpo.setText("");
        } catch (RuntimeException ex) { MainFrame.msgErro(this, ex.getMessage()); }
    }

    private void ver() {
        String expo = (String) cbExposicoes.getSelectedItem();
        if (expo == null) { MainFrame.msgErro(this, "Nenhuma exposicao disponivel."); return; }
        Vector<Obra> obras = gallery.obrasExpostas(expo);
        if (obras.isEmpty()) { areaResult.setText("Exposicao vazia."); return; }
        StringBuilder sb = new StringBuilder();
        sb.append("Exposicao: ").append(expo).append("  (").append(obras.size()).append(" obra(s))\n\n");
        int i = 1;
        for (Obra o : obras) {
            sb.append(i++).append(". ").append(o.exibirDetalhes()).append("\n\n");
        }
        areaResult.setText(sb.toString());
        areaResult.setCaretPosition(0);
    }

    public void atualizarCombo() {
        cbExposicoes.removeAllItems();
        for (Exposicao e : gallery.listarExposicoes()) cbExposicoes.addItem(e.getNome());
    }
}
