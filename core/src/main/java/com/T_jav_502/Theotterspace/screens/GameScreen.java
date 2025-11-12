package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Map;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.tiles.Selector;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.T_jav_502.Theotterspace.inputs.CameraDrag;
import com.T_jav_502.Theotterspace.inputs.CameraZoom;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Represents the main game screen, handling rendering of the map, units, and UI, as well as camera controls.
 * <p>
 * This class implements {@link Screen} and is responsible for:
 * <ul>
 *     <li>Rendering a tiled map using {@link OrthogonalTiledMapRenderer}.</li>
 *     <li>Displaying units for each team on the map.</li>
 *     <li>Providing UI buttons for zooming in and out.</li>
 *     <li>Allowing camera movement by dragging and zooming with the mouse.</li>
 * </ul>
 * </p>
 * <p>
 * Camera behavior:
 * <ul>
 *     <li>Drag with the middle mouse button to pan the camera ({@link CameraDrag}).</li>
 *     <li>Scroll or click UI buttons to zoom in and out ({@link CameraZoom}).</li>
 * </ul>
 * </p>
 */
public class GameScreen implements Screen {

    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final OrthogonalTiledMapRenderer renderer;
    private final Stage stage;

    private final Map map;
    private final CameraDrag cameraDrag;
    private final CameraZoom cameraZoom;
    private final BitmapFont font = new BitmapFont();
    private final Selector selector =  new Selector();

    private final float MIN_ZOOM = 0.3f;
    private final float MAX_ZOOM = 3f;
    private final float ZOOM_STEP = 0.1f;

    /**
     * Constructs a new GameScreen.
     *
     * @param tiledMap the {@link TiledMap} to render
     * @param map      the {@link Map} object containing teams and units
     * @param scale    the scale factor for rendering the map
     */
    public GameScreen(TiledMap tiledMap, Map map, int scale) {
        this.map = map;
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap, scale);

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1280, 720, camera);
        viewport.apply();
        camera.position.set(1280 / 2f, 720 / 2f, 0);

        this.stage = new Stage(new FitViewport(1280, 720));

        this.cameraDrag = new CameraDrag(camera);
        this.cameraZoom = new CameraZoom(camera, MIN_ZOOM, MAX_ZOOM);

        createUI();
        setupInput();
    }

    /**
     * Creates the UI elements for the game screen, including zoom buttons.
     */
    private void createUI() {
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        Label zoomInButton = new Label("+", style);
        zoomInButton.setSize(50, 50);
        zoomInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cameraZoom.zoom(-ZOOM_STEP, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
            }
        });

        Label zoomOutButton = new Label("-", style);
        zoomOutButton.setSize(50, 50);
        zoomOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cameraZoom.zoom(ZOOM_STEP, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
            }
        });

        Table table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.pad(10);
        table.add(zoomInButton).size(50, 50).padRight(10);
        table.add(zoomOutButton).size(50, 50);
        stage.addActor(table);
    }

    /**
     * Sets up input processors for UI and camera controls.
     */
    private void setupInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage); // UI
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                cameraDrag.update();
                return true;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                cameraZoom.zoom(amountY * 0.1f, Gdx.input.getX(), Gdx.input.getY());
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.DOWN) {
                    Vector2 co = selector.getCoordinate();
                    selector.setCoordinate(co.set(co.x, co.y-1));
                    System.out.println(selector.getCoordinate());
                    return true;
                }
                else if (keycode == Input.Keys.UP) {
                    Vector2 co = selector.getCoordinate();
                    selector.setCoordinate(co.set(co.x, co.y+1));
                    System.out.println(selector.getCoordinate());
                    return true;
                }
                else if (keycode == Input.Keys.LEFT) {
                    Vector2 co = selector.getCoordinate();
                    selector.setCoordinate(co.set(co.x-1, co.y));
                    System.out.println(selector.getCoordinate());
                    return true;
                }
                else if (keycode == Input.Keys.RIGHT) {
                    Vector2 co = selector.getCoordinate();
                    selector.setCoordinate(co.set(co.x+1, co.y));
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
     * Renders the game screen, including map, units, and UI.
     *
     * @param delta time in seconds since the last frame
     */
    @Override
    public void render(float delta) {
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        renderer.setView(camera);
        renderer.render();

        stage.act(delta);
        stage.draw();

        renderer.getBatch().begin();
        for (Team team : map.getTeams()) {
            for (aUnit unit : team.getUnits()) {
                unit.draw(renderer.getBatch());
            }
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

    /**
     * Disposes resources used by this screen.
     */
    @Override
    public void dispose() {
        renderer.getMap().dispose();
        renderer.dispose();
        stage.dispose();
        font.dispose();
    }
}
