package com.T_jav_502.Theotterspace.logic;

import com.T_jav_502.Theotterspace.PathFinder.AStarPathFinder;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.units.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class BattleManager {
    private Array<Team> teams;
    private int currentTurnIndex = 0;

    private aUnit selectedUnit = null;
    private aUnit movingUnit = null;
    private Array<Vector2> currentPath = new Array<>();

    private Array<Vector2> movementRange;
    private Array<Vector2> attackRange;

    private final TiledMapTileLayer collisionLayer;
    private final BattleAI battleAI; // Référence vers le cerveau de l'IA

    public BattleManager(TiledMap tiledMap) {
        if (tiledMap.getLayers().getCount() > 0) {
            this.collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        } else {
            throw new RuntimeException("La TiledMap ne contient aucun layer !");
        }
        this.teams = new Array<>();
        initializeTeams(tiledMap);

        // Initialisation de l'IA
        this.battleAI = new BattleAI(this, collisionLayer);
    }

    private void initializeTeams(TiledMap map) {
        teams.add(new Team(Team.Species.OTTER, false)); // Joueur
        teams.add(new Team(Team.Species.WOLF, true));  // IA
        // ... (Reste de l'initialisation des unités identique) ...
        TiledMapTileLayer unitLayer = (TiledMapTileLayer) map.getLayers().get("Units");
        if (unitLayer != null) {
            for (int x = 0; x < unitLayer.getWidth(); x++) {
                for (int y = 0; y < unitLayer.getHeight(); y++) {
                    if (unitLayer.getCell(x, y) != null) {
                        String teamName = unitLayer.getCell(x, y).getTile().getProperties().get("team", String.class);
                        String typeName = unitLayer.getCell(x, y).getTile().getProperties().get("type", String.class);

                        if (teamName != null && typeName != null) {
                            Team currentTeam = teamName.equals("OTTER") ? teams.get(0) : teams.get(1);
                            switch (typeName) {
                                case "Infantry": currentTeam.addUnit(new Infantry(currentTeam, x, y)); break;
                                case "Blaster": currentTeam.addUnit(new Blaster(currentTeam, x, y)); break;
                                case "Heavy": currentTeam.addUnit(new Heavy(currentTeam, x, y)); break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void update(float delta) {
        // 1. Gestion Prioritaire : Animation de mouvement
        if (movingUnit != null && currentPath.size > 0) {
            currentPath = movingUnit.moveTo(currentPath, 10, delta);

            if (currentPath.size == 0) {
                movingUnit.setMoved(true);
                movementRange = MovementCalculator.getAccessibleTiles(movingUnit, collisionLayer, false);
                attackRange = MovementCalculator.getAccessibleTiles(movingUnit, collisionLayer, true);
                selectedUnit = movingUnit;
                movingUnit = null;

                if (selectedUnit.hasAttacked()) {
                    finishUnitTurn(selectedUnit);
                }
            }
            return; // Bloque tout le reste pendant l'animation
        }

        // 2. Si c'est le tour de l'IA, on délègue à la classe BattleAI
        if (getCurrentTeam().isAi()) {
            battleAI.update(delta);
        }
    }

    // --- Gestion des Inputs Joueur ---

    public void selectTile(Vector2 position) {
        if (movingUnit != null || getCurrentTeam().isAi()) return; // Bloqué si IA joue

        aUnit clickedUnit = getUnitAt(position);
        if (clickedUnit != null && clickedUnit.getTeam() == getCurrentTeam() && !clickedUnit.hasAttacked()) {
            forceSelectUnit(clickedUnit);
        } else {
            deselectUnit();
        }
    }

    public void actionAtTile(Vector2 position) {
        // Note : On enlève la protection "isAi" ici si c'est l'IA qui appelle cette méthode via BattleAI
        // Mais pour sécuriser, l'IA appelle directement les logiques internes ou on laisse ouvert.
        // Pour simplifier : si c'est le joueur, selectTile bloque déjà l'interaction.

        if (selectedUnit == null || movingUnit != null) return;

        aUnit targetUnit = getUnitAt(position);
        if (targetUnit != null) {
            if (targetUnit.getTeam() != getCurrentTeam() && !selectedUnit.hasAttacked()
                && attackRange != null && contains(attackRange, position)) {
                performAttack(selectedUnit, targetUnit);
            }
        } else {
            if (movementRange != null && contains(movementRange, position) && !selectedUnit.hasMoved()) {
                // Vérifier collision unité
                if (getUnitAt(position) == null) {
                    currentPath = AStarPathFinder.findPath(selectedUnit.getCoordinates(), position, collisionLayer, teams);
                    if (currentPath.size > 0) {
                        movingUnit = selectedUnit;
                    }
                }
            }
        }
    }

    // --- Méthodes "Publiques" pour l'IA (Interface de commande) ---

    /** Permet à l'IA de sélectionner une unité logiciellement */
    public void forceSelectUnit(aUnit unit) {
        this.selectedUnit = unit;
        this.movementRange = MovementCalculator.getAccessibleTiles(unit, collisionLayer, false);
        this.attackRange = MovementCalculator.getAccessibleTiles(unit, collisionLayer, true);
    }

    /** Permet à l'IA de finir le tour d'une unité */
    public void forceFinishTurn(aUnit unit) {
        finishUnitTurn(unit);
    }

    // --- Logique Interne ---

    private void performAttack(aUnit attacker, aUnit defender) {
        System.out.println(attacker.getClass().getSimpleName() + " attaque " + defender.getClass().getSimpleName());
        defender.receiveDamage(attacker.getAttack());
        attacker.setAttacked(true);
        attacker.setMoved(true);

        if (defender.getHp() <= 0) {
            defender.getTeam().removeUnit(defender);
        }
        finishUnitTurn(attacker);
    }

    private void finishUnitTurn(aUnit unit) {
        unit.setColor(Color.GRAY);
        deselectUnit();
    }

    public void deselectUnit() {
        selectedUnit = null;
        movementRange = null;
        attackRange = null;
    }

    public void endTurn() {
        deselectUnit();
        for (aUnit u : getCurrentTeam().getUnits()) {
            u.resetTurn();
        }
        currentTurnIndex = (currentTurnIndex + 1) % teams.size;
        System.out.println("Nouveau tour : " + getCurrentTeam().getCurentSpecies());
    }

    private aUnit getUnitAt(Vector2 pos) {
        for (Team t : teams) {
            for (aUnit u : t.getUnits()) {
                if ((int)u.getCoordinates().x == (int)pos.x && (int)u.getCoordinates().y == (int)pos.y) {
                    return u;
                }
            }
        }
        return null;
    }

    public Team getCurrentTeam() { return teams.get(currentTurnIndex); }
    public Array<Team> getTeams() { return teams; }
    public Array<Vector2> getMovementRange() { return movementRange; }
    public Array<Vector2> getAttackRange() { return attackRange; }
    public aUnit getSelectedUnit() { return selectedUnit; } // Nécessaire pour l'IA

    private boolean contains(Array<Vector2> list, Vector2 v) {
        if (list == null) return false;
        for(Vector2 item : list) {
            if((int)item.x == (int)v.x && (int)item.y == (int)v.y) return true;
        }
        return false;
    }
}
