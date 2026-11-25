
package jumpingthomas;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class GenerateurPieces {

    private ArrayList<Coin> pieces = new ArrayList<>();
    private Random hasard = new Random();

    private double avantProchainePiece = 0;

    // Nombre de pièces réellement collectées
    private int nombrePiecesCollectees = 0;

    // Mise à jour des pièces à chaque image
    public void mettreAJour(double vitesse, int couloirJoueur) {

        // Faire avancer toutes les pièces
        for (Coin piece : pieces) {
            piece.avancer(vitesse);

            // Vérifier si le joueur récupère une pièce
            if (piece.toucher(couloirJoueur)) {
                nombrePiecesCollectees++;
            }
        }

        // Supprimer les pièces collectées ou dépassées
        pieces.removeIf(piece ->
            piece.estCollectee() || piece.getDistance() < -5
        );

        // Créer de nouvelles pièces
        avantProchainePiece -= vitesse;

        if (avantProchainePiece <= 0) {
            creerPiece();
            avantProchainePiece = 12 + hasard.nextInt(9);
        }
    }

    // Créer une pièce dans un couloir aléatoire
    private void creerPiece() {

        int couloir = hasard.nextInt(3) - 1;

        pieces.add(new Coin(couloir));
    }

    // Dessiner toutes les pièces
    public void dessiner(Graphics g) {

        for (int i = pieces.size() - 1; i >= 0; i--) {
            pieces.get(i).dessiner(g);
        }
    }

    // Récupérer le nombre de pièces collectées
    public int getNombrePiecesCollectees() {
        return nombrePiecesCollectees;
    }

    // Réinitialiser pour une nouvelle partie
    public void reinitialiser() {

        pieces.clear();
        nombrePiecesCollectees = 0;
        avantProchainePiece = 0;
    }
}