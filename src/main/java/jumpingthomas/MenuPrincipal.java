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
    
    private JButton boutonCroixPerso;
    private JButton boutonPlus;
    private static ImageIcon dispBoutonPlus = new ImageIcon("flecheDroite.png");
    private JButton boutonMoins;
    private static ImageIcon dispBoutonMoins = new ImageIcon("flecheGauche.png");
    private JLabel titrePersonalisation;
    private static Image cases = new ImageIcon("cadre.png").getImage();
    private static Image style0 = new ImageIcon("velo.png").getImage();
    private static Image style1 = new ImageIcon("moto.png").getImage();
    
    private JButton boutonCroixParam;
    private JLabel texteVolSon;
    private JLabel texteVolMusique;
    private JLabel titreParametre;
    private JPanel sliderSon;
    private JPanel sliderMusique;

    private static Image fond = new ImageIcon("fondMenu.png").getImage();
    private static Image FondParametres = new ImageIcon("PageParametre.png").getImage();
    private static boolean afficherParametres = false;
    private static boolean afficherPersonalisation = false;

    public static int selectedType = 0; //Style séelectionné

    

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
        add(creerBouton("Quitter", e -> System.exit(0)));*/
        
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

        JButton boutonPerso = creerBouton("Personnalisation", e -> DispPersonalisation());
        boutonPerso.setBounds(boutonX, 510, boutonLarg, boutonHaut);
        add(boutonPerso);

        JButton boutonQuitter = creerBouton("Quitter", e -> System.exit(0));
        boutonQuitter.setBounds(boutonX, 590, boutonLarg, boutonHaut);
        add(boutonQuitter);

        Parametres();
        Personnalisation();
 
        
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

    private void DispPersonalisation()
    {
        afficherPersonalisation = !afficherPersonalisation; // clic = afficher / masquer
        for (Component c : getComponents()) {
        c.setVisible(!afficherPersonalisation);
        }
        boutonCroixPerso.setVisible(afficherPersonalisation);
        boutonPlus.setVisible(afficherPersonalisation);
        boutonMoins.setVisible(afficherPersonalisation);
        titrePersonalisation.setVisible(afficherPersonalisation);

        repaint();
    }

    private void Personnalisation()
    {
        boutonsPerso();
        textPersonalisation();
    }

    private void textPersonalisation()
    {
        titrePersonalisation = new JLabel("PERSONALISATION");
        titrePersonalisation.setFont(new Font("Arial", Font.BOLD, 27));
        titrePersonalisation.setForeground(Color.BLACK);
        titrePersonalisation.setBounds(Game.LARGEUR/2 -130, 350, 300, 30);
        titrePersonalisation.setVisible(false);
        add(titrePersonalisation);
    }

    private void boutonsPerso()
    {
        boutonCroixPerso = new JButton(dispBoutonCroix);
        boutonCroixPerso.setFocusable(false);
        boutonCroixPerso.setContentAreaFilled(false);
        boutonCroixPerso.setBorderPainted(false);
        boutonCroixPerso.addActionListener(e -> DispPersonalisation());
        boutonCroixPerso.setBounds(330, 665, 35, 35); 
        boutonCroixPerso.setVisible(false);
        add(boutonCroixPerso);

        boutonPlus = new JButton(dispBoutonPlus);
        boutonPlus.setFocusable(false);
        boutonPlus.setContentAreaFilled(false);
        boutonPlus.setBorderPainted(false);
        boutonPlus.addActionListener(e -> {
            if (selectedType < 1)
            {
                selectedType += 1;
            }
            repaint();
        });
        boutonPlus.setBounds(250, 565, 70, 70);//25
        boutonPlus.setVisible(false);
        add(boutonPlus);

        boutonMoins = new JButton(dispBoutonMoins);
        boutonMoins.setFocusable(false);
        boutonMoins.setContentAreaFilled(false);
        boutonMoins.setBorderPainted(false);
        boutonMoins.addActionListener(e -> {
            if (selectedType > 0)
            {
                selectedType -= 1;
            }
            repaint();
        });
        boutonMoins.setBounds(130, 565, 70, 70);
        boutonMoins.setVisible(false);
        add(boutonMoins);


    }


    private void DispParametres()
    {
        afficherParametres = !afficherParametres; // clic = afficher / masquer
        for (Component c : getComponents()) {
        c.setVisible(!afficherParametres);
        }
        boutonCroixParam.setVisible(afficherParametres);
        texteVolMusique.setVisible(afficherParametres);
        texteVolSon.setVisible(afficherParametres);
        titreParametre.setVisible(afficherParametres);
        sliderSon.setVisible(afficherParametres);
        sliderMusique.setVisible(afficherParametres);

        repaint();
    }

    private void Parametres()
    {
        textParametres();
        boutonsParam();
        
        sliderSon = SliderParametres(Game.volumeSon, v -> Game.volumeSon = v);
        sliderSon.setBounds(75, 430, 280,80);
        sliderSon.setVisible(false);
        add(sliderSon);

        sliderMusique = SliderParametres(Game.volumeMusique, v -> Game.volumeMusique = v);
        sliderMusique.setBounds(75, 505, 280,80);
        sliderMusique.setVisible(false);
        add(sliderMusique);
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
    private void boutonsParam()
    {
        boutonCroixParam = new JButton(dispBoutonCroix);
        boutonCroixParam.setFocusable(false);
        boutonCroixParam.setContentAreaFilled(false);
        boutonCroixParam.setBorderPainted(false);
        boutonCroixParam.addActionListener(e -> DispParametres());
        boutonCroixParam.setBounds(330, 620, 35, 35);
        boutonCroixParam.setVisible(false);
        add(boutonCroixParam);
    }

    private JPanel SliderParametres(double valeurInitiale, Consumer<Double> onChange)
    {
        JSlider slider = new JSlider(0, 100, (int) (valeurInitiale * 100));
        
        slider.setOpaque(false);

        slider.addChangeListener(e -> {
            double v = slider.getValue() / 100.0;
            
            onChange.accept(v); // prévient le jeu de la nouvelle valeur
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);   // sinon un fond gris cache l'image
        panel.add(slider, BorderLayout.CENTER);
        
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
            if (afficherPersonalisation) {
                int fondLarg = FondParametres.getWidth(this);
                int casesLarg = cases.getWidth(this);
                g.drawImage(FondParametres, (Game.LARGEUR - fondLarg)/2, 325, 325, 400, this);
                g.drawImage(cases, Game.LARGEUR/2 - casesLarg+25, 400, 150, 150, this);
                switch (selectedType)
                {
                    case 0:
                        g.drawImage(style0, Game.LARGEUR/2 - casesLarg/2, 415, 100, 100, this);
                        break;
                    case 1 :
                        g.drawImage(style1, Game.LARGEUR/2 -casesLarg/2, 415, 100, 100, this);
                        break;
                }
            }
                     
            
        }
    }
}