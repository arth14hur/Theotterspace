package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.logic.BattleManager;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.tiles.Selector;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {

    private final OrthogonalTiledMapRenderer mapRenderer;
    private final BattleManager battleManager;
    private final Selector selector;

    // Assets graphiques
    private final Texture textureSelector;
    private final Sprite greenSquare;
    private final Sprite redSquare;

    public WorldRenderer(TiledMap map, BattleManager battleManager, Selector selector, int scale) {
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, scale);
        this.battleManager = battleManager;
        this.selector = selector;

        // Chargement des textures
        this.textureSelector = new Texture(Gdx.files.internal("Select2.png"));
        this.greenSquare = new Sprite(new Texture(Gdx.files.internal("green.png")));
        this.redSquare = new Sprite(new Texture(Gdx.files.internal("red.png")));
    }

    public void render(OrthographicCamera camera) {
        mapRenderer.setView(camera);

        // 1. Dessiner la carte (couches 0, 1, 2)
        mapRenderer.render(new int[]{0, 1, 2});

        mapRenderer.getBatch().begin();

        // 2. Dessiner les zones de déplacement/attaque
        drawRanges();

        // 3. Dessiner le sélecteur
        if (selector.isDisplay()) {
            mapRenderer.getBatch().draw(textureSelector,
                selector.getCoordinates().x * 32,
                selector.getCoordinates().y * 32);
        }

        // 4. Dessiner les unités
        for (Team team : battleManager.getTeams()) {
            for (aUnit unit : team.getUnits()) {
                unit.draw(mapRenderer.getBatch());
            }
        }

        mapRenderer.getBatch().end();
    }

    private void drawRanges() {
        // 1. On récupère les listes
        Array<Vector2> moves = battleManager.getMovementRange();
        Array<Vector2> attacks = battleManager.getAttackRange();

        // 2. On dessine d'abord TOUTES les cases vertes (Déplacement)
        if (moves != null) {
            for (Vector2 pos : moves) {
                greenSquare.setPosition(pos.x * 32, pos.y * 32);
                greenSquare.draw(mapRenderer.getBatch());
            }
        }

        // 3. On dessine les cases rouges (Attaque) UNIQUEMENT si elles ne sont pas déjà vertes
        if (attacks != null) {
            for (Vector2 pos : attacks) {
                // La condition magique : Si moves est null OU si moves ne contient PAS cette position
                if (moves == null || !moves.contains(pos, false)) {
                    redSquare.setPosition(pos.x * 32, pos.y * 32);
                    redSquare.draw(mapRenderer.getBatch());
                }
            }
        }
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
        textureSelector.dispose();
        if (greenSquare.getTexture() != null) greenSquare.getTexture().dispose();
        if (redSquare.getTexture() != null) redSquare.getTexture().dispose();
    }
}
