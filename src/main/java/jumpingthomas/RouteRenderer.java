package jumpingthomas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
//import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * UNIQUEMENT le decor : espace, piste arc-en-ciel, trois voies, enseignes.
 * Ne depend ni du cycliste, ni des obstacles, ni du clavier.
 * Java 21 / Swing, sans bibliotheque externe.
 */
public final class RouteRenderer {
    private final int largeur;
    private final int hauteur;
    private final int horizon;
    private final double centre;
    private final BufferedImage espace;
    private final BufferedImage[] canettes = new BufferedImage[3];

    private double avance = 0.0;

    public RouteRenderer(int largeur, int hauteur, int horizon) {
        if (largeur <= 0 || hauteur <= 0 || horizon <= 0 || horizon >= hauteur) {
            throw new IllegalArgumentException("Dimensions de route incorrectes");
        }
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.horizon = horizon;
        this.centre = largeur / 2.0;

        espace = charger("/images/espace.jpg");
        canettes[0] = charger("/images/maximator.png");
        canettes[1] = charger("/images/mega_demon.png");
        canettes[2] = charger("/images/navigator.png");
    }

    /** Appele UNE fois par frame depuis le Timer existant de Game. */
    public void mettreAJour(double vitesse) {
        // La vitesse du jeu accelere l'animation mais pas sa geometrie.
        double pas = 0.004 + Math.max(0.0, Math.min(vitesse, 12.0)) * 0.0028;
        avance += pas;
        if (avance >= 10000.0) avance -= 10000.0;
    }

    public void reinitialiser() { avance = 0.0; }

    /** Route et decorations d'arriere-plan, AVANT obstacles et cycliste. */
    public void dessiner(Graphics g, Component observer) {
        Graphics2D d = (Graphics2D) g.create();
        d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        dessinerUnivers(d, observer);
        dessinerPiste(d);
        dessinerGrille(d);
        dessinerRefletsDePiste(d);
        dessinerLignesVoies(d);
        dessinerBordures(d);
        dessinerEtoilesBord(d);
        dessinerCanettes(d, observer);
        dessinerGrandesEnseignes(d, observer);
        d.dispose();
    }

    private void dessinerUnivers(Graphics2D d, Component observer) {
        d.setColor(new Color(7, 4, 25));
        d.fillRect(0, 0, largeur, hauteur);
        if (espace != null) {
            d.drawImage(espace, 0, 0, largeur, horizon + 55, observer);
        }
        // Vide spatial, teinte violette vers les bords de la route.
        d.setPaint(new GradientPaint(0, horizon - 10, new Color(35, 13, 73),
                                     0, hauteur, new Color(18, 6, 41)));
        d.fillRect(0, horizon, largeur, hauteur - horizon);
        // Brumes de nebuleuse, legeres et transparentes.
        d.setPaint(new RadialGradientPaint(
            new Point2D.Float(largeur * .02f, horizon + 90f), largeur * .56f,
            new float[]{0f, 1f},
            new Color[]{new Color(105, 33, 191, 100), new Color(105, 33, 191, 0)}));
        d.fillRect(0, horizon, largeur, hauteur - horizon);
        d.setPaint(new RadialGradientPaint(
            new Point2D.Float(largeur * .95f, horizon + 120f), largeur * .65f,
            new float[]{0f, 1f},
            new Color[]{new Color(38, 95, 226, 86), new Color(38, 95, 226, 0)}));
        d.fillRect(0, horizon, largeur, hauteur - horizon);

        // Le vide spatial existe aussi sur les deux cotes de la piste.
        for (int i = 0; i < 170; i++) {
            int x = (i * 97 + 37) % largeur;
            int y = horizon + (i * 131 + 19) % (hauteur - horizon);
            int taille = 1 + (i % 13 == 0 ? 1 : 0);
            d.setColor(new Color(204, 184, 255, 35 + (i % 4) * 15));
            d.fillOval(x, y, taille, taille);
        }
    }

    /** La piste est parfaitement DROITE. Sa base depasse legerement l'ecran.
     * A y=720, ses centres de voies sont x=75/225/375 (a l'arrondi), comme Game.java.
     */
    private double demiLargeur(double y) {
        double t = borner((y - horizon) / (hauteur - (double) horizon));
        return 18.0 + (262.0 * largeur / 450.0 - 18.0) * t;
    }

