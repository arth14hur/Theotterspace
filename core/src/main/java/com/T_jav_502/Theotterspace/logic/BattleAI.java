package com.T_jav_502.Theotterspace.logic;

import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class BattleAI {

    private final BattleManager battleManager;
    private final TiledMapTileLayer floorLayer;

    // Timer pour donner un rythme humain aux actions
    private float timer = 0;

    public BattleAI(BattleManager battleManager, TiledMapTileLayer floorLayer) {
        this.battleManager = battleManager;
        this.floorLayer = floorLayer;
    }

    public void update(float delta) {
        timer += delta;
        float delay = 0.8f;
        if (timer >= delay) {
            botTurn();
            timer = 0;
        }
    }

    private void botTurn() {
        // 1. Si une unité est déjà sélectionnée (après un mouvement), elle doit finir son action
        if (battleManager.getSelectedUnit() != null) {
            AIaction(battleManager.getSelectedUnit());
            return;
        }

        // 2. Trouver une unité disponible
        aUnit chooseUnit = null;
        // On mélange la liste pour ne pas toujours jouer les unités dans le même ordre (Imprévisibilité)
        Array<aUnit> availableUnits = new Array<>(battleManager.getCurrentTeam().getUnits());
        availableUnits.shuffle();

        for (aUnit unit : availableUnits) {
            if (!unit.hasMoved() && !unit.hasAttacked()) {
                chooseUnit = unit;
                break;
            }
        }

        // 3. Si plus personne ne peut jouer, on passe le tour
        if (chooseUnit == null) {
            battleManager.endTurn();
            return;
        }

        // 4. Calculer le meilleur mouvement pour cette unité
        AIEvaluatingSystem(chooseUnit);
    }

    private void AIEvaluatingSystem(aUnit unit) {
        // On sélectionne l'unité dans le moteur
        battleManager.forceSelectUnit(unit);

        Array<Vector2> moves = battleManager.getMovementRange();
        // Ajoute la position actuelle (si rester sur place est la meilleure option)
        if(moves == null) moves = new Array<>();
        moves.add(unit.getCoordinates());

        Vector2 chooseTile = null;
        float scoreAI = -Float.MAX_VALUE;

        // --- SYSTÈME DE SCORE (Utility AI) ---
        for (Vector2 tile : moves) {
            // On vérifie que la case est libre (ou que c'est moi-même)
            aUnit occupier = getUnitposition(tile);
            if (occupier != null && occupier != unit) continue;

            float score = tileScore(unit, tile);

            if (score > scoreAI) {
                scoreAI = score;
                chooseTile = tile;
            }
        }

        // 5. Exécuter l'action
        if (chooseTile != null) {
            // Si la meilleure case est la case actuelle, on passe directement à l'attaque/fin
            if (chooseTile.equals(unit.getCoordinates())) {
                AIaction(unit);
            } else {
                // Sinon, on demande au BattleManager de bouger
                battleManager.actionAtTile(chooseTile);
            }
        } else {
            // Cas de secours
            battleManager.forceFinishTurn(unit);
        }
    }

    /**
     * Calcule l'intérêt d'une case donnée.
     * C'est ici que réside "l'intelligence".
     */
    private float tileScore(aUnit AIUnit, Vector2 tilePossition) {
        float score = 0;

        // Facteur aléatoire pour rendre l'IA moins prévisible (Ex: ne prend pas toujours le chemin optimal parfait)
        score += MathUtils.random(-5f, 5f);

        // --- CRITÈRE 1 : Distance vers l'ennemi le plus proche (Se rapprocher) ---
        aUnit playerUnit = null;
        float minDistance = Float.MAX_VALUE;

        for (Team t : battleManager.getTeams()) {
            if (t == AIUnit.getTeam()) continue;
            for (aUnit enemy : t.getUnits()) {
                float dst = tilePossition.dst(enemy.getCoordinates());
                if (dst < minDistance) {
                    minDistance = dst;
                    playerUnit = enemy;
                }
            }
        }

        if (playerUnit != null) {
            // Moins on est loin, plus le score est haut. (MaxMapSize ~ 50, donc score ~ -dist)
            score -= minDistance * 2;
        }

        // --- CRITÈRE 2 : Possibilité d'attaquer (Très important) ---
        // On simule "si j'étais sur cette case, qui pourrais-je attaquer ?"
        // Note : On fait une simulation simplifiée de la portée ici pour la performance
        if (playerUnit != null) {
            float distancePlayer = tilePossition.dst(playerUnit.getCoordinates());
            // Si l'ennemi est à portée d'attaque (approximatif via distance euclidienne vs range)
            if (distancePlayer <= AIUnit.getRange() + 0.5f) { // +0.5 pour la marge d'erreur float/grid
                score += 100; // Gros bonus si on peut taper

                // --- CRITÈRE 3 : Tuer un ennemi (Priorité absolue) ---
                int damage = Math.max(1, AIUnit.getAttack() - playerUnit.getDefense());
                if (playerUnit.getHp() - damage <= 0) {
                    score += 200; // Bonus KILL
                }

                // --- CRITÈRE 4 : Taper les unités faibles ---
                score += (float)(playerUnit.getMaxHp() - playerUnit.getHp()) * 2;
            }
        }

        // --- CRITÈRE 5 : Défense (Éviter d'être trop exposé ?) ---
        // Pourrait être ajouté ici (ex: bonus si la case est une Forêt/Cover)

        return score;
    }

    private void AIaction(aUnit unit) {
        // Une fois déplacé, l'IA regarde si elle peut attaquer quelqu'un
        aUnit target = AItarget(unit);

        if (target != null) {
            battleManager.actionAtTile(target.getCoordinates()); // Attaque !
        } else {
            battleManager.forceFinishTurn(unit); // Rien à faire, fin.
        }
    }

    private aUnit AItarget(aUnit attacker) {
        aUnit selctedTarget = null;
        Array<Vector2> range = attacker.whereCanWalk(floorLayer, true, battleManager.getOpponentTeam());
        aUnit bestTarget = null;
        float lowestHp = Float.MAX_VALUE;

        for (Team t : battleManager.getTeams()) {
            if (t == attacker.getTeam()) continue;
            for (aUnit enemy : t.getUnits()) {
                if (contains(range, enemy.getCoordinates())) {
                    // Stratégie : Attaquer l'unité la plus faible à portée
                    if (enemy.getHp() < lowestHp) {
                        lowestHp = enemy.getHp();
                        selctedTarget = enemy;
                    }
                }
            }
        }
        return selctedTarget;
    }

    // Helpers
    private aUnit getUnitposition(Vector2 pos) {
        for (Team t : battleManager.getTeams()) {
            for (aUnit u : t.getUnits()) {
                if ((int)u.getCoordinates().x == (int)pos.x && (int)u.getCoordinates().y == (int)pos.y) return u;
            }
        }
        return null;
    }

    private boolean contains(Array<Vector2> list, Vector2 v) {
        for(Vector2 item : list) if((int)item.x == (int)v.x && (int)item.y == (int)v.y) return true;
        return false;
    }
}
