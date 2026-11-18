package jumpingthomas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MenuPrincipal extends JPanel {

    public MenuPrincipal(Runnable lancerPartie) {
        setPreferredSize(new Dimension(Game.LARGEUR, Game.HAUTEUR));
        setBackground(new Color(30, 30, 60));                // fond bleu nuit (brouillon)
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));    // les éléments s'empilent de haut en bas

        JLabel titre = new JLabel("JUMPING THOMAS");
        titre.setFont(new Font("Arial", Font.BOLD, 40));
        titre.setForeground(Color.ORANGE);
        titre.setAlignmentX(CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(150));   // espace vide de 150 pixels
        add(titre);
        add(Box.createVerticalStrut(100));
        add(creerBouton("Lancer une partie", e -> lancerPartie.run()));
        add(Box.createVerticalStrut(20));
        add(creerBouton("Paramètres", e -> bientot()));
        add(Box.createVerticalStrut(20));
        add(creerBouton("Personnalisation", e -> bientot()));
        add(Box.createVerticalStrut(20));
        add(creerBouton("Quitter", e -> System.exit(0)));
    }

    // Fabrique un bouton : texte + action à faire quand on clique
    public static JButton creerBouton(String texte, ActionListener action) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Arial", Font.BOLD, 22));
        bouton.setAlignmentX(CENTER_ALIGNMENT);
        bouton.setMaximumSize(new Dimension(300, 60));
        bouton.setFocusable(false);   // sinon le bouton "vole" le clavier au jeu
        bouton.addActionListener(action);
        return bouton;
    }

    private void bientot() {
        JOptionPane.showMessageDialog(this, "Bientôt disponible !");
    }
}