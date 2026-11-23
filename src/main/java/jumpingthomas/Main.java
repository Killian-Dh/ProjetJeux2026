package jumpingthomas;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

    public static void main(String[] args) {
        JFrame fenetre = new JFrame("Jumping Thomas");
        fenetre.setResizable(false); // Empêche le redimensionnement
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Game moteur = new Game();
        //characterController controller = new characterController(moteur);

        // Le "paquet de cartes" : la fenêtre affiche une seule carte à la fois
        CardLayout cartes = new CardLayout();
        JPanel conteneur = new JPanel(cartes);

        MenuPrincipal menu = new MenuPrincipal(() -> {
            cartes.show(conteneur, "jeu");    // on montre la carte du jeu
            moteur.nouvellePartie();          // on lance une partie
            moteur.requestFocusInWindow();    // le clavier va au jeu
        });

                // Bouton "Menu" du game over : on remontre la carte du menu
        moteur.setRetourMenu(() -> cartes.show(conteneur, "menu"));

        conteneur.add(menu, "menu");     // la première carte ajoutée est affichée au départ
        conteneur.add(moteur, "jeu");

        fenetre.add(conteneur);
        fenetre.pack();
        fenetre.setVisible(true);
    }
}