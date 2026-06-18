package artgallery.ui;

import artgallery.repository.RepositorioObra;
import artgallery.service.ArtGallery;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private ArtGallery gallery;

    public MainFrame() {
        RepositorioObra repo = new RepositorioObra();
        gallery = new ArtGallery(repo);

        setTitle("ArtGallery");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Obras",      new ObraPanel(gallery));
        tabs.addTab("Avaliacoes", new AvaliacaoPanel(gallery));
        tabs.addTab("Busca",      new BuscaPanel(gallery));
        tabs.addTab("Exposicoes", new ExposicaoPanel(gallery));
        tabs.addTab("Rankings",   new RankingPanel(gallery));
        add(tabs);
    }
    public static JButton btn(String texto) {
        return new JButton(texto);
    }
    public static void msgSucesso(Component pai, String msg) {
        JOptionPane.showMessageDialog(pai, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void msgErro(Component pai, String msg) {
        JOptionPane.showMessageDialog(pai, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
