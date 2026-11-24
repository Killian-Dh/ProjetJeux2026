package jumpingthomas;
import javax.sound.sampled.*;
import java.io.*;

public class Son 
{
    private Clip clip;

    public Son(String chemin) 
    {
        try 
        {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(chemin).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } 
        catch (Exception e) 
        {
            System.out.println("Erreur lors du chargement du son : " + e.getMessage());
        }
    }

    public void jouer() 
    {
        if (clip != null) 
        {
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
        }
    }

    public void stop() 
    {
        if (clip != null && clip.isRunning()) 
        {
            clip.stop();
        }
    }

    public void boucle() 
    {
        if (clip != null) 
        {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void volume(float volume) {
        if (clip != null) {
            FloatControl controle = (FloatControl)
                clip.getControl(FloatControl.Type.MASTER_GAIN);

            // volume entre 0.0 et 1.0
            if (volume <= 0.0f) {
                controle.setValue(controle.getMinimum());
            } else {
                float dB = (float)(20 * Math.log10(volume));
                controle.setValue(Math.max(controle.getMinimum(),
                    Math.min(controle.getMaximum(), dB)));
            }
        }
    }
}
