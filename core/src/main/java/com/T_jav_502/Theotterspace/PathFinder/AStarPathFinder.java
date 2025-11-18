package com.T_jav_502.Theotterspace.PathFinder;

import com.T_jav_502.Theotterspace.teams.Team; // Import nécessaire
import com.T_jav_502.Theotterspace.units.aUnit; // Import nécessaire
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AStarPathFinder {

    // On ajoute le paramètre 'teams' pour connaître la position des unités
    public static Array<Vector2> findPath(Vector2 start, Vector2 goal, TiledMapTileLayer layer, Array<Team> teams) {

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

                // On passe 'teams' à isBlocked
                if (isBlocked(neighbor, layer, teams)) continue;
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

    // === ISBLOCKED MODIFIÉ ===
    // Ajout de la vérification des unités
    private static boolean isBlocked(Vector2 pos, TiledMapTileLayer layer, Array<Team> teams) {
        int x = (int) pos.x;
        int y = (int) pos.y;

        // 1. Vérifier si le terrain est marchable (Murs, vide, objets statiques de la map)
        if (!isWalkable(x, y, layer)) {
            return true;
        }

        // 2. Vérifier si une unité occupe cette case
        if (teams != null) {
            for (Team team : teams) {
                for (aUnit unit : team.getUnits()) {
                    // On vérifie les coordonnées (en entiers pour être sûr)
                    if ((int)unit.getCoordinates().x == x && (int)unit.getCoordinates().y == y) {
                        return true; // La case est bloquée par une unité
                    }
                }
            }
        }

        return false;
    }

    private static Array<Vector2> getNeighbors(Vector2 pos) {
        Array<Vector2> out = new Array<>();

        out.add(new Vector2(pos.x + 1, pos.y));
        out.add(new Vector2(pos.x - 1, pos.y));
        out.add(new Vector2(pos.x, pos.y + 1));
        out.add(new Vector2(pos.x, pos.y - 1));

        return out;
    }

    private static boolean isWalkable(int x, int y, TiledMapTileLayer layer){
        if (layer.getCell(x, y) != null){
            // Vérifie la propriété "walkable" définie dans Tiled
            return(layer.getCell(x, y).getTile().getProperties().get("walkable", Boolean.class));
        }
        return false;
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
