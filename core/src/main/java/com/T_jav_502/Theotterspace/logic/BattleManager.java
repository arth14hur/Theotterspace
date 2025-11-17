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

    // État de la sélection
    private aUnit selectedUnit = null;
    private aUnit movingUnit = null;
    private Array<Vector2> currentPath = new Array<>();

    // Portées calculées pour l'affichage
    private Array<Vector2> movementRange;
    private Array<Vector2> attackRange;

    private final TiledMapTileLayer collisionLayer;

    public BattleManager(TiledMap tiledMap) {
        this.collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0); // Floor layer
        this.teams = new Array<>();
        initializeTeams(tiledMap);
    }

    /** Extrait la logique de création d'équipe du GameScreen */
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
        // Gestion de l'animation de mouvement
        if (movingUnit != null && currentPath.size > 0) {
            currentPath = movingUnit.moveTo(currentPath, 10, delta); // 10 = vitesse
            if (currentPath.size == 0) {
                movingUnit.setMoved(true);
                movingUnit.setColor(Color.GRAY); // Indique visuellement que l'unité a joué
                movingUnit = null;
                deselectUnit();
            }
        }
    }

    // Appelé par GameScreen lors d'un clic GAUCHE
    public void selectTile(Vector2 position) {
        if (movingUnit != null) return; // On bloque si une unité bouge déjà

        // Vérifier si on clique sur une unité
        aUnit clickedUnit = getUnitAt(position);

        if (clickedUnit != null && clickedUnit.getTeam() == getCurrentTeam() && !clickedUnit.hasMoved()) {
            // Sélectionner une unité alliée non fatiguée
            selectedUnit = clickedUnit;
            // Calculer les portées via MovementCalculator
            movementRange = MovementCalculator.getAccessibleTiles(selectedUnit, collisionLayer, false);
            attackRange = MovementCalculator.getAccessibleTiles(selectedUnit, collisionLayer, true);
        } else {
            deselectUnit();
        }
    }

    // Appelé par GameScreen lors d'un clic DROIT (Action)
    public void actionAtTile(Vector2 position) {
        if (selectedUnit == null || movingUnit != null) return;

        // Logique de déplacement
        if (movementRange != null && contains(movementRange, position)) {
            // Vérifier que la case n'est pas occupée par une autre unité
            if (getUnitAt(position) == null) {
                currentPath = AStarPathFinder.findPath(selectedUnit.getCoordinates(), position, collisionLayer);
                if (currentPath.size > 0) {
                    movingUnit = selectedUnit; // Démarre l'animation dans update()
                }
            }
        }
    }

    public void deselectUnit() {
        selectedUnit = null;
        movementRange = null;
        attackRange = null;
    }

    public void endTurn() {
        deselectUnit();
        // Réinitialiser les unités de l'équipe actuelle
        for (aUnit u : getCurrentTeam().getUnits()) {
            u.resetTurn();
        }
        // Changer d'équipe
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

    // Helper pour Array<Vector2> qui manque parfois de méthode contains simple
    private boolean contains(Array<Vector2> list, Vector2 v) {
        for(Vector2 item : list) if(item.equals(v)) return true;
        return false;
    }
}
