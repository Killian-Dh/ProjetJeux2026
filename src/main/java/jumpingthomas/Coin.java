
package jumpingthomas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Coin {

    private int couloir;
    private double distance;
    private double distancePrecedente;
    private static final Image[] PIECE = chargerAnimation("pixelart_pieces.png", 8);
    private int age = 0;   // nombre d'images depuis la création de l'obstacle (pour l'animation)

    public static double distanceDepart = 100;

    public int getCouloir() { return couloir; }

    private boolean collectee;

    public Coin(int couloir) {
        this.couloir = couloir;
        this.distance = distanceDepart;
        this.distancePrecedente = distanceDepart;
        this.collectee = false;
    }

    // Faire avancer la pièce vers le joueur
    public void avancer(double vitesse) {
        distancePrecedente = distance;
        distance -= vitesse;
        age++;
    }

    // Dessiner la pièce
    public void dessiner(Graphics g) {

        if (collectee) {
            return;
        }

        double e = Perspective.echelle(distance);
        int largeur = (int) (1.5 * Game.TAILLE * e);
        int ySol = Perspective.ySol(distance);
        int x = Perspective.x(couloir, distance);
        

        if (PIECE.length > 0) {
            
            Image img = PIECE[(age / 8) % PIECE.length];   // change d'image toutes les 8 images, en boucle
            int hauteur = largeur * img.getHeight(null) / img.getWidth(null);
            int basPiece = ySol - (int) (Game.TAILLE * e);  
            g.drawImage(img, x - largeur / 2 + 9, basPiece, largeur/2, hauteur/2, null);
        }

        /*g.setColor(new Color(255, 190, 0));

        g.fillOval(
            x - taille / 2,
            y - taille / 2,
            taille,
            taille
        );

        g.setColor(Color.ORANGE);

        g.drawOval(
            x - taille / 2,
            y - taille / 2,
            taille,
            taille
        );*/
    }

    // Vérifier si le joueur récupère la pièce
    public boolean toucher(int couloirJoueur) {

        boolean memeCouloir = couloir == couloirJoueur;

        boolean franchitLeJoueur =
            distancePrecedente > 0 && distance <= 0;

        if (memeCouloir && franchitLeJoueur && !collectee) {
            collectee = true;
            return true;
        }

        return false;
    }

    public double getDistance() {
        return distance;
    }

    public boolean estCollectee() {
        return collectee;
    }

    // Découpe une spritesheet (images côte à côte sur une ligne) en images séparées
    private static Image[] chargerAnimation(String fichier, int nombre) {
        try {
            BufferedImage feuille = ImageIO.read(new File(fichier));
            int l = feuille.getWidth() / nombre;
            Image[] images = new Image[nombre];
            for (int i = 0; i < nombre; i++) {
                images[i] = feuille.getSubimage(i * l, 0, l, feuille.getHeight());
            }
            return images;
        } catch (Exception ex) {
            System.out.println("Image introuvable : " + fichier);
            return new Image[0];   // tableau vide : on dessinera la banderole à la place
        }
    }
}

