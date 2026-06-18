package artgallery.ui;

import artgallery.model.*;
import artgallery.service.ArtGallery;
import artgallery.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class ObraPanel extends JPanel {

    private ArtGallery gallery;

    private JTextField txtTitulo  = new JTextField(15);
    private JTextField txtAutor   = new JTextField(15);
    private JTextField txtParam1  = new JTextField(15);
    private JTextField txtParam2  = new JTextField(15);
    private JComboBox<String> cbTipo = new JComboBox<>(new String[]{
            "Pintura Digital", "Modelagem 3D", "Arte Generativa"});
    private JLabel lblParam1 = new JLabel("Resolucao:");
    private JLabel lblParam2 = new JLabel("Software:");

    private DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private JList<String> listaObras = new JList<>(modeloLista);
    private JTextArea areaDetalhes = new JTextArea(6, 30);

    // Transformado em atributo da classe para alterar o texto durante a edição
    private JButton btnCadastrar = new JButton("Cadastrar");

    public ObraPanel(ArtGallery gallery) {
        this.gallery = gallery;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // painel de cadastro (esquerda)
        JPanel form = new JPanel(new GridLayout(0, 2, 4, 4));
        form.setBorder(BorderFactory.createTitledBorder("Cadastrar / Editar Obra"));

        form.add(new JLabel("Tipo:")); form.add(cbTipo);
        form.add(new JLabel("Titulo:")); form.add(txtTitulo);
        form.add(new JLabel("Autor:")); form.add(txtAutor);
        form.add(lblParam1); form.add(txtParam1);
        form.add(lblParam2); form.add(txtParam2);

        cbTipo.addActionListener(e -> atualizarLabels());

        JButton btnEditar = new JButton("Editar");
        JButton btnDesativar = new JButton("Desativar Selecionada");

        btnCadastrar.addActionListener(e -> processarFormulario());
        btnEditar.addActionListener(e -> prepararEdicao());
        btnDesativar.addActionListener(e -> desativar());

        JPanel botoesForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesForm.add(btnCadastrar);
        botoesForm.add(btnEditar);
        botoesForm.add(btnDesativar);

        JPanel esquerda = new JPanel(new BorderLayout(4, 4));
        esquerda.add(form, BorderLayout.CENTER);
        esquerda.add(botoesForm, BorderLayout.SOUTH);

        // painel direito: lista + detalhes
        listaObras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaObras.addListSelectionListener(e -> mostrarDetalhes());
        JScrollPane scrollLista = new JScrollPane(listaObras);
        scrollLista.setBorder(BorderFactory.createTitledBorder("Obras Cadastradas"));

        areaDetalhes.setEditable(false);
        areaDetalhes.setText("Selecione uma obra para ver os detalhes.");
        JScrollPane scrollDet = new JScrollPane(areaDetalhes);
        scrollDet.setBorder(BorderFactory.createTitledBorder("Detalhes"));

        JButton btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.addActionListener(e -> atualizarLista());

        JPanel direita = new JPanel(new BorderLayout(4, 4));
        direita.add(scrollLista, BorderLayout.CENTER);
        direita.add(scrollDet, BorderLayout.SOUTH);
        direita.add(btnAtualizar, BorderLayout.NORTH);

        add(esquerda, BorderLayout.WEST);
        add(direita, BorderLayout.CENTER);

        atualizarLista();
    }

    private void atualizarLabels() {
        String tipo = (String) cbTipo.getSelectedItem();
        if ("Pintura Digital".equals(tipo)) {
            lblParam1.setText("Resolucao:"); lblParam2.setText("Software:");
        } else if ("Modelagem 3D".equals(tipo)) {
            lblParam1.setText("Num Poligonos:"); lblParam2.setText("Engine:");
        } else {
            lblParam1.setText("Algoritmo:"); lblParam2.setText("Seed (numero):");
        }
    }

    private void prepararEdicao() {
        String sel = listaObras.getSelectedValue();
        if (sel == null) {
            MainFrame.msgErro(this, "Selecione uma obra na lista para editar.");
            return;
        }

        String titulo = sel.split(" - ")[0].trim();
        Obra obraSelecionada = null;

        for (Obra o : gallery.listarObras()) {
            if (o.getTitulo().equalsIgnoreCase(titulo)) {
                obraSelecionada = o;
                break;
            }
        }

        if (obraSelecionada != null) {
            txtTitulo.setText(obraSelecionada.getTitulo());
            txtTitulo.setEditable(false); // Bloqueia o título para não quebrar a busca na atualização
            txtAutor.setText(obraSelecionada.getAutor());

            if (obraSelecionada instanceof PinturaDigital) {
                cbTipo.setSelectedItem("Pintura Digital");
                PinturaDigital pd = (PinturaDigital) obraSelecionada;
                txtParam1.setText(pd.getResolucao());
                txtParam2.setText(pd.getSoftwareUtilizado());
            } else if (obraSelecionada instanceof Modelagem3D) {
                cbTipo.setSelectedItem("Modelagem 3D");
                Modelagem3D m3d = (Modelagem3D) obraSelecionada;
                txtParam1.setText(String.valueOf(m3d.getNumeroPoligonos()));
                txtParam2.setText(m3d.getEngine());
            } else if (obraSelecionada instanceof ArteGenerativa) {
                cbTipo.setSelectedItem("Arte Generativa");
                ArteGenerativa ag = (ArteGenerativa) obraSelecionada;
                txtParam1.setText(ag.getAlgoritmo());
                txtParam2.setText(String.valueOf(ag.getSeed()));
            }

            btnCadastrar.setText("Salvar Edição");
        }
    }

    private void processarFormulario() {
        String titulo = txtTitulo.getText().trim();
        String autor  = txtAutor.getText().trim();
        String p1     = txtParam1.getText().trim();
        String p2     = txtParam2.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty() || p1.isEmpty() || p2.isEmpty()) {
            MainFrame.msgErro(this, "Preencha todos os campos.");
            return;
        }

        String tipo = (String) cbTipo.getSelectedItem();
        Obra obra;

        try {
            if ("Pintura Digital".equals(tipo)) {
                obra = new PinturaDigital(titulo, autor, p1, p2);
            } else if ("Modelagem 3D".equals(tipo)) {
                obra = new Modelagem3D(titulo, autor, Integer.parseInt(p1), p2);
            } else {
                obra = new ArteGenerativa(titulo, autor, p1, Long.parseLong(p2));
            }
        } catch (NumberFormatException ex) {
            MainFrame.msgErro(this, "Valor numerico invalido.");
            return;
        }

        try {
            if (btnCadastrar.getText().equals("Salvar Edição")) {
                gallery.atualizarObra(obra);
                MainFrame.msgSucesso(this, "Obra atualizada com sucesso!");
                btnCadastrar.setText("Cadastrar"); // Reseta o botão
                txtTitulo.setEditable(true);       // Libera o título novamente
            } else {
                gallery.publicarObra(obra);
                MainFrame.msgSucesso(this, "Obra cadastrada!");
            }

            txtTitulo.setText(""); txtAutor.setText(""); txtParam1.setText(""); txtParam2.setText("");
            atualizarLista();

        } catch (Exception ex) { // Captura ObraNaoEncontradaException, ObraJaCadastradaException, etc.
            MainFrame.msgErro(this, ex.getMessage());
        }
    }

    private void desativar() {
        String sel = listaObras.getSelectedValue();
        if (sel == null) { MainFrame.msgErro(this, "Selecione uma obra."); return; }
        String titulo = sel.split(" - ")[0].trim();
        int c = JOptionPane.showConfirmDialog(this, "Desativar \"" + titulo + "\"?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            try {
                gallery.removerObra(titulo);
                MainFrame.msgSucesso(this, "Obra desativada.");
                atualizarLista();
                areaDetalhes.setText("");
            } catch (RuntimeException ex) {
                MainFrame.msgErro(this, ex.getMessage());
            }
        }
    }

    private void mostrarDetalhes() {
        String sel = listaObras.getSelectedValue();
        if (sel == null) return;
        String titulo = sel.split(" - ")[0].trim();
        for (Obra o : gallery.listarObras()) {
            if (o.getTitulo().equalsIgnoreCase(titulo)) {
                String texto = o.exibirDetalhes()
                        + "\nStatus: " + (o.isAtiva() ? "Ativa" : "Inativa")
                        + "\nMedia: " + (o.getAvaliacoes().isEmpty() ? "Sem avaliacoes"
                        : String.format("%.1f / 10", o.mediaAvaliacoes()))
                        + "\nTotal avaliacoes: " + o.getAvaliacoes().size();
                areaDetalhes.setText(texto);
                return;
            }
        }
    }

    public void atualizarLista() {
        modeloLista.clear();
        for (Obra o : gallery.listarObras()) {
            modeloLista.addElement(o.getTitulo() + " - " + o.getAutor());
        }
    }
}