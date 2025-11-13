package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Simple loading screen with a progress bar drawn using ShapeRenderer.
 * No external textures required.
 */
public class LoadingScreen implements Screen {

    private final Main game;
    private final AssetManager assetManager;
    private final String mapPath;
    private final int scale;

    private final Stage stage;
    private final Label loadingLabel;
    private final ShapeRenderer shapeRenderer;

    private float progress;

    public LoadingScreen(Main game, String mapPath, int scale) {
        this.game = game;
        this.mapPath = mapPath;
        this.scale = scale;

        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());

        stage = new Stage(new FitViewport(1280, 720));
        shapeRenderer = new ShapeRenderer();

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        loadingLabel = new Label("Loading...", labelStyle);
        loadingLabel.setPosition(540, 400);
        stage.addActor(loadingLabel);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        assetManager.load(mapPath, TiledMap.class);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update loading progress
        if (assetManager.update()) {
            try {
                TiledMap tiledMap = assetManager.get(mapPath, TiledMap.class);
                System.out.println("TMX loaded successfully: " + mapPath);
                game.setScreen(new GameScreen(game, tiledMap, scale));
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed to load TMX: " + mapPath);
            }
            dispose();
            return;
        }

        progress = assetManager.getProgress();

        // Draw progress bar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float barWidth = 400;
        float barHeight = 30;
        float x = (1280 - barWidth) / 2f;
        float y = 300;

        // Background (gray)
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, barWidth, barHeight);

        // Progress (blue)
        shapeRenderer.setColor(Color.SKY);
        shapeRenderer.rect(x, y, barWidth * progress, barHeight);

        shapeRenderer.end();

        // Update label
        loadingLabel.setText("Loading... " + (int) (progress * 100) + "%");

        stage.act(delta);
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
        assetManager.dispose();
    }
}
