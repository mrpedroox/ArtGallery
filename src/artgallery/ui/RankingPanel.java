package artgallery.ui;

import artgallery.model.Obra;
import artgallery.service.ArtGallery;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RankingPanel extends JPanel {

    private ArtGallery gallery;
    private JTextArea areaRanking = new JTextArea(20, 40);

    public RankingPanel(ArtGallery gallery) {
        this.gallery = gallery;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JButton btnTop   = new JButton("Top por Avaliacao");
        JButton btnTodas = new JButton("Listar Todas");
        btnTop.addActionListener(e -> mostrarTop());
        btnTodas.addActionListener(e -> mostrarTodas());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnTop);
        botoes.add(btnTodas);

        areaRanking.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaRanking);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        add(botoes, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        mostrarTop();
    }

    private void mostrarTop() {
        Vector<Obra> top = gallery.topObras();
        if (top.isEmpty()) { areaRanking.setText("Nenhuma obra cadastrada."); return; }
        StringBuilder sb = new StringBuilder("RANKING POR MEDIA DE AVALIACOES\n\n");
        int pos = 1;
        for (Obra o : top) {
            sb.append(pos++).append(". ").append(o.getTitulo()).append(" - ").append(o.getAutor()).append("\n");
            if (o.getAvaliacoes().isEmpty()) sb.append("   Sem avaliacoes\n");
            else sb.append(String.format("   Media: %.1f/10  (%d avaliacoes)\n",
                    o.mediaAvaliacoes(), o.getAvaliacoes().size()));
            sb.append("\n");
        }
        areaRanking.setText(sb.toString());
        areaRanking.setCaretPosition(0);
    }

    private void mostrarTodas() {
        Vector<Obra> obras = gallery.listarObras();
        if (obras.isEmpty()) { areaRanking.setText("Nenhuma obra ativa."); return; }
        StringBuilder sb = new StringBuilder("TODAS AS OBRAS ATIVAS\n\n");
        for (Obra o : obras) {
            sb.append(o.exibirDetalhes()).append("\n\n");
        }
        areaRanking.setText(sb.toString());
        areaRanking.setCaretPosition(0);
    }
}
