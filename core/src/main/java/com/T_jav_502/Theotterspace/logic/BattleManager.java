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

    public BattleManager(TiledMap tiledMap) {
        if (tiledMap.getLayers().getCount() > 0) {
            this.collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        } else {
            throw new RuntimeException("La TiledMap ne contient aucun layer !");
        }
        this.teams = new Array<>();
        initializeTeams(tiledMap);
    }

    private void initializeTeams(TiledMap map) {
        teams.add(new Team(Team.Species.OTTER));
        teams.add(new Team(Team.Species.WOLF));

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
        if (movingUnit != null && currentPath.size > 0) {
            currentPath = movingUnit.moveTo(currentPath, 10, delta);

            if (currentPath.size == 0) {
                movingUnit.setMoved(true);
                // Recalculer les portées après mouvement
                movementRange = MovementCalculator.getAccessibleTiles(movingUnit, collisionLayer, false);
                attackRange = MovementCalculator.getAccessibleTiles(movingUnit, collisionLayer, true);

                selectedUnit = movingUnit; // L'unité reste sélectionnée après le mouvement
                movingUnit = null;

                // Si l'unité ne peut plus attaquer, on termine son tour
                if (selectedUnit.hasAttacked()) {
                    finishUnitTurn(selectedUnit);
                }
            }
        }
    }

    public void selectTile(Vector2 position) {
        if (movingUnit != null) return;

        aUnit clickedUnit = getUnitAt(position);

        // Sélection si unité alliée et pas encore attaqué
        if (clickedUnit != null && clickedUnit.getTeam() == getCurrentTeam() && !clickedUnit.hasAttacked()) {
            selectedUnit = clickedUnit;
            movementRange = MovementCalculator.getAccessibleTiles(selectedUnit, collisionLayer, false);
            attackRange = MovementCalculator.getAccessibleTiles(selectedUnit, collisionLayer, true);
        } else {
            deselectUnit();
        }
    }

    public void actionAtTile(Vector2 position) {
        if (selectedUnit == null || movingUnit != null) return;

        aUnit targetUnit = getUnitAt(position);

        // CAS 1 : ATTAQUE (Unité présente sur la case cible)
        if (targetUnit != null) {
            if (targetUnit.getTeam() != getCurrentTeam()
                && !selectedUnit.hasAttacked()
                && attackRange != null
                && contains(attackRange, position)) {

                performAttack(selectedUnit, targetUnit);
            }
        }
        // CAS 2 : DÉPLACEMENT (Case vide)
        else {
            if (movementRange != null && contains(movementRange, position) && !selectedUnit.hasMoved()) {
                currentPath = AStarPathFinder.findPath(selectedUnit.getCoordinates(), position, collisionLayer);
                if (currentPath.size > 0) {
                    movingUnit = selectedUnit;
                }
            }
        }
    }

    private void performAttack(aUnit attacker, aUnit defender) {
        System.out.println("Attacking unit at " + defender.getCoordinates());
        defender.receiveDamage(attacker.getAttack());

        attacker.setAttacked(true);
        attacker.setMoved(true); // Une attaque termine le mouvement

        if (defender.getHp() <= 0) {
            defender.getTeam().removeUnit(defender);
            System.out.println("Target eliminated!");
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
        System.out.println("Turn: " + getCurrentTeam().getCurentSpecies());
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

    private boolean contains(Array<Vector2> list, Vector2 v) {
        if (list == null) return false;
        for(Vector2 item : list) {
            if((int)item.x == (int)v.x && (int)item.y == (int)v.y) return true;
        }
        return false;
    }
}
