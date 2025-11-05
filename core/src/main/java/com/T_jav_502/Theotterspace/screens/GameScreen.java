package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Map;
import com.T_jav_502.Theotterspace.tiles.aTile;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {

    private final Map map;

    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;

    // Taille d’une tuile (dans le repère logique)
    private final float TILE_SIZE = 64f;

    public GameScreen(Map map) {
        this.map = map;

        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera); // résolution virtuelle
        viewport.apply();

        camera.position.set(1280 / 2f, 720 / 2f, 0);

        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        aTile[][] grid = map.getMap();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                aTile tile = grid[i][j];

                if (tile != null) {
                    batch.draw(
                        tile.getTexture(),
                        j * TILE_SIZE,
                        (grid.length - 1 - i) * TILE_SIZE,  // pour inverser Y
                        TILE_SIZE,
                        TILE_SIZE
                    );
                }
            }
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { batch.dispose(); }
}
