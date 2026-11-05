package jumpingthomas;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class characterController {

    Game moteur = new Game();

    public characterController() {

        moteur.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moteur.allerGauche = true;
                }

                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moteur.allerDroite = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moteur.allerGauche = false;
                }

                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moteur.allerDroite = false;
                }
            }
        });
    }
}