    private int xVoie(double fraction, double y) {
        double demi = demiLargeur(y);
        return (int) Math.round(centre - demi + fraction * demi * 2.0);
    }

    private double yProfondeur(double t) {
        return horizon + (hauteur - horizon) * Math.pow(borner(t), 1.55);
    }

    private static double borner(double v) { return Math.max(0.0, Math.min(1.0, v)); }
    private static double fract(double v) { return v - Math.floor(v); }

    private void dessinerPiste(Graphics2D d) {
        d.setColor(new Color(255, 56, 196, 65));
        d.setStroke(new BasicStroke(18, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        d.drawLine(xVoie(0, horizon), horizon, xVoie(0, hauteur), hauteur);
        d.drawLine(xVoie(1, horizon), horizon, xVoie(1, hauteur), hauteur);
        Polygon piste = new Polygon();
        piste.addPoint(xVoie(0, horizon), horizon);
        piste.addPoint(xVoie(1, horizon), horizon);
        piste.addPoint(xVoie(1, hauteur), hauteur);
        piste.addPoint(xVoie(0, hauteur), hauteur);
        d.setColor(new Color(23, 10, 75));
        d.fillPolygon(piste);
    }

    /** Dalles brillantes, organisees sur toute la largeur et vers l'horizon. */
    private void dessinerGrille(Graphics2D d) {
        Shape ancienne = d.getClip();
        Polygon masque = new Polygon();
        masque.addPoint(xVoie(0, horizon), horizon);
        masque.addPoint(xVoie(1, horizon), horizon);
        masque.addPoint(xVoie(1, hauteur), hauteur);
        masque.addPoint(xVoie(0, hauteur), hauteur);
        d.clip(masque);

        final int lignes = 22;
        final int colonnes = 9;
        double decalage = fract(avance * 1.3);
        long cycle = (long) Math.floor(avance * 1.3);
        for (int i = -1; i <= lignes; i++) {
            double p1 = (i + decalage) / lignes;
            double p2 = (i + 1 + decalage) / lignes;
            if (p2 <= 0 || p1 >= 1) continue;
            double y1 = yProfondeur(p1);
            double y2 = yProfondeur(p2);
            int yHaut = (int) Math.round(y1);
            int yBas = (int) Math.ceil(y2) + 1;
            if (yBas <= yHaut) continue;
            for (int j = 0; j < colonnes; j++) {
                double u1 = j / (double) colonnes;
                double u2 = (j + 1.0) / colonnes;
                Polygon dalle = new Polygon();
                dalle.addPoint(xVoie(u1, yHaut), yHaut);
                dalle.addPoint(xVoie(u2, yHaut), yHaut);
                dalle.addPoint(xVoie(u2, yBas), yBas);
                dalle.addPoint(xVoie(u1, yBas), yBas);

                // Harmonie arc-en-ciel : variation par colonne ET par rang de dalles.
                float teinte = (float) fract(0.86 - 0.030 * (i + cycle)
                         + 0.069 * j + 0.032 * Math.sin((i + cycle) * 0.31));
                Color vive = Color.getHSBColor(teinte, 0.80f, 1.0f);
                Color claire = eclaircir(vive, 0.25);
                Color sombre = assombrir(vive, 0.14);
                d.setPaint(new GradientPaint(0, yHaut, claire, 0, yBas, sombre));
                d.fillPolygon(dalle);

                // Jonctions minerales tres douces pour obtenir l'effet dalle brillante.
                d.setColor(new Color(255, 255, 255, 46));
                d.setStroke(new BasicStroke(0.7f));
                d.drawLine(xVoie(u1, yHaut), yHaut, xVoie(u2, yHaut), yHaut);
                d.setColor(new Color(255, 255, 255, 20));
                d.drawLine(xVoie(u1, yHaut), yHaut, xVoie(u1, yBas), yBas);
            }
        }
        // Reflets brillants transparents, en bandes longitudinales.
        for (int j = 1; j <= 17; j++) {
            double u = j / 18.0;
            d.setStroke(new BasicStroke(j % 3 == 0 ? 2.0f : 1.0f));
            d.setColor(new Color(245, 255, 255, j % 3 == 0 ? 29 : 13));
            d.drawLine(xVoie(u, horizon), horizon, xVoie(u, hauteur), hauteur);
        }
        d.setClip(ancienne);
    }

    // Petits reflets et eclats sur la piste (pas de collision : pure decoration).
    private void dessinerRefletsDePiste(Graphics2D d) {
        for (int i = 0; i < 24; i++) {
            double profondeur = .13 + fract(i * .381966) * .86;
            int y = (int)yProfondeur(profondeur);
            double u = .08 + fract(i * .618034) * .84;
            int x = xVoie(u, y);
            int r = Math.max(1, (int)(1 + 3 * profondeur));
            d.setColor(new Color(255, 255, 255, 60 + i % 5 * 20));
            d.fillOval(x - r, y - r, r * 2, r * 2);
            if (i % 6 == 0) {
                d.setColor(new Color(255, 255, 255, 96));
                d.setStroke(new BasicStroke(1.2f));
                d.drawLine(x-3*r, y, x+3*r, y);
                d.drawLine(x, y-3*r, x, y+3*r);
            }
        }
    }

    private void dessinerLignesVoies(Graphics2D d) {
        // Exactement deux lignes = trois voies.
        for (int l = 1; l <= 2; l++) {
            double u = l / 3.0;
            int xHaut = xVoie(u, horizon);
            int xBas = xVoie(u, hauteur);
            traitBrillant(d, xHaut, horizon, xBas, hauteur,
                    new Color(120, 235, 255), 12f, 5.5f, 2.6f);
        }
    }

    private void dessinerBordures(Graphics2D d) {
        for (int cote = 0; cote <= 1; cote++) {
            int xHaut = xVoie(cote, horizon);
            int xBas = xVoie(cote, hauteur);
            traitBrillant(d, xHaut, horizon, xBas, hauteur,
                    new Color(255, 57, 184), 18f, 8f, 3.5f);
            d.setStroke(new BasicStroke(2.3f));
            d.setColor(new Color(255, 238, 157));
            d.drawLine(xHaut, horizon, xBas, hauteur);
        }
    }

    private void traitBrillant(Graphics2D d, int x1, int y1, int x2, int y2,
                               Color halo, float grand, float moyen, float fin) {
        d.setStroke(new BasicStroke(grand, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        d.setColor(new Color(halo.getRed(), halo.getGreen(), halo.getBlue(), 45));
        d.drawLine(x1, y1, x2, y2);
        d.setStroke(new BasicStroke(moyen, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        d.setColor(new Color(halo.getRed(), halo.getGreen(), halo.getBlue(), 130));
        d.drawLine(x1, y1, x2, y2);
        d.setStroke(new BasicStroke(fin, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        d.setColor(new Color(250, 255, 255));
        d.drawLine(x1, y1, x2, y2);
    }

    private void dessinerEtoilesBord(Graphics2D d) {
        for (int i = 0; i < 10; i++) {
            double t = fract((i + fract(avance * 1.3)) / 10.0);
            if (t < .05 || t > .98) continue;
            int y = (int) yProfondeur(t);
            int taille = 2 + (int) Math.round(13 * t);
            for (int cote = 0; cote < 2; cote++) {
                int x = xVoie(cote, y) + (cote == 0 ? -7 - taille : 7 + taille);
                // Petite etoile doree lumineuse, en dehors de la route.
                Polygon etoile = polygoneEtoile(x, y - taille, taille);
                d.setColor(new Color(255, 155, 45, 68));
                d.fillOval(x - taille - 5, y - 2 * taille - 5,
                           taille * 2 + 10, taille * 2 + 10);
                d.setColor(new Color(255, 238, 128));
                d.fillPolygon(etoile);
                d.setColor(new Color(255, 251, 221));
                d.setStroke(new BasicStroke(1.4f));
                d.drawPolygon(etoile);
            }
        }
    }

    private Polygon polygoneEtoile(int cx, int cy, int rayon) {
        Polygon p = new Polygon();
        for (int i = 0; i < 10; i++) {
            double angle = -Math.PI / 2 + i * Math.PI / 5;
            double r = i % 2 == 0 ? rayon : rayon * .42;
            p.addPoint((int) (cx + r * Math.cos(angle)),
                       (int) (cy + r * Math.sin(angle)));
        }
        return p;
    }

    private void dessinerCanettes(Graphics2D d, Component observer) {
        // Panneaux AMSTERDAM sur les COTES, jamais sur la route.
        for (int i = 0; i < 8; i++) {
            double phase = fract(avance * .32 + i / 8.0);
            if (phase < .035 || phase > .84) continue;
            int y = (int) yProfondeur(phase);
            int h = Math.max(12, (int) (20 + 120 * phase * phase));
            int w = Math.max(6, (int) (h * .40));
            int marge = 6 + (int) (10 * phase);
            for (int cote = 0; cote < 2; cote++) {
                int x = cote == 0 ? xVoie(0.0, y) - marge - w : xVoie(1.0, y) + marge;
                if (x + w < -5 || x > largeur + 5) continue;
                int type = (i + cote) % canettes.length;
                BufferedImage sprite = canettes[type];
                if (sprite == null) continue;
                // Halo/pied stylises, puis canette de la serie alternee.
                d.setPaint(new RadialGradientPaint(new Point2D.Float(x + w/2f, y - h*.35f),
                        Math.max(10, h * .62f), new float[]{0f, 1f},
                        new Color[]{new Color(175, 105, 255, 116), new Color(175, 105, 255, 0)}));
                d.fillOval(x - w/2, y - h, w*2, h*2);
                d.setStroke(new BasicStroke(Math.max(1f, h / 80f)));
                d.setColor(new Color(143, 219, 255, 190));
                d.drawLine(x + w/2, y + h/2, x + w/2, y + h + Math.max(5,h/3));
                d.drawImage(sprite, x, y - h/2, w, h, observer);
            }
        }
    }

    // Deux grandes enseignes en avant-plan de l'espace, comme sur la reference.
    // Elles restent en place ; les autres canettes avancent le long des cotes.
    private void dessinerGrandesEnseignes(Graphics2D d, Component observer) {
        double echelle = largeur / 450.0;
        int h = (int)(154 * echelle);
        int w = (int)(66 * echelle);
        int y = (int)(80 * echelle);
        dessinerGrandeEnseigne(d, observer, canettes[0], (int)(8 * echelle), y, w, h);
        dessinerGrandeEnseigne(d, observer, canettes[2], largeur - w - (int)(8 * echelle), y, w, h);
    }

    private void dessinerGrandeEnseigne(Graphics2D d, Component observer,
                                        BufferedImage image, int x, int y, int w, int h) {
        if (image == null) return;
        float rayon = Math.max(10, w * 1.12f);
        d.setPaint(new RadialGradientPaint(new Point2D.Float(x + w/2f, y + h*.52f),
                rayon, new float[]{0f, 1f},
                new Color[]{new Color(174, 119, 255, 100), new Color(174, 119, 255, 0)}));
        d.fillOval(x - w/2, y, w*2, h);
        d.setStroke(new BasicStroke(2.4f));
        d.setColor(new Color(189, 156, 255, 210));
        d.drawLine(x + w/2, y + h - 15, x + w/2, horizon + 27);
        d.drawImage(image, x, y, w, h, observer);
    }

    private static Color eclaircir(Color a, double f) {
        return new Color((int)(a.getRed()*(1-f)+255*f),
                (int)(a.getGreen()*(1-f)+255*f),
                (int)(a.getBlue()*(1-f)+255*f));
    }
    private static Color assombrir(Color a, double f) {
        return new Color((int)(a.getRed()*(1-f)),
                (int)(a.getGreen()*(1-f)),
                (int)(a.getBlue()*(1-f)));
    }
    private static BufferedImage charger(String resource) {
        try (InputStream in = RouteRenderer.class.getResourceAsStream(resource)) {
            if (in == null) {
                System.err.println("[RouteRenderer] Image absente : " + resource);
                return null;
            }
            return ImageIO.read(in);
        } catch (IOException e) {
            System.err.println("[RouteRenderer] Erreur image " + resource + " : " + e.getMessage());
            return null;
        }
    }
}
