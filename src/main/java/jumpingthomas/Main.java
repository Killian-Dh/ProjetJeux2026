package jumpingthomas;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
    JFrame fenetre = new JFrame("Jumping Thomas");
    fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Game moteur = new Game();
    characterController controller = new characterController(moteur);

    fenetre.add(moteur);
    fenetre.pack();

    fenetre.setVisible(true);
    }
}