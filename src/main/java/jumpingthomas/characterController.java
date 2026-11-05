package jumpingthomas;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class characterController 
{
    //Player Controller/////////////////////////////////////////
    @addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                couloir = couloir - 1;
                
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                couloir = couloir + 1;
            }
        }
        ////////////////////////////////////////////////
});

}
