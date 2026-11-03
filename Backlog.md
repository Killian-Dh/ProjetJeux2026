# Backlog
## Idée générale : TOMA DASH ; JUMPING THOM'ASS
- Un jeu de course vu de dos, en fausse 3D : le personnage court vers le fond de l'écran sur une route à 3 couloirs et doit éviter des obstacles et ennemis générés aléatoirement.

  __HC: automatiquement?__

  > **Réponse équipe :** le personnage avance tout seul vers l'avant (le joueur ne gère pas la vitesse). Le joueur contrôle uniquement les esquives : changer de couloir (← →), sauter (espace) et glisser (↓).

- Le niveau est infini, le score augmente avec la distance parcourue
- La vitesse augmente progressivement pour rendre la partie de plus en plus difficile
- Des boss au bout d'un certain temps
- Le personnage possède 3 vies
- Des bonus Etoile mario / maximator

## 1er sprint 3 semaines :
Objectif : une première version **jouable du début à la fin** : la route défile en perspective, le personnage esquive, les obstacles arrivent, on perd des vies et la partie se termine par un game over avec le score.

Les graphismes sont volontairement simples (rectangles et couleurs unies) : les vrais sprites arrivent au sprint 2.

### User stories

**US1 - Route en perspective**
En tant que joueur, je veux voir une route à 3 couloirs défiler vers moi pour avoir la sensation de courir vers l'avant.
- La route se rétrécit vers l'horizon, les bandes au sol défilent vers le bas de l'écran
- La vitesse de défilement augmente doucement au fil de la partie

**US2 - Changer de couloir**
En tant que joueur, je veux passer d'un couloir à l'autre avec les flèches ← et → pour esquiver un obstacle par le côté.
- Le personnage glisse rapidement vers le couloir voisin (pas de téléportation)
- Impossible de sortir de la route (rien ne se passe à gauche du couloir de gauche)

**US3 - Sauter et glisser**
En tant que joueur, je veux sauter avec espace et glisser avec ↓ pour passer au-dessus ou en dessous d'un obstacle.
- Le saut suit une gravité (montée puis descente), on ne peut pas resauter en l'air
- La glissade dure un temps limité puis le personnage se relève

**US4 - Obstacles aléatoires**
En tant que joueur, je veux que des obstacles apparaissent au loin dans un couloir au hasard et grossissent en approchant, pour que chaque partie soit différente.
- 3 types d'obstacles : un bas (à sauter), un haut (à glisser dessous), un mur (à contourner en changeant de couloir)
- Il reste toujours au moins un passage possible (jamais 3 murs côte à côte)
- Les obstacles sont supprimés une fois passés derrière le joueur

**US5 - Vies, score et game over**
En tant que joueur, je veux perdre une vie quand je touche un obstacle et voir mon score, pour savoir si je progresse.
- Un obstacle touché fait perdre la partie. 
- Le score augmente avec la distance et s'affiche en haut de l'écran.
- Quand on meurt : écran game over avec le score final, et la touche Entrée relance une partie

  __HC: quelle différence entre 2ème et 3ème item?__

  > **Réponse équipe :** 

  __HC: il se passe quoi si je saute pas?__

  > **Réponse équipe :** le personnage touche l'obstacle et perd la partie. 

  __HC: il y a des trous ou juste des obstacles?__

  > **Réponse équipe :** juste des obstacles au sprint 1 (3 types, voir US4). Les trous sont ajoutés au sprint 2.

  __HC: on pourrait être plus ambitieux pour un travail à 5 pendant 3 semaines__

  > **Réponse équipe :** le sprint 1 passe en fausse 3D avec 3 couloirs, et intègre les vies, le score, la vitesse progressive et le game over, initialement prévus plus tard.

### Répartition des rôles

| Rôle | Membre | Stories | Fichiers |
|------|--------|---------|----------|
| Moteur de jeu & projection | _à remplir_ | socle commun | `GamePanel.java`, `Projection.java` |
| Route & décor | _à remplir_ | US1 | `Route.java` |
| Personnage | _à remplir_ | US2, US3 | `Player.java` |
| Obstacles | _à remplir_ | US4 | `Obstacle.java`, `ObstacleGenerator.java` |
| Règles du jeu | _à remplir_ | US5 | `Collision.java`, `GameState.java`, `Hud.java` |

`Projection.java` convertit une position dans le monde (couloir, distance, hauteur) en position et taille à l'écran. Tous les rôles l'utilisent : c'est la première chose à livrer, avec la boucle de jeu.

### Planning

- **Semaine 1 :** le moteur (fenêtre, boucle à 60 images/s, clavier, `Projection`) est livré en début de semaine. En parallèle, chacun prépare sa classe et ses tests avec des valeurs en dur.
- **Semaine 2 :** chacun branche sa partie sur le moteur. Fin de semaine : on peut courir, esquiver et voir arriver les obstacles.
- **Semaine 3 :** collisions, vies, game over, réglage de la difficulté, correction des bugs et démo.

### Définition de « terminé »

Une story est terminée quand :
- le code est sur `main` via une pull request relue par un autre membre
- le jeu compile et se lance sans erreur (`mvn package`)
- les critères de la story sont vérifiés en jouant
- l'issue GitHub correspondante est fermée

## 2eme sprint 3 semaines :
- faire la partie graphique (sprites du personnage, des obstacles et du décor)
- ajouter des trous dans la route
- faire un menu
- ajouter le boss

## 3eme sprint 3 semaines :
- ajout d'un shop
- différents personnages
- bonus en plus
