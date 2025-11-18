package com.T_jav_502.Theotterspace.PathFinder;

import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap; // On importe TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AStarPathFinder {

    /**
     * Trouve un chemin en prenant en compte les unités (teams) et tous les obstacles de la TiledMap.
     * @param start Position de départ
     * @param goal Position d'arrivée
     * @param map La carte complète (TiledMap) pour vérifier tous les calques
     * @param teams La liste des équipes pour éviter les unités
     */
    public static Array<Vector2> findPath(Vector2 start, Vector2 goal, TiledMap map, Array<Team> teams) {

        Array<Node> open = new Array<>();
        Array<Node> closed = new Array<>();

        open.add(new Node(start, null, 0, heuristic(start, goal)));

        while (open.size > 0) {

            Node current = open.first();
            for (Node n : open) {
                if (n.fCost < current.fCost)
                    current = n;
            }

            if (current.position.epsilonEquals(goal, 0.1f))
                return reconstructPath(current);

            open.removeValue(current, true);
            closed.add(current);

            for (Vector2 neighbor : getNeighbors(current.position)) {

                // On passe maintenant la 'map' complète
                if (isBlocked(neighbor, map, teams)) continue;
                if (containsPosition(closed, neighbor)) continue;

                float gCost = current.gCost + 1;

                Node existing = getNode(open, neighbor);
                if (existing == null) {
                    open.add(new Node(
                        neighbor,
                        current,
                        gCost,
                        heuristic(neighbor, goal)
                    ));
                } else if (gCost < existing.gCost) {
                    existing.parent = current;
                    existing.gCost = gCost;
                    existing.fCost = gCost + existing.hCost;
                }
            }
        }

        return new Array<>();
    }

    private static float heuristic(Vector2 a, Vector2 b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private static boolean isBlocked(Vector2 pos, TiledMap map, Array<Team> teams) {
        int x = (int) pos.x;
        int y = (int) pos.y;

        // 1. Vérification de la Carte (Murs, Objets, Vide)
        // On vérifie si la case est marchable sur la map
        if (!isWalkableOnMap(x, y, map)) {
            return true;
        }

        // 2. Vérification des Unités (Dynamique)
        if (teams != null) {
            for (Team team : teams) {
                for (aUnit unit : team.getUnits()) {
                    if ((int)unit.getCoordinates().x == x && (int)unit.getCoordinates().y == y) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Vérifie tous les calques de la map à la position x,y.
     * Retourne TRUE si on peut marcher (sol présent + pas d'obstacle).
     */
    private static boolean isWalkableOnMap(int x, int y, TiledMap map) {
        boolean hasFloor = false; // Est-ce qu'il y a du sol ?

        // On parcourt tous les calques de la carte
        for (MapLayer mapLayer : map.getLayers()) {
            // On ne s'intéresse qu'aux calques de tuiles (pas les calques d'objets/images)
            if (mapLayer instanceof TiledMapTileLayer) {
                TiledMapTileLayer layer = (TiledMapTileLayer) mapLayer;

                // Vérifier les limites du calque
                if (x < 0 || x >= layer.getWidth() || y < 0 || y >= layer.getHeight()) {
                    continue;
                }

                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null && cell.getTile() != null) {
                    // Si la tuile a la propriété "walkable"
                    if (cell.getTile().getProperties().containsKey("walkable")) {
                        boolean walkable = cell.getTile().getProperties().get("walkable", Boolean.class);

                        // Si une seule couche dit "NON" (walkable = false), c'est bloqué (ex: mur, objet)
                        if (!walkable) {
                            return false;
                        }

                        // Si une couche dit "OUI", c'est qu'il y a du sol
                        hasFloor = true;
                    }
                }
            }
        }

        // Pour marcher, il faut au moins un sol (hasFloor) et aucun obstacle (géré par le return false ci-dessus)
        return hasFloor;
    }

    private static Array<Vector2> getNeighbors(Vector2 pos) {
        Array<Vector2> out = new Array<>();
        out.add(new Vector2(pos.x + 1, pos.y));
        out.add(new Vector2(pos.x - 1, pos.y));
        out.add(new Vector2(pos.x, pos.y + 1));
        out.add(new Vector2(pos.x, pos.y - 1));
        return out;
    }

    private static boolean containsPosition(Array<Node> list, Vector2 pos) {
        for (Node n : list)
            if (n.position.epsilonEquals(pos, 0.1f))
                return true;
        return false;
    }

    private static Node getNode(Array<Node> list, Vector2 pos) {
        for (Node n : list)
            if (n.position.epsilonEquals(pos, 0.1f))
                return n;
        return null;
    }

    private static Array<Vector2> reconstructPath(Node end) {
        Array<Vector2> path = new Array<>();
        Node current = end;
        while (current != null) {
            path.add(current.position.cpy());
            current = current.parent;
        }
        path.reverse();
        return path;
    }
}
