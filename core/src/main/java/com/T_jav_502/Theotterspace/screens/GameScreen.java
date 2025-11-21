package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.T_jav_502.Theotterspace.inputs.*;
import com.T_jav_502.Theotterspace.logic.BattleManager;
import com.T_jav_502.Theotterspace.tiles.Selector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {

    private Main main;
    private final TiledMap map;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final BattleManager battleManager;
    private final WorldRenderer worldRenderer;
    private final GameUI gameUI;
    private final GameInputProcessor inputProcessor;
    private final Selector selector;
    private final ClickPosition clickPosition;
    private final CameraDrag cameraDrag;
    private final CameraZoom cameraZoom;
    private boolean isPaused = false;
    private final float mapWidth, mapHeight;
    private final int scale;
    private final String mapPath = "Maps/test.tmx";

    public GameScreen(Main main, TiledMap tiledMap, int scale) {
        this.main = main;
        this.map = tiledMap;
        this.scale = scale;
        this.mapWidth = tiledMap.getProperties().get("width", Integer.class);
        this.mapHeight = tiledMap.getProperties().get("height", Integer.class);
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1280, 720, camera);
        camera.position.set(1280 / 2f, 720 / 2f, 0);
        this.selector = new Selector();
        this.clickPosition = new ClickPosition(camera);
        this.cameraDrag = new CameraDrag(camera);
        this.cameraZoom = new CameraZoom(camera, 0.3f, 3f);
        this.battleManager = new BattleManager(tiledMap);
        this.worldRenderer = new WorldRenderer(tiledMap, battleManager, selector, scale);
        this.gameUI = new GameUI(main, this, viewport);
        this.inputProcessor = new GameInputProcessor(this, selector, cameraDrag, cameraZoom, mapWidth, mapHeight);
        setupInput();
    }

    private void setupInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameUI.getStage());
        multiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Appelé par l'UI pour finir le tour manuellement.
     */
    public void endTurn() {
        if (battleManager.getTeams().get(1).getUnits().isEmpty()) {
            changeScreenSafe(new WinnerScreen(main));
            return;
        }

        if (battleManager.getTeams().get(0).getUnits().isEmpty()) {
            changeScreenSafe(new LooserScreen(main, mapPath, scale));
            return;
        }

        if (!isPaused) {
            battleManager.endTurn();
        }
    }

    private void changeScreenSafe(Screen nextScreen) {
        Gdx.input.setInputProcessor(null);
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                main.setScreen(nextScreen);
                GameScreen.this.dispose();
            }
        });
    }

    public void togglePause(boolean pause) {
        this.isPaused = pause;
        gameUI.setPaused(pause);
    }

    public boolean isPaused() { return isPaused; }

    @Override
    public void render(float delta) {
        camera.update();

        if (!isPaused) {
            handleInput();
            if (main.getScreen() != this) return;
            battleManager.update(delta);
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (main.getScreen() != this) return;
        viewport.apply();
        worldRenderer.render(camera);
        gameUI.render(delta, isPaused);
    }

    private void handleInput() {

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 pos = clickPosition.update();
            selector.setCoordinates(pos, mapWidth, mapHeight);
            battleManager.selectTile(pos);
        }
        else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            battleManager.actionAtTile(clickPosition.update());
        }
    }

    @Override public void resize(int w, int h) { viewport.update(w, h); gameUI.resize(w, h); }

    @Override
    public void dispose() {
        map.dispose();
        worldRenderer.dispose();
        gameUI.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
