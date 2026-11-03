# Backlog
## Idée générale : TOMA DASH ; JUMPING THOM'ASS
- Un jeu de course vu de dos, en fausse 3D : le personnage court sur une route à 3 couloirs et doit éviter des obstacles générés aléatoirement.

  __HC: automatiquement?__

  > **Réponse équipe :** oui, le personnage avance tout seul. Le joueur contrôle seulement les esquives : changer de couloir (← →) et sauter (espace).

- Le niveau est infini, le score augmente avec la distance parcourue
- Toucher un obstacle = game over
- La vitesse augmente au fil de la partie
- Des boss au bout d'un certain temps
- Des bonus Etoile mario / maximator

## 1er sprint 3 semaines :
Objectif : une première version jouable : la route défile, le personnage esquive les obstacles, et la partie s'arrête au premier obstacle touché. Graphismes simples (rectangles de couleur).

**US1 - Route qui défile**
En tant que joueur, je veux voir une route à 3 couloirs défiler vers moi pour avoir la sensation d'avancer.

**US2 - Changer de couloir**
En tant que joueur, je veux changer de couloir avec ← et → pour contourner un obstacle.

**US3 - Sauter**
En tant que joueur, je veux sauter avec espace pour passer au-dessus d'un obstacle bas.

**US4 - Obstacles aléatoires**
En tant que joueur, je veux que des obstacles arrivent dans des couloirs au hasard pour que chaque partie soit différente. Il reste toujours au moins un passage libre.

**US5 - Score et game over**
En tant que joueur, je veux voir mon score pendant la partie, et un écran game over quand je touche un obstacle, avec la possibilité de rejouer.

  __HC: quelle différence entre 2ème et 3ème item?__

  > **Réponse équipe :** l'ancien 2ème item parlait du joueur (le saut), le 3ème du jeu (les obstacles). C'est maintenant séparé : US2 et US3 sont les contrôles du joueur, US4 la génération des obstacles.

  __HC: il se passe quoi si je saute pas?__

  > **Réponse équipe :** le personnage touche l'obstacle et c'est game over (US5).

  __HC: il y a des trous ou juste des obstacles?__

  > **Réponse équipe :** juste des obstacles au sprint 1. Les trous arrivent au sprint 2.

  __HC: on pourrait être plus ambitieux pour un travail à 5 pendant 3 semaines__

  > **Réponse équipe :** le sprint 1 passe en fausse 3D avec 3 couloirs, et intègre le score, la vitesse progressive et le game over.

### Répartition des rôles

| Rôle | Membre | Stories |
|------|--------|---------|
| Moteur de jeu (fenêtre, boucle, clavier, perspective) | _à remplir_ | socle commun |
| Route | _à remplir_ | US1 |
| Personnage | _à remplir_ | US2, US3 |
| Obstacles | _à remplir_ | US4 |
| Score et game over | _à remplir_ | US5 |

### Planning
- **Semaine 1 :** le moteur de jeu est prêt, chacun commence sa partie.
- **Semaine 2 :** on assemble : on peut courir, esquiver et voir arriver les obstacles.
- **Semaine 3 :** collisions, score, game over, corrections et démo.

## 2eme sprint 3 semaines :
- faire la partie graphique (sprites)
- ajouter des trous et la glissade (↓)
- faire un menu
- ajouter le boss

## 3eme sprint 3 semaines :
- ajout d'un shop
- différents personnages
- bonus en plus
## Test de ma branche
Premier commit sur la route vers Amsterdam.