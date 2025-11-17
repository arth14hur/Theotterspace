package com.T_jav_502.Theotterspace.logic;

import com.T_jav_502.Theotterspace.PathFinder.AStarPathFinder;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class BattleAI {

    private final BattleManager battleManager;
    private final TiledMapTileLayer collisionLayer;

    // Timer pour donner un rythme humain aux actions
    private float timer = 0;
    private final float ACTION_DELAY = 0.8f;

    public BattleAI(BattleManager battleManager, TiledMapTileLayer collisionLayer) {
        this.battleManager = battleManager;
        this.collisionLayer = collisionLayer;
    }

    public void update(float delta) {
        timer += delta;
        if (timer >= ACTION_DELAY) {
            playTurn();
            timer = 0;
        }
    }

    private void playTurn() {
        // 1. Si une unité est déjà sélectionnée (après un mouvement), elle doit finir son action
        if (battleManager.getSelectedUnit() != null) {
            finishUnitAction(battleManager.getSelectedUnit());
            return;
        }

        // 2. Trouver une unité disponible
        aUnit bestUnit = null;
        // On mélange la liste pour ne pas toujours jouer les unités dans le même ordre (Imprévisibilité)
        Array<aUnit> availableUnits = new Array<>(battleManager.getCurrentTeam().getUnits());
        availableUnits.shuffle();

        for (aUnit unit : availableUnits) {
            if (!unit.hasMoved() && !unit.hasAttacked()) {
                bestUnit = unit;
                break;
            }
        }

        // 3. Si plus personne ne peut jouer, on passe le tour
        if (bestUnit == null) {
            battleManager.endTurn();
            return;
        }

        // 4. Calculer le meilleur mouvement pour cette unité
        evaluateAndAct(bestUnit);
    }

    private void evaluateAndAct(aUnit unit) {
        // On sélectionne l'unité dans le moteur
        battleManager.forceSelectUnit(unit);

        Array<Vector2> moves = battleManager.getMovementRange();
        // Ajoute la position actuelle (si rester sur place est la meilleure option)
        if(moves == null) moves = new Array<>();
        moves.add(unit.getCoordinates());

        Vector2 bestTile = null;
        float bestScore = -Float.MAX_VALUE;

        // --- SYSTÈME DE SCORE (Utility AI) ---
        for (Vector2 tile : moves) {
            // On vérifie que la case est libre (ou que c'est moi-même)
            aUnit occupier = getUnitAt(tile);
            if (occupier != null && occupier != unit) continue;

            float score = calculateTileScore(unit, tile);

            if (score > bestScore) {
                bestScore = score;
                bestTile = tile;
            }
        }

        // 5. Exécuter l'action
        if (bestTile != null) {
            // Si la meilleure case est la case actuelle, on passe directement à l'attaque/fin
            if (bestTile.equals(unit.getCoordinates())) {
                finishUnitAction(unit);
            } else {
                // Sinon, on demande au BattleManager de bouger
                battleManager.actionAtTile(bestTile);
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
    private float calculateTileScore(aUnit myUnit, Vector2 tilePos) {
        float score = 0;

        // Facteur aléatoire pour rendre l'IA moins prévisible (Ex: ne prend pas toujours le chemin optimal parfait)
        score += MathUtils.random(-5f, 5f);

        // --- CRITÈRE 1 : Distance vers l'ennemi le plus proche (Se rapprocher) ---
        aUnit closestEnemy = null;
        float minDst = Float.MAX_VALUE;

        for (Team t : battleManager.getTeams()) {
            if (t == myUnit.getTeam()) continue;
            for (aUnit enemy : t.getUnits()) {
                float dst = tilePos.dst(enemy.getCoordinates());
                if (dst < minDst) {
                    minDst = dst;
                    closestEnemy = enemy;
                }
            }
        }

        if (closestEnemy != null) {
            // Moins on est loin, plus le score est haut. (MaxMapSize ~ 50, donc score ~ -dist)
            score -= minDst * 2;
        }

        // --- CRITÈRE 2 : Possibilité d'attaquer (Très important) ---
        // On simule "si j'étais sur cette case, qui pourrais-je attaquer ?"
        // Note : On fait une simulation simplifiée de la portée ici pour la performance
        if (closestEnemy != null) {
            float distToEnemy = tilePos.dst(closestEnemy.getCoordinates());
            // Si l'ennemi est à portée d'attaque (approximatif via distance euclidienne vs range)
            if (distToEnemy <= myUnit.getRange() + 0.5f) { // +0.5 pour la marge d'erreur float/grid
                score += 100; // Gros bonus si on peut taper

                // --- CRITÈRE 3 : Tuer un ennemi (Priorité absolue) ---
                int damage = Math.max(1, myUnit.getAttack() - closestEnemy.getDefense());
                if (closestEnemy.getHp() - damage <= 0) {
                    score += 200; // Bonus KILL
                }

                // --- CRITÈRE 4 : Taper les unités faibles ---
                score += (float)(closestEnemy.getMaxHp() - closestEnemy.getHp()) * 2;
            }
        }

        // --- CRITÈRE 5 : Défense (Éviter d'être trop exposé ?) ---
        // Pourrait être ajouté ici (ex: bonus si la case est une Forêt/Cover)

        return score;
    }

    private void finishUnitAction(aUnit unit) {
        // Une fois déplacé, l'IA regarde si elle peut attaquer quelqu'un
        aUnit target = findBestTarget(unit);

        if (target != null) {
            battleManager.actionAtTile(target.getCoordinates()); // Attaque !
        } else {
            battleManager.forceFinishTurn(unit); // Rien à faire, fin.
        }
    }

    private aUnit findBestTarget(aUnit attacker) {
        // Recalcule la vraie portée d'attaque
        Array<Vector2> range = MovementCalculator.getAccessibleTiles(attacker, collisionLayer, true);
        aUnit bestTarget = null;
        float lowestHp = Float.MAX_VALUE;

        for (Team t : battleManager.getTeams()) {
            if (t == attacker.getTeam()) continue;
            for (aUnit enemy : t.getUnits()) {
                if (contains(range, enemy.getCoordinates())) {
                    // Stratégie : Attaquer l'unité la plus faible à portée
                    if (enemy.getHp() < lowestHp) {
                        lowestHp = enemy.getHp();
                        bestTarget = enemy;
                    }
                }
            }
        }
        return bestTarget;
    }

    // Helpers
    private aUnit getUnitAt(Vector2 pos) {
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
