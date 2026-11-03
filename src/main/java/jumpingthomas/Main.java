package jumpingthomas;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
    JFrame fenetre = new JFrame("Jumping Thomas");
    fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    fenetre.add(new Game());
    fenetre.pack();

    fenetre.setVisible(true);
    }
}