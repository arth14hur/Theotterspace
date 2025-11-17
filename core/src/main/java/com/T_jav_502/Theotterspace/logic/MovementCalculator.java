package com.T_jav_502.Theotterspace.logic;

import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Classe utilitaire responsable du calcul des zones de déplacement et d'attaque.
 * Elle permet de sortir la logique de pathfinding/floodfill des classes d'entités (Unit, Map).
 */
public class MovementCalculator {

    /**
     * Calcule les tuiles accessibles pour une unité donnée, que ce soit pour se déplacer ou attaquer.
     *
     * @param unit L'unité pour laquelle on calcule la portée.
     * @param layer La couche de tuiles de la carte (pour vérifier les collisions/walkable).
     * @param isAttackMode Si true, calcule la portée d'attaque (incluant la portée de l'arme). Sinon, calcule le mouvement.
     * @return Une liste de coordonnées (Vector2) accessibles.
     */
    public static Array<Vector2> getAccessibleTiles(aUnit unit, TiledMapTileLayer layer, boolean isAttackMode) {
        Array<Vector2> visitedTiles = new Array<>();
        // On commence par la position actuelle de l'unité
        visitedTiles.add(unit.getCoordinates().cpy());

        Array<Vector2> frontier = new Array<>(); // Les tuiles à explorer à cette itération
        Array<Vector2> nextFrontier = new Array<>(); // Les tuiles pour la prochaine itération

        // Directions : Haut, Bas, Gauche, Droite
        Array<Vector2> directions = new Array<>();
        directions.add(new Vector2(0, -1));
        directions.add(new Vector2(0, 1));
        directions.add(new Vector2(-1, 0));
        directions.add(new Vector2(1, 0));

        // Détermination des points de mouvement disponibles
        int movementPoints = 0;
        if (!unit.hasMoved()) {
            movementPoints = unit.getMovement();
        }

        // Si on calcule l'attaque, on ajoute la portée de l'arme aux points de mouvement
        // Note : Selon votre logique originale, si l'unité n'a pas attaqué, on ajoute la range.
        if (isAttackMode && !unit.hasAttacked()) {
            movementPoints += unit.getRange();
        }

        // Algorithme de Flood Fill (Largeur d'abord) limité par les points de mouvement
        // On initialise la frontière avec les tuiles déjà visitées (juste la position de départ au début)
        frontier.addAll(visitedTiles);

        for (int i = 0; i < movementPoints; i++) {
            for (Vector2 currentPos : frontier) {
                for (Vector2 direction : directions) {
                    float nextX = currentPos.x + direction.x;
                    float nextY = currentPos.y + direction.y;
                    Vector2 neighbor = new Vector2(nextX, nextY);

                    // Vérification des limites de la carte et si la tuile a déjà été visitée
                    if (nextX >= 0 && nextX < layer.getWidth() &&
                        nextY >= 0 && nextY < layer.getHeight() &&
                        !contains(visitedTiles, neighbor)) {

                        TiledMapTileLayer.Cell cell = layer.getCell((int) nextX, (int) nextY);

                        // Vérification si la tuile est "walkable"
                        if (cell != null && cell.getTile() != null) {
                            boolean isWalkable = true;
                            if (cell.getTile().getProperties().containsKey("walkable")) {
                                isWalkable = cell.getTile().getProperties().get("walkable", Boolean.class);
                            }

                            if (isWalkable) {
                                nextFrontier.add(neighbor);
                                visitedTiles.add(neighbor);
                            }
                        }
                    }
                }
            }
            // On prépare la prochaine itération
            frontier.clear();
            frontier.addAll(nextFrontier);
            nextFrontier.clear();
        }

        // Si c'est le mode attaque, on garde tout.
        // Si c'est le mode déplacement, on retire la position d'origine (l'unité est déjà dessus).
        if (!isAttackMode) {
            visitedTiles.removeValue(unit.getCoordinates(), false); // false car Vector2.equals compare les valeurs
        }

        // Note : La logique originale pour 'atkOutput' dans aUnit.java semblait filtrer différemment
        // les tuiles selon qu'on a bougé ou non. Ici, nous retournons l'ensemble des tuiles accessibles.
        // Le BattleManager pourra filtrer davantage si nécessaire (ex: ne pas attaquer une case vide).

        return visitedTiles;
    }

    /**
     * Vérifie si une liste contient un vecteur donné (comparaison par valeur).
     */
    private static boolean contains(Array<Vector2> list, Vector2 target) {
        for (Vector2 v : list) {
            if (v.epsilonEquals(target, 0.1f)) return true;
        }
        return false;
    }
}
