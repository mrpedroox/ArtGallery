package artgallery.ui;

import artgallery.model.Obra;
import artgallery.service.ArtGallery;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class BuscaPanel extends JPanel {

    private ArtGallery gallery;
    private JTextField txtBusca   = new JTextField(20);
    private JRadioButton rbAutor  = new JRadioButton("Por Artista", true);
    private JRadioButton rbTitulo = new JRadioButton("Por Titulo");
    private JTextArea areaResult  = new JTextArea(20, 40);

    public BuscaPanel(ArtGallery gallery) {
        this.gallery = gallery;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbAutor); grupo.add(rbTitulo);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscar());
        txtBusca.addActionListener(e -> buscar());

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.setBorder(BorderFactory.createTitledBorder("Busca"));
        topo.add(rbAutor);
        topo.add(rbTitulo);
        topo.add(new JLabel("Termo:"));
        topo.add(txtBusca);
        topo.add(btnBuscar);

        areaResult.setEditable(false);
        areaResult.setText("Use o campo acima para buscar.");
        JScrollPane scroll = new JScrollPane(areaResult);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        add(topo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void buscar() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) { MainFrame.msgErro(this, "Digite um termo."); return; }

        Vector<Obra> resultados;
        if (rbAutor.isSelected()) {
            resultados = gallery.buscarPorAutor(termo);
        } else {
            resultados = new Vector<>();
            for (Obra o : gallery.listarObras())
                if (o.getTitulo().equalsIgnoreCase(termo)) resultados.add(o);        }

        if (resultados.isEmpty()) { areaResult.setText("Nenhuma obra encontrada."); return; }

        StringBuilder sb = new StringBuilder();
        sb.append(resultados.size()).append(" resultado(s)\n\n");
        for (Obra o : resultados) {
            sb.append(o.exibirDetalhes()).append("\n");
            sb.append("Status: ").append(o.isAtiva() ? "Ativa" : "Inativa").append("\n");
            if (!o.getAvaliacoes().isEmpty())
                sb.append(String.format("Media: %.1f/10  (%d avaliacoes)\n", o.mediaAvaliacoes(), o.getAvaliacoes().size()));
            else sb.append("Sem avaliacoes\n");
            sb.append("---\n");
        }
        areaResult.setText(sb.toString());
        areaResult.setCaretPosition(0);
    }
}
