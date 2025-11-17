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

    // --- Composants principaux ---
    private final TiledMap map;
    private final OrthographicCamera camera;
    private final FitViewport viewport;

    // --- Modules (MVC) ---
    private final BattleManager battleManager;  // LOGIQUE
    private final WorldRenderer worldRenderer;  // RENDU (CARTE + UNITÉS)
    private final GameUI gameUI;                // INTERFACE (MENUS)
    private final GameInputProcessor inputProcessor; // ENTRÉES (CLAVIER)

    // --- Outils ---
    private final Selector selector;
    private final ClickPosition clickPosition;
    private final CameraDrag cameraDrag;
    private final CameraZoom cameraZoom;

    private boolean isPaused = false;
    private final float mapWidth, mapHeight;

    public GameScreen(Main main, TiledMap tiledMap, int scale) {
        this.map = tiledMap;
        this.mapWidth = tiledMap.getProperties().get("width", Integer.class);
        this.mapHeight = tiledMap.getProperties().get("height", Integer.class);

        // 1. Init Caméra
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1280, 720, camera);
        camera.position.set(1280 / 2f, 720 / 2f, 0);

        // 2. Init Outils
        this.selector = new Selector();
        this.clickPosition = new ClickPosition(camera);
        this.cameraDrag = new CameraDrag(camera);
        this.cameraZoom = new CameraZoom(camera, 0.3f, 3f);

        // 3. Init Modules
        this.battleManager = new BattleManager(tiledMap);
        this.worldRenderer = new WorldRenderer(tiledMap, battleManager, selector, scale);
        this.gameUI = new GameUI(main, this, viewport);

        // 4. Init Inputs
        this.inputProcessor = new GameInputProcessor(this, selector, cameraDrag, cameraZoom, mapWidth, mapHeight);
        setupInput();
    }

    private void setupInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameUI.getStage()); // UI prioritaire
        multiplexer.addProcessor(inputProcessor);    // Jeu ensuite
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Appelé par l'UI pour finir le tour manuellement.
     */
    public void endTurn() {
        if (!isPaused) {
            battleManager.endTurn();
        }
    }

    public void togglePause(boolean pause) {
        this.isPaused = pause;
        gameUI.setPaused(pause);
    }

    public boolean isPaused() { return isPaused; }

    @Override
    public void render(float delta) {
        // --- 1. Mise à jour (Update) ---
        camera.update();
        if (!isPaused) {
            handleMouse();
            battleManager.update(delta);
        }

        // --- 2. Dessin (Draw) ---
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        worldRenderer.render(camera);       // Dessine le jeu
        gameUI.render(delta, isPaused);     // Dessine l'interface par dessus
    }

    private void handleMouse() {
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
    @Override public void dispose() { map.dispose(); worldRenderer.dispose(); gameUI.dispose(); }

    // Méthodes Screen non utilisées
    @Override public void show() {} @Override public void pause() {}
    @Override public void resume() {} @Override public void hide() {}
}
