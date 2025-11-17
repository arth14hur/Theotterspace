package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.T_jav_502.Theotterspace.inputs.CameraDrag;
import com.T_jav_502.Theotterspace.inputs.CameraZoom;
import com.T_jav_502.Theotterspace.inputs.ClickPosition;
import com.T_jav_502.Theotterspace.inputs.GameInputProcessor;
import com.T_jav_502.Theotterspace.logic.BattleManager;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.tiles.Selector;
import com.T_jav_502.Theotterspace.units.Blaster;
import com.T_jav_502.Theotterspace.units.Heavy;
import com.T_jav_502.Theotterspace.units.Infantry;
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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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

/**
 * Écran principal du jeu.
 * Responsabilités : Rendu graphique, Initialisation, Lien entre les Entrées et la Logique.
 */
public class GameScreen implements Screen {

    private final Main main;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;
    private final OrthographicCamera camera;
    private final FitViewport viewport;

    // -- Gestion Logique et Entrées --
    private final BattleManager battleManager;
    private final GameInputProcessor inputProcessor;

    // -- Outils d'interface et d'interaction --
    private final Stage stage;
    private final Selector selector;
    private final ClickPosition clickPosition;
    private final CameraDrag cameraDrag;
    private final CameraZoom cameraZoom;

    // -- Ressources graphiques --
    private final Texture textureSelector;
    private final Sprite greenSquare;
    private final Sprite redSquare;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    // -- État du Screen --
    private boolean isPaused = false;
    private final float maxX;
    private final float maxY;

    // -- UI Pause --
    private Label pauseLabel;
    private TextButton continueButton;
    private TextButton quitButton;

    public GameScreen(Main main, TiledMap tiledMap, int scale) {
        this.main = main;
        this.map = tiledMap;
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap, scale);

        this.maxX = tiledMap.getProperties().get("width", Integer.class);
        this.maxY = tiledMap.getProperties().get("height", Integer.class);

        // Initialisation de la caméra et du viewport
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1280, 720, camera);
        camera.position.set(1280 / 2f, 720 / 2f, 0);

        // Initialisation des outils d'interaction
        this.selector = new Selector();
        this.clickPosition = new ClickPosition(camera);
        this.cameraDrag = new CameraDrag(camera);
        this.cameraZoom = new CameraZoom(camera, 0.3f, 3f);

        // Chargement des textures
        this.textureSelector = new Texture(Gdx.files.internal("Select2.png"));
        this.greenSquare = new Sprite(new Texture(Gdx.files.internal("green.png")));
        this.redSquare = new Sprite(new Texture(Gdx.files.internal("red.png")));

        this.stage = new Stage(new FitViewport(1280, 720));
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();

        // --- Initialisation de la Logique (BattleManager) ---
        Array<Team> teams = initializeTeams(tiledMap);
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        this.battleManager = new BattleManager(teams, collisionLayer);

        // --- Configuration des Inputs ---
        this.inputProcessor = new GameInputProcessor(this, selector, cameraDrag, cameraZoom, maxX, maxY);
        setupInput();

        // Création de l'interface
        createUI();
    }

    /**
     * Lit la carte Tiled pour créer les équipes et placer les unités.
     */
    private Array<Team> initializeTeams(TiledMap map) {
        Array<Team> teams = new Array<>();
        teams.add(new Team(Team.Species.OTTER));
        teams.add(new Team(Team.Species.WOLF));

        TiledMapTileLayer unitLayer = (TiledMapTileLayer) map.getLayers().get("Units");
        if (unitLayer != null) {
            for (int x = 0; x < unitLayer.getWidth(); x++) {
                for (int y = 0; y < unitLayer.getHeight(); y++) {
                    TiledMapTileLayer.Cell cell = unitLayer.getCell(x, y);
                    if (cell != null) {
                        String teamName = cell.getTile().getProperties().get("team", String.class);
                        String typeName = cell.getTile().getProperties().get("type", String.class);

                        if (teamName != null && typeName != null) {
                            Team team = teamName.equals("OTTER") ? teams.get(0) : teams.get(1);
                            switch (typeName) {
                                case "Infantry": team.addUnit(new Infantry(team, x, y)); break;
                                case "Blaster": team.addUnit(new Blaster(team, x, y)); break;
                                case "Heavy": team.addUnit(new Heavy(team, x, y)); break;
                            }
                        }
                    }
                }
            }
        }
        return teams;
    }

    private void setupInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage); // UI en priorité
        multiplexer.addProcessor(inputProcessor); // Jeu ensuite
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
            public void clicked(InputEvent event, float x, float y) {
                togglePause(false);
            }
        });

        quitButton = new TextButton("Return to Menu", buttonStyle);
        quitButton.setVisible(false);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new TitleScreen(main));
            }
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
        this.isPaused = pause;
        pauseLabel.setVisible(pause);
        continueButton.setVisible(pause);
        quitButton.setVisible(pause);
    }

    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public void render(float delta) {
        // 1. Mise à jour
        camera.update();

        if (!isPaused) {
            // Gestion des clics (Sélection / Déplacement) via BattleManager
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                Vector2 pos = clickPosition.update();
                selector.setCoordinates(pos, maxX, maxY);
                battleManager.handleInteraction(pos, false); // False = Clic gauche (Sélection)
            }
            else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                Vector2 pos = clickPosition.update();
                battleManager.handleInteraction(pos, true); // True = Clic droit (Action/Move)
            }

            // Mise à jour de la logique de combat (animations, etc.)
            battleManager.update(delta);
        }

        // 2. Rendu Graphique
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        renderer.setView(camera);

        // Rendu de la carte
        renderer.render(new int[]{0, 1, 2});

        renderer.getBatch().begin();

        // Affichage des portées (Mouvement / Attaque)
        // Note: BattleManager utilise MovementCalculator en interne pour remplir ces listes
        Array<Vector2> moves = battleManager.getMovementRange();
        if (moves != null) {
            for (Vector2 pos : moves) {
                greenSquare.setX(pos.x * 32);
                greenSquare.setY(pos.y * 32);
                greenSquare.draw(renderer.getBatch());
            }
        }

        Array<Vector2> attacks = battleManager.getAttackRange();
        if (attacks != null) {
            for (Vector2 pos : attacks) {
                redSquare.setX(pos.x * 32);
                redSquare.setY(pos.y * 32);
                redSquare.draw(renderer.getBatch());
            }
        }

        // Affichage du sélecteur
        if (selector.isDisplay()) {
            renderer.getBatch().draw(textureSelector, selector.getCoordinates().x * 32, selector.getCoordinates().y * 32);
        }

        // Affichage des unités
        for (Team team : battleManager.getTeams()) {
            for (aUnit unit : team.getUnits()) {
                unit.draw(renderer.getBatch());
            }
        }

        renderer.getBatch().end();

        // 3. UI Overlay (Pause)
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

        // Libération des textures manuelles
        textureSelector.dispose();
        if (greenSquare.getTexture() != null) greenSquare.getTexture().dispose();
        if (redSquare.getTexture() != null) redSquare.getTexture().dispose();
    }
}
