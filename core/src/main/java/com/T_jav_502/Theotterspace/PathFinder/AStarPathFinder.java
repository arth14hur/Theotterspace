package com.T_jav_502.Theotterspace.PathFinder;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AStarPathFinder {

    // Interface fonctionnelle pour vérifier si une case est praticable
    // Cela permet à GameScreen de fournir la logique de collision
    @FunctionalInterface
    public interface WalkableChecker {
        boolean isWalkable(int x, int y);
    }

    private final int width;
    private final int height;
    private final WalkableChecker walkableChecker;

    // === CONSTRUCTEUR MODIFIÉ ===
    public AStarPathFinder(int width, int height, WalkableChecker checker) {
        this.width = width;
        this.height = height;
        this.walkableChecker = checker;
    }

    public Array<Vector2> findPath(Vector2 start, Vector2 goal) {

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

                if (isBlocked(neighbor)) continue;
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

    private float heuristic(Vector2 a, Vector2 b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    // === ISBLOCKED MODIFIÉ ===
    private boolean isBlocked(Vector2 pos) {
        int x = (int) pos.x;
        int y = (int) pos.y;

        // Vérifie les limites de la carte
        if (x < 0 || y < 0 || x >= width || y >= height) return true;

        // Utilise le "checker" fourni dans le constructeur
        return !walkableChecker.isWalkable(x, y);
    }

    private Array<Vector2> getNeighbors(Vector2 pos) {
        Array<Vector2> out = new Array<>();

        out.add(new Vector2(pos.x + 1, pos.y));
        out.add(new Vector2(pos.x - 1, pos.y));
        out.add(new Vector2(pos.x, pos.y + 1));
        out.add(new Vector2(pos.x, pos.y - 1));

        return out;
    }

    private boolean containsPosition(Array<Node> list, Vector2 pos) {
        for (Node n : list)
            if (n.position.epsilonEquals(pos, 0.1f))
                return true;
        return false;
    }

    private Node getNode(Array<Node> list, Vector2 pos) {
        for (Node n : list)
            if (n.position.epsilonEquals(pos, 0.1f))
                return n;
        return null;
    }

    private Array<Vector2> reconstructPath(Node end) {
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
