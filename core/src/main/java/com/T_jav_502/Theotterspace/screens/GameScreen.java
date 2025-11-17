package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.T_jav_502.Theotterspace.inputs.CameraDrag;
import com.T_jav_502.Theotterspace.inputs.CameraZoom;
import com.T_jav_502.Theotterspace.inputs.ClickPosition;
import com.T_jav_502.Theotterspace.inputs.GameInputProcessor;
import com.T_jav_502.Theotterspace.logic.BattleManager;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.tiles.Selector;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {

    private final Main main;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final Stage stage;

    // --- Managers ---
    private final BattleManager battleManager;
    private final GameInputProcessor inputProcessor;

    // --- Tools ---
    private final Selector selector;
    private final CameraDrag cameraDrag;
    private final CameraZoom cameraZoom;
    private final ClickPosition clickPosition;

    // --- UI & Assets ---
    private final BitmapFont font = new BitmapFont();
    private final Sprite greenSquare;
    private final Sprite redSquare;
    private final Texture textureSelector;
    private final ShapeRenderer shapeRenderer;
    private Label pauseLabel;
    private TextButton continueButton, quitButton;

    private boolean isPaused = false;
    private final float mapWidth;
    private final float mapHeight;

    public GameScreen(Main main, TiledMap tiledMap, int scale) {
        this.main = main;
        this.map = tiledMap;
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap, scale);

        // Map Dimensions
        this.mapWidth = tiledMap.getProperties().get("width", Integer.class);
        this.mapHeight = tiledMap.getProperties().get("height", Integer.class);

        // Camera Setup
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1280, 720, camera);
        camera.position.set(1280 / 2f, 720 / 2f, 0);

        // Initialize Tools
        this.stage = new Stage(new FitViewport(1280, 720));
        this.selector = new Selector();
        this.clickPosition = new ClickPosition(camera);
        this.cameraDrag = new CameraDrag(camera);
        this.cameraZoom = new CameraZoom(camera, 0.3f, 3f);
        this.shapeRenderer = new ShapeRenderer();

        // Load Assets
        this.textureSelector = new Texture(Gdx.files.internal("Select2.png"));
        this.greenSquare = new Sprite(new Texture(Gdx.files.internal("green.png")));
        this.redSquare = new Sprite(new Texture(Gdx.files.internal("red.png")));

        // Initialize Logic
        this.battleManager = new BattleManager(tiledMap);

        // Initialize Input
        this.inputProcessor = new GameInputProcessor(this, selector, cameraDrag, cameraZoom, mapWidth, mapHeight);
        setupInput();
        createUI();
    }

    private void setupInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage); // UI First
        multiplexer.addProcessor(inputProcessor); // Game logic Second
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void createUI() {
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        pauseLabel = new Label("PAUSED", labelStyle);
        pauseLabel.setFontScale(2f);
        pauseLabel.setVisible(false);

        continueButton = new TextButton("Continue", buttonStyle);
        continueButton.setVisible(false);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { togglePause(false); }
        });

        quitButton = new TextButton("Return to Menu", buttonStyle);
        quitButton.setVisible(false);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) { main.setScreen(new TitleScreen(main)); }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(pauseLabel).padBottom(30).row();
        table.add(continueButton).width(200).height(40).padBottom(10).row();
        table.add(quitButton).width(200).height(40);

        stage.addActor(table);
    }

    public void togglePause(boolean pause) {
        isPaused = pause;
        pauseLabel.setVisible(pause);
        continueButton.setVisible(pause);
        quitButton.setVisible(pause);
    }

    public boolean isPaused() { return isPaused; }

    @Override
    public void render(float delta) {
        // 1. UPDATE
        camera.update();

        if (!isPaused) {
            // Mouse Handling via clickPosition -> BattleManager
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                Vector2 tilePos = clickPosition.update();
                selector.setCoordinates(tilePos, mapWidth, mapHeight);
                battleManager.selectTile(tilePos);
            }
            else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                Vector2 tilePos = clickPosition.update();
                battleManager.actionAtTile(tilePos);
            }

            // Game Logic Update
            battleManager.update(delta);
        }

        // 2. DRAW
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        renderer.setView(camera);
        renderer.render(new int[]{0, 1, 2}); // Draw Map Layers

        renderer.getBatch().begin();

        // Draw Ranges (from BattleManager)
        Array<Vector2> moves = battleManager.getMovementRange();
        if (moves != null) {
            for (Vector2 pos : moves) {
                greenSquare.setPosition(pos.x * 32, pos.y * 32);
                greenSquare.draw(renderer.getBatch());
            }
        }

        Array<Vector2> attacks = battleManager.getAttackRange();
        if (attacks != null) {
            for (Vector2 pos : attacks) {
                redSquare.setPosition(pos.x * 32, pos.y * 32);
                redSquare.draw(renderer.getBatch());
            }
        }

        // Draw Selector
        if (selector.isDisplay()) {
            renderer.getBatch().draw(textureSelector, selector.getCoordinates().x * 32, selector.getCoordinates().y * 32);
        }

        // Draw Units (Iterate through teams)
        for (Team team : battleManager.getTeams()) {
            for (aUnit unit : team.getUnits()) {
                unit.draw(renderer.getBatch());
            }
        }

        renderer.getBatch().end();

        // 3. DRAW UI (Pause Overlay)
        if (isPaused) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 0.5f);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        renderer.dispose();
        map.dispose();
        stage.dispose();
        font.dispose();
        shapeRenderer.dispose();
        textureSelector.dispose();
        greenSquare.getTexture().dispose();
        redSquare.getTexture().dispose();
    }
}
