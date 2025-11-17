package com.T_jav_502.Theotterspace.logic;

import com.T_jav_502.Theotterspace.PathFinder.AStarPathFinder;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Cerveau central de la logique de combat.
 * Gère le tour par tour, la sélection, les déplacements et les règles du jeu.
 * Découplé de l'affichage (GameScreen).
 */
public class BattleManager {

    private final Array<Team> teams;
    private final TiledMapTileLayer collisionLayer; // Nécessaire pour le pathfinding

    private int currentTurnIndex = 0;
    private aUnit selectedUnit = null;
    private aUnit movingUnit = null; // Unité en cours de déplacement (animation)

    private Array<Vector2> currentPath = new Array<>();
    private Array<Vector2> movementRange = new Array<>();
    private Array<Vector2> attackRange = new Array<>();

    private final int unitSpeed = 10; // Vitesse de déplacement visuel

    /**
     * @param teams La liste des équipes en jeu.
     * @param collisionLayer La couche de la carte utilisée pour vérifier les collisions.
     */
    public BattleManager(Array<Team> teams, TiledMapTileLayer collisionLayer) {
        this.teams = teams;
        this.collisionLayer = collisionLayer;
    }

    /**
     * Méthode principale de mise à jour logique (appelée par render).
     * Gère l'animation de déplacement.
     * @param delta Temps écoulé depuis la dernière frame.
     */
    public void update(float delta) {
        if (movingUnit != null && currentPath.size > 0) {
            // On délègue le mouvement "physique" à l'unité
            currentPath = movingUnit.moveTo(currentPath, unitSpeed, delta);

            // Si le chemin est vide, le mouvement est fini
            if (currentPath.size == 0) {
                movingUnit = null;
                // Ici, on pourrait déclencher la fin de l'action de l'unité
                deselectUnit();
            }
        }
    }

    /**
     * Gère une interaction (clic) sur une tuile donnée.
     * @param tileCoordinate Coordonnées de la tuile cliquée.
     * @param isRightClick Indique s'il s'agit d'un clic droit (action) ou gauche (sélection).
     */
    public void handleInteraction(Vector2 tileCoordinate, boolean isRightClick) {
        // Si une unité est en train de bouger, on bloque les inputs
        if (movingUnit != null) return;

        if (isRightClick) {
            tryMoveUnit(tileCoordinate);
        } else {
            trySelectUnit(tileCoordinate);
        }
    }

    /**
     * Tente de sélectionner une unité à la position donnée.
     */
    private void trySelectUnit(Vector2 position) {
        Team currentTeam = getCurrentTeam();
        aUnit unitFound = null;

        // Cherche si une unité de l'équipe active est sur la case
        for (aUnit unit : currentTeam.getUnits()) {
            // Attention à la comparaison de float, cast en int pour être sûr d'être sur la tuile
            if ((int)unit.getCoordinates().x == (int)position.x &&
                (int)unit.getCoordinates().y == (int)position.y) {
                unitFound = unit;
                break;
            }
        }

        if (unitFound != null) {
            selectUnit(unitFound);
        } else {
            deselectUnit();
        }
    }

    /**
     * Tente de déplacer l'unité sélectionnée vers la destination.
     */
    private void tryMoveUnit(Vector2 destination) {
        if (selectedUnit == null) return;

        // Vérifie si la destination est dans la portée calculée
        if (movementRange != null && movementRange.contains(destination, false)) {
            // Calcul du chemin
            currentPath = AStarPathFinder.findPath(
                selectedUnit.getCoordinates(),
                destination,
                collisionLayer
            );

            if (currentPath.size > 0) {
                movingUnit = selectedUnit;
                // On ne désélectionne pas tout de suite, on attend la fin du mouvement
            }
        }
    }

    private void selectUnit(aUnit unit) {
        this.selectedUnit = unit;
        // Utilisation du MovementCalculator refactorisé
        this.movementRange = MovementCalculator.getAccessibleTiles(unit, collisionLayer, false);
        this.attackRange = MovementCalculator.getAccessibleTiles(unit, collisionLayer, true);
    }

    public void deselectUnit() {
        this.selectedUnit = null;
        this.movementRange = null;
        this.attackRange = null;
    }

    /**
     * Passe au tour de l'équipe suivante.
     */
    public void endTurn() {
        deselectUnit();

        // Réinitialiser les unités de l'équipe qui vient de finir
        // Note: Il faudra ajouter une méthode resetTurn() dans aUnit pour remettre moved=false
        // for (aUnit unit : getCurrentTeam().getUnits()) { unit.resetTurn(); }

        currentTurnIndex = (currentTurnIndex + 1) % teams.size;
        System.out.println("Tour de l'équipe : " + getCurrentTeam().getCurentSpecies());
    }

    // --- Getters pour l'affichage (GameScreen) ---

    public Team getCurrentTeam() {
        return teams.get(currentTurnIndex);
    }

    public Array<Team> getTeams() {
        return teams;
    }

    public aUnit getSelectedUnit() {
        return selectedUnit;
    }

    public Array<Vector2> getMovementRange() {
        return movementRange;
    }

    public Array<Vector2> getAttackRange() {
        return attackRange;
    }
}
