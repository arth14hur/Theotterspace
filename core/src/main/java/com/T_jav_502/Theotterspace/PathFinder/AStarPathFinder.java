package com.T_jav_502.Theotterspace.PathFinder;

import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AStarPathFinder {


    public static Array<Vector2> findPath(Vector2 start, Vector2 goal, TiledMapTileLayer layer, Team enemyTeam) {

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

                if (isBlocked(neighbor, layer, enemyTeam)) continue;
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

    private static boolean isBlocked(Vector2 pos, TiledMapTileLayer layer, Team enemyTeam) {

        for (aUnit unit : enemyTeam.getUnits()) {
            if (unit.getCoordinates().epsilonEquals(pos)) return true;
        }
        return !isWalkable(pos, layer);
    }

    private static Array<Vector2> getNeighbors(Vector2 pos) {
        Array<Vector2> out = new Array<>();

        out.add(new Vector2(pos.x + 1, pos.y));
        out.add(new Vector2(pos.x - 1, pos.y));
        out.add(new Vector2(pos.x, pos.y + 1));
        out.add(new Vector2(pos.x, pos.y - 1));

        return out;
    }

    private static boolean isWalkable(Vector2 pos, TiledMapTileLayer layer){
        if (layer.getCell((int) pos.x, (int) pos.y) != null){
            return(layer.getCell((int) pos.x, (int) pos.y).getTile().getProperties().get("walkable", Boolean.class));
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
