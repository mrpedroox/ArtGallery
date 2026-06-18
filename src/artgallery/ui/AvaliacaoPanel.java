package artgallery.ui;

import artgallery.exceptions.NotaInvalidaException;
import artgallery.model.Avaliacao;
import artgallery.model.Obra;
import artgallery.service.ArtGallery;

import javax.swing.*;
import java.awt.*;

public class AvaliacaoPanel extends JPanel {

    private ArtGallery gallery;
    private JTextField txtTitulo  = new JTextField(15);
    private JTextField txtUsuario = new JTextField(15);
    private JSpinner   spinNota   = new JSpinner(new SpinnerNumberModel(8, 0, 10, 1));
    private JTextArea  txtComent  = new JTextArea(3, 15);
    private JTextArea  areaResult = new JTextArea(15, 30);

    public AvaliacaoPanel(ArtGallery gallery) {
        this.gallery = gallery;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 4, 4));
        form.setBorder(BorderFactory.createTitledBorder("Avaliar Obra"));
        form.add(new JLabel("Titulo da obra:")); form.add(txtTitulo);
        form.add(new JLabel("Usuario:"));        form.add(txtUsuario);
        form.add(new JLabel("Nota (0-10):"));    form.add(spinNota);
        form.add(new JLabel("Comentario:"));
        form.add(new JScrollPane(txtComent));

        JButton btnEnviar = new JButton("Enviar Avaliacao");
        JButton btnVer    = new JButton("Ver Avaliacoes");
        btnEnviar.addActionListener(e -> enviar());
        btnVer.addActionListener(e -> ver());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnEnviar);
        botoes.add(btnVer);

        JPanel esquerda = new JPanel(new BorderLayout(4, 4));
        esquerda.add(form, BorderLayout.CENTER);
        esquerda.add(botoes, BorderLayout.SOUTH);

        areaResult.setEditable(false);
        areaResult.setText("Digite o titulo e clique em Ver Avaliacoes.");
        JScrollPane scroll = new JScrollPane(areaResult);
        scroll.setBorder(BorderFactory.createTitledBorder("Avaliacoes"));

        add(esquerda, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);
    }

    private void enviar() {
        String titulo   = txtTitulo.getText().trim();
        String usuario  = txtUsuario.getText().trim();
        int nota        = (Integer) spinNota.getValue();
        String coment   = txtComent.getText().trim();
        if (titulo.isEmpty() || usuario.isEmpty()) {
            MainFrame.msgErro(this, "Preencha titulo e usuario."); return;
        }
        try {
            gallery.avaliarObra(titulo, new Avaliacao(usuario, nota, coment));
            MainFrame.msgSucesso(this, "Avaliacao salva!");
            txtComent.setText("");
            ver();
        } catch (NotaInvalidaException | RuntimeException ex) {
            MainFrame.msgErro(this, ex.getMessage());
        }
    }

    private void ver() {
        String titulo = txtTitulo.getText().trim();
        if (titulo.isEmpty()) { MainFrame.msgErro(this, "Digite o titulo."); return; }
        Obra obra = null;
        for (Obra o : gallery.listarObras())
            if (o.getTitulo().equalsIgnoreCase(titulo)) { obra = o; break; }
        if (obra == null) { areaResult.setText("Obra nao encontrada."); return; }
        if (obra.getAvaliacoes().isEmpty()) { areaResult.setText("Sem avaliacoes."); return; }
        StringBuilder sb = new StringBuilder();
        sb.append("Obra: ").append(obra.getTitulo()).append("\n");
        sb.append(String.format("Media: %.1f / 10  (%d avaliacoes)\n\n",
                obra.mediaAvaliacoes(), obra.getAvaliacoes().size()));
        for (Avaliacao a : obra.getAvaliacoes()) {
            sb.append("Usuario: ").append(a.getUsuario()).append("  Nota: ").append(a.getNota()).append("/10\n");
            if (!a.getComentario().isEmpty()) sb.append("Comentario: ").append(a.getComentario()).append("\n");
            sb.append("\n");
        }
        areaResult.setText(sb.toString());
        areaResult.setCaretPosition(0);
    }
}
