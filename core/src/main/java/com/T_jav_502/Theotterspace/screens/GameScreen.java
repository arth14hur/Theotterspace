package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.T_jav_502.Theotterspace.inputs.ClickPosition;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.tiles.Selector;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.T_jav_502.Theotterspace.inputs.CameraDrag;
import com.T_jav_502.Theotterspace.inputs.CameraZoom;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Represents the main in-game screen responsible for rendering the map,
 * units, and handling gameplay logic such as camera controls and pausing.
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *   <li>Renders the Tiled map and all {@link aUnit} instances for each {@link Team}.</li>
 *   <li>Supports camera dragging and zooming via mouse input.</li>
 *   <li>Includes a pause system that overlays a dimmed UI while preserving unit rendering.</li>
 *   <li>Allows resuming or returning to the {@link TitleScreen} using the pause menu.</li>
 *   <li>ESC key toggles pause state.</li>
 * </ul>
 *
 * <p>When paused, the game world remains visible — including all units —
 * but player input affecting gameplay is suspended until resumed.</p>
 */
public class GameScreen implements Screen {

    /** Reference to the main application */
    private final Main main;

    /** Camera used to render the world */
    private final OrthographicCamera camera;

    /** Viewport maintaining aspect ratio */
    private final FitViewport viewport;

    /** Tiled map renderer */
    private final OrthogonalTiledMapRenderer renderer;

    /** Stage for UI (pause menu, etc.) */
    private final Stage stage;

    //private final Map map;
    private final CameraDrag cameraDrag;

    /** Handles mouse scroll zoom */
    private final CameraZoom cameraZoom;
    private final ClickPosition clickPosition;

    /** Font used for UI elements */
    private final BitmapFont font = new BitmapFont();
    private final Selector selector ;
    private final Texture textureselector ;
    private final float maxX ;
    private final float maxY ;
    /** Renderer for semi-transparent overlay when paused */
    private final ShapeRenderer shapeRenderer;

    /** Zoom constraints */
    private final float MIN_ZOOM = 0.3f, MAX_ZOOM = 3f;

    /** Whether the game is currently paused */
    private boolean isPaused = false;

    /** Pause menu UI elements */
    private Label pauseLabel;
    private TextButton continueButton, quitButton;

    /**
     * Constructs the main gameplay screen.
     *
     * @param main Reference to the main application
     * @param tiledMap Map file loaded via {@link com.badlogic.gdx.maps.tiled.TmxMapLoader}
     * //@param map Logical map containing teams and units
     * @param scale Scale factor for map rendering
     */
    public GameScreen(Main main, TiledMap tiledMap, int scale) {
        this.main = main;
        //this.map = map;
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap, scale);
        this.maxX = tiledMap.getProperties().get("width", Integer.class);
        this.maxY = tiledMap.getProperties().get("height", Integer.class);

        this.selector = new Selector();
        this.textureselector = new Texture(Gdx.files.internal("Select2.png"));
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1280, 720, camera);
        this.stage = new Stage(new FitViewport(1280, 720));

        this.cameraDrag = new CameraDrag(camera);
        this.cameraZoom = new CameraZoom(camera, MIN_ZOOM, MAX_ZOOM);
        this.shapeRenderer = new ShapeRenderer();

        camera.position.set(1280 / 2f, 720 / 2f, 0);
        this.clickPosition = new ClickPosition(camera);

        createUI();
        setupInput();
    }

    /**
     * Creates the pause UI elements (title + buttons).
     */
    private void createUI() {
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        pauseLabel = new Label("PAUSED", labelStyle);
        pauseLabel.setFontScale(2f);
        pauseLabel.setVisible(false);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        continueButton = new TextButton("Continue", buttonStyle);
        quitButton = new TextButton("Return to Menu", buttonStyle);

        continueButton.setVisible(false);
        quitButton.setVisible(false);

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause(false);
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new TitleScreen(main));
            }
        });

        Table table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.center();
        table.add(pauseLabel).padBottom(30).row();
        table.add(continueButton).width(200).height(40).padBottom(10).row();
        table.add(quitButton).width(200).height(40);

        stage.addActor(table);
    }

    /**
     * Sets up combined input handling for UI, camera, and pause key.
     */
    private void setupInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (!isPaused) cameraDrag.update();
                return true;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (!isPaused) cameraZoom.zoom(amountY * 0.1f, Gdx.input.getX(), Gdx.input.getY());
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                clickPosition.update(selector, maxX , maxY);
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.ESCAPE) {
                    togglePause(!isPaused);
                }
                if (keycode == Input.Keys.DOWN) {
                    Vector2 co = selector.getCoordinate();
                    selector.setCoordinate(new Vector2(co.x, co.y-1), maxX , maxY);
                    System.out.println(selector.getCoordinate());
                    return true;
                }
                else if (keycode == Input.Keys.UP) {
                    Vector2 co = selector.getCoordinate();
                    selector.setCoordinate(new Vector2(co.x, co.y+1), maxX , maxY);
                    System.out.println(selector.getCoordinate());
                    return true;
                }
                else if (keycode == Input.Keys.LEFT) {
                    Vector2 co = selector.getCoordinate();
                    selector.setCoordinate(new Vector2(co.x-1, co.y), maxX , maxY);
                    System.out.println(selector.getCoordinate());
                    return true;
                }
                else if (keycode == Input.Keys.RIGHT) {
                    Vector2 co = selector.getCoordinate();
                    selector.setCoordinate(new Vector2(co.x+1, co.y), maxX , maxY);
                    System.out.println(selector.getCoordinate());
                    return true;
                }
                else {
                    return false;
                }
            }


        });
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Enables or disables the paused state.
     *
     * @param pause True to pause, false to resume
     */
    private void togglePause(boolean pause) {
        isPaused = pause;
        pauseLabel.setVisible(pause);
        continueButton.setVisible(pause);
        quitButton.setVisible(pause);
    }

    /**
     * Renders the game world, units, and pause overlay if active.
     */
    @Override
    public void render(float delta) {
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        renderer.setView(camera);

        // Always render the map
        renderer.render();

        stage.act(delta);
        stage.draw();

//        renderer.getBatch().begin();
//        for (Team team : map.getTeams()) {
//            for (aUnit unit : team.getUnits()) {
//                unit.draw(renderer.getBatch());
//            }
//        }
//        renderer.getBatch().end();
        // Always render units (even when paused)

        // Semi-transparent overlay when paused
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
        renderer.getBatch().begin();
        if (selector.isDisplay()) {
            renderer.getBatch().draw(textureselector, selector.getCoordinate().x*32, selector.getCoordinate().y*32);
        }
        renderer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(width / 2f, height / 2f, 0);
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /** Frees all allocated resources. */
    @Override
    public void dispose() {
        renderer.getMap().dispose();
        renderer.dispose();
        stage.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }


}
