package jumpingthomas;

import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.JComponent;



import java.awt.*;



public class MenuPrincipal extends JPanel {

    private static ImageIcon dispBouton = new ImageIcon("ButtonType.png");
    private static ImageIcon dispBoutonCroix = new ImageIcon("Croix.png");
    private JButton boutonCroix;
    private JLabel texteVolSon;
    private JLabel texteVolMusique;
    private JLabel titreParametre;

    private static Image fond = new ImageIcon("fondMenu.png").getImage();
    private static Image FondParametres = new ImageIcon("PageParametre.png").getImage();
    private static boolean afficherParametres = false;

    

    public MenuPrincipal(Runnable lancerPartie) {
        setPreferredSize(new Dimension(Game.LARGEUR, Game.HAUTEUR));
        setBackground(new Color(30, 30, 60));                // fond bleu nuit (brouillon)
        
        /*setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));    // les éléments s'empilent de haut en bas

        //JLabel titre = new JLabel("JUMPING THOMAS");
        //titre.setFont(new Font("Arial", Font.BOLD, 40));
        //titre.setForeground(Color.ORANGE);
        //titre.setAlignmentX(CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(150));   // espace vide de 150 pixels
        //add(titre);
        add(Box.createVerticalStrut(200));
        add(creerBouton("Lancer une partie", e -> lancerPartie.run()));
        add(Box.createVerticalStrut(20));
        add(creerBouton("Paramètres", e -> DispParametres()));
        add(Box.createVerticalStrut(20));
        add(creerBouton("Personnalisation", e -> bientot()));
        add(Box.createVerticalStrut(20));
        add(creerBouton("Quitter", e -> System.exit(0)));
        */
       setLayout(null); // à la place du BoxLayout

        int boutonLarg = 300;
        int boutonHaut = 60;                 // taille des boutons
        int boutonX = (Game.LARGEUR - boutonLarg) / 2;      // centré horizontalement

        JButton boutonLancer = creerBouton("Lancer une partie", e -> lancerPartie.run());
        boutonLancer.setBounds(boutonX, 350, boutonLarg, boutonHaut);
        add(boutonLancer);

        JButton boutonParametres = creerBouton("Paramètres", e -> DispParametres());
        boutonParametres.setBounds(boutonX, 430, boutonLarg, boutonHaut);
        add(boutonParametres);

        JButton boutonPerso = creerBouton("Personnalisation", e -> bientot());
        boutonPerso.setBounds(boutonX, 510, boutonLarg, boutonHaut);
        add(boutonPerso);

        JButton boutonQuitter = creerBouton("Quitter", e -> System.exit(0));
        boutonQuitter.setBounds(boutonX, 590, boutonLarg, boutonHaut);
        add(boutonQuitter);

        Parametres();
 
        
    }

    // Fabrique un bouton : texte + action à faire quand on clique
    public static JButton creerBouton(String texte, ActionListener action) {
        JButton bouton = new JButton(texte, dispBouton);
       //JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Arial", Font.BOLD, 22));
        bouton.setAlignmentX(CENTER_ALIGNMENT);
        bouton.setMaximumSize(new Dimension(300, 60));
        bouton.setFocusable(false);   // sinon le bouton "vole" le clavier au jeu
        
        // Texte centré sur l'image
        bouton.setHorizontalTextPosition(SwingConstants.CENTER);
        bouton.setVerticalTextPosition(SwingConstants.CENTER);

        // Supprime le fond et la bordure classiques
        bouton.setContentAreaFilled(false); //retire le fond du bouton
        bouton.setBorderPainted(false);//retire la bordure du bouton
        //bouton.setFocusPainted(false);//retire le contour du bouton quand il est sélectionné
        bouton.addActionListener(action);
        
        return bouton;
    }

    private void bientot() {
        JOptionPane.showMessageDialog(this, "Bientôt disponible !");
    }


    
    private void DispParametres()
    {
        
        afficherParametres = !afficherParametres; // clic = afficher / masquer
        for (Component c : getComponents()) {
        c.setVisible(!afficherParametres);
        }
        boutonCroix.setVisible(afficherParametres);
        texteVolMusique.setVisible(afficherParametres);
        texteVolSon.setVisible(afficherParametres);
        titreParametre.setVisible(afficherParametres);

        JLabel VolumeSon = new JLabel("Volume Sons:");
        VolumeSon.setFont(new Font("Arial", Font.BOLD, 20));
        VolumeSon.setBounds(300,300,200,30);
        JComponent sliderSon = SliderParametres("Volume du son", Game.volumeSon, v -> Game.volumeSon = v);
        JComponent sliderMusique = SliderParametres("Volume de la musique", Game.volumeMusique, v -> Game.volumeMusique = v);
        repaint();
    }
    private void Parametres()
    {
        textParametres();
        boutonCroix();


    }

    private void textParametres()
    {
        texteVolSon = new JLabel("Volume du son :");
        texteVolSon.setFont(new Font("Arial", Font.BOLD, 20));
        texteVolSon.setForeground(Color.BLACK);
        texteVolSon.setBounds(80, 425, 200, 30);
        texteVolSon.setVisible(false);
        add(texteVolSon);

        texteVolMusique = new JLabel("Volume du musique :");
        texteVolMusique.setFont(new Font("Arial", Font.BOLD, 20));
        texteVolMusique.setForeground(Color.BLACK);
        texteVolMusique.setBounds(80, 500, 200, 30);
        texteVolMusique.setVisible(false);
        add(texteVolMusique);

        titreParametre = new JLabel("PARAMETRES");
        titreParametre.setFont(new Font("Arial", Font.BOLD, 30));
        titreParametre.setForeground(Color.BLACK);
        titreParametre.setBounds(Game.LARGEUR/2 -110, 350, 300, 30);
        titreParametre.setVisible(false);
        add(titreParametre);
    }
    private void boutonCroix()
    {
        boutonCroix = new JButton(dispBoutonCroix);
        boutonCroix.setFocusable(false);
        boutonCroix.setContentAreaFilled(false);
        boutonCroix.setBorderPainted(false);
        boutonCroix.addActionListener(e -> DispParametres());
        boutonCroix.setBounds(330, 620, 35, 35);
        boutonCroix.setVisible(false);
        add(boutonCroix);
    }

    private JPanel SliderParametres(String nom, double valeurInitiale, Consumer<Double> onChange)
    {
        JSlider slider = new JSlider(0, 100, (int) (valeurInitiale * 100));
        JLabel label = new JLabel(String.format("%s :", nom));
        slider.setOpaque(false);

        slider.addChangeListener(e -> {
            double v = slider.getValue() / 100.0;
            label.setText(String.format("%s :", nom));
            onChange.accept(v); // prévient le jeu de la nouvelle valeur
        });

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(slider);
        return panel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (fond != null) {
            g.drawImage(fond, 0, 0, Game.LARGEUR, Game.HAUTEUR, this);
            if (afficherParametres) {
                int imageLarg = FondParametres.getWidth(this);
                g.drawImage(FondParametres, (Game.LARGEUR - imageLarg)/2, 325, 325, 350, this);
                
            }
                     
            
        }
    }
}