package jumpingthomas;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/** Lance UNIQUEMENT le decor : pas de Game Over, pas d'obstacles, pas de joueur. */
public final class RoutePreview extends JPanel {
    private final RouteRenderer route = new RouteRenderer(450, 800, 800/3);

    public RoutePreview() {
        setPreferredSize(new Dimension(450, 800));
        new Timer(16, e -> {
            route.mettreAJour(0.5);
            repaint();
        }).start();
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        route.dessiner(g, this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Jumping Thomas - Apercu Rainbow Road");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setResizable(false);
            f.add(new RoutePreview());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}
