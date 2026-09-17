package jumpingthomas;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Main {

    public static void main(String[] args) {
        JFrame fenetre = new JFrame("Jumping Thomas");
        fenetre.setSize(800, 400);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel texte = new JLabel("Jumping Thomas - v0.1.0", SwingConstants.CENTER);
        texte.setFont(new Font("Arial", Font.BOLD, 28));
        fenetre.add(texte);

        fenetre.setVisible(true);
    }
}