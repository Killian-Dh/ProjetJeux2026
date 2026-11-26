
package jumpingthomas;
import java.util.Collections;
import java.util.List;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class GenerateurPieces {

    private ArrayList<Coin> pieces = new ArrayList<>();
    private Random hasard = new Random();

    private double avantProchainePiece = 0;

    // Nombre de pièces réellement collectées
    private int nombrePiecesCollectees = 0;
    public static int stockPiece = 0;
    public boolean pieceTouchee = false;

    // Mise à jour des pièces à chaque image
    public void mettreAJour(double vitesse, int couloirJoueur, GenerateurObstacles obstacles) {

    for (Coin piece : pieces) {
        piece.avancer(vitesse);
        if (piece.toucher(couloirJoueur)) {
            nombrePiecesCollectees++;
        }
        

    }

    pieces.removeIf(piece -> piece.estCollectee() || piece.getDistance() < -5);

    // supprime les pièces qu'une nouvelle vague vient de recouvrir
    pieces.removeIf(piece ->
        piece.getDistance() > Coin.distanceDepart - 20
        && obstacles.obstacleProche(piece.getCouloir(), piece.getDistance(), 15)
    );

    avantProchainePiece -= vitesse;
    if (avantProchainePiece <= 0) {
        creerPiece(obstacles);
        avantProchainePiece = 12 + hasard.nextInt(9);
    }
}

    // Créer une pièce dans un couloir aléatoire
    private void creerPiece(GenerateurObstacles obstacles) {
    List<Integer> couloirs = new ArrayList<>(List.of(-1, 0, 1));
    Collections.shuffle(couloirs, hasard);

    for (int couloir : couloirs) {
        if (!obstacles.obstacleProche(couloir, Coin.distanceDepart, 15)) {
            pieces.add(new Coin(couloir));
            return;
        }
    }
}

    public void dessinerEntre(Graphics g, double loin, double proche) {
        for (Coin piece : pieces) {
            if (piece.getDistance() > proche && piece.getDistance() <= loin) {
                piece.dessiner(g);
            }
        }
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

    public ArrayList<Coin> getPieces() {
    return pieces;
}

    
}