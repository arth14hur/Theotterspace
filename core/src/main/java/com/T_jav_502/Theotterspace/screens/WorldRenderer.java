package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.logic.BattleManager;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.tiles.Selector;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer; // Import nécessaire
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {

    private final OrthogonalTiledMapRenderer mapRenderer;
    private final BattleManager battleManager;
    private final Selector selector;

    // Outil de dessin de formes (pour les barres de vie)
    private final ShapeRenderer shapeRenderer;

    // Assets graphiques
    private final Texture textureSelector;
    private final Sprite greenSquare;
    private final Sprite redSquare;

    public WorldRenderer(TiledMap map, BattleManager battleManager, Selector selector, int scale) {
        this.mapRenderer = new OrthogonalTiledMapRenderer(map, scale);
        this.battleManager = battleManager;
        this.selector = selector;

        this.shapeRenderer = new ShapeRenderer();

        // Chargement des textures
        this.textureSelector = new Texture(Gdx.files.internal("Select2.png"));
        this.greenSquare = new Sprite(new Texture(Gdx.files.internal("green.png")));
        this.redSquare = new Sprite(new Texture(Gdx.files.internal("red.png")));
    }

    public void render(OrthographicCamera camera) {
        mapRenderer.setView(camera);

        // Rendering of the Tiles and objects of the map
        mapRenderer.render(new int[]{0, 1, 2});

        mapRenderer.getBatch().begin();

        // Displays the range around a unit
        drawRanges();

        // displays selector
        if (selector.isDisplay()) {
            mapRenderer.getBatch().draw(textureSelector,
                selector.getCoordinates().x * 32,
                selector.getCoordinates().y * 32);
        }

        //
        for (Team team : battleManager.getTeams()) {
            for (aUnit unit : team.getUnits()) {
                unit.draw(mapRenderer.getBatch());
            }
        }

        mapRenderer.getBatch().end();

        // 5. Dessiner les barres de vie (par-dessus tout le reste)
        drawHealthBars(camera);
    }

    private void drawHealthBars(OrthographicCamera camera) {
        // Il faut configurer le ShapeRenderer avec la caméra pour qu'il dessine au bon endroit
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Team team : battleManager.getTeams()) {
            for (aUnit unit : team.getUnits()) {
                // CONDITION : On ne dessine que si l'unité est blessée
                if (unit.getHp() < unit.getMaxHp()) {

                    float x = unit.getX();
                    float y = unit.getY() + unit.getHeight() + 5; // 5 pixels au-dessus de la tête
                    float width = unit.getWidth();
                    float height = 4; // Hauteur de la barre

                    // Calcul du pourcentage de vie
                    float hpPercent = (float) unit.getHp() / unit.getMaxHp();

                    // Fond Rouge (Dégâts)
                    shapeRenderer.setColor(Color.RED);
                    shapeRenderer.rect(x, y, width, height);

                    // Premier plan Vert (Vie restante)
                    shapeRenderer.setColor(Color.GREEN);
                    shapeRenderer.rect(x, y, width * hpPercent, height);
                }
            }
        }
        shapeRenderer.end();
    }

    private void drawRanges() {
        Array<Vector2> moves = battleManager.getMovementRange();
        Array<Vector2> attacks = battleManager.getAttackRange();

        if (moves != null) {
            for (Vector2 pos : moves) {
                greenSquare.setPosition(pos.x * 32, pos.y * 32);
                greenSquare.draw(mapRenderer.getBatch());
            }
        }

        if (attacks != null) {
            for (Vector2 pos : attacks) {
                // Évite de dessiner du rouge par dessus du vert
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
        shapeRenderer.dispose(); // Ne pas oublier de nettoyer le ShapeRenderer
        textureSelector.dispose();
        if (greenSquare.getTexture() != null) greenSquare.getTexture().dispose();
        if (redSquare.getTexture() != null) redSquare.getTexture().dispose();
    }
}
