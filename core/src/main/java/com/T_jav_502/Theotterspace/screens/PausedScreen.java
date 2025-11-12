package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Represents the pause overlay screen that appears when the player
 * pauses the game.
 * <p>
 * This screen does not interrupt the rendering of the {@link GameScreen}
 * — the game map and units remain visible in the background — but it
 * overlays a semi-transparent dark layer and UI controls for resuming
 * or leaving the game.
 * </p>
 *
 * <h3>Features</h3>
 * <ul>
 *   <li>Renders a translucent overlay above the active {@link GameScreen}.</li>
 *   <li>Displays "Continue" and "Return to Menu" buttons centered on screen.</li>
 *   <li>Allows resuming gameplay or returning to the {@link TitleScreen}.</li>
 *   <li>Keeps all previously rendered elements from {@link GameScreen} visible underneath.</li>
 * </ul>
 *
 * <h3>Usage</h3>
 * <p>
 * When the player pauses the game, the {@link Main} instance can replace the active
 * {@link GameScreen} with a new {@code PausedScreen}:
 * </p>
 *
 * <pre>{@code
 * main.setScreen(new PausedScreen(main, currentGameScreen));
 * }</pre>
 *
 * When "Continue" is clicked, the screen switches back to the given {@link GameScreen}.
 * When "Return to Menu" is clicked, it transitions to the {@link TitleScreen}.
 *
 * @see GameScreen
 * @see TitleScreen
 */
public class PausedScreen implements Screen {

    /** Reference to the main application controlling screen transitions. */
    private final Main main;

    /** Stage used for managing and rendering pause menu UI components. */
    private final Stage stage;

    /** Font used for rendering the title and button labels. */
    private final BitmapFont font = new BitmapFont();

    /** ShapeRenderer used to draw the translucent background overlay. */
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    /** The {@link GameScreen} instance to return to when resuming gameplay. */
    private final GameScreen gameScreen;

    /**
     * Constructs a pause overlay screen.
     *
     * @param main       the main {@link Main} application instance
     * @param gameScreen the current {@link GameScreen} instance to resume after unpausing
     */
    public PausedScreen(Main main, GameScreen gameScreen) {
        this.main = main;
        this.gameScreen = gameScreen;
        this.stage = new Stage(new FitViewport(1280, 720));

        createUI();
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Creates and configures the pause menu interface, including the title label
     * and action buttons ("Continue" and "Return to Menu").
     */
    private void createUI() {
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        Label title = new Label("PAUSED", labelStyle);
        title.setFontScale(2f);

        TextButton continueButton = new TextButton("Continue", buttonStyle);
        TextButton quitButton = new TextButton("Return to Menu", buttonStyle);

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(gameScreen);
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new TitleScreen(main));
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(title).padBottom(40).row();
        table.add(continueButton).width(200).height(40).padBottom(15).row();
        table.add(quitButton).width(200).height(40);

        stage.addActor(table);
    }

    /**
     * Renders the pause overlay.
     * <p>
     * This method draws a semi-transparent black rectangle over the screen
     * (allowing the {@link GameScreen} to remain visible behind),
     * then renders the pause menu UI.
     * </p>
     *
     * @param delta time in seconds since the last frame
     */
    @Override
    public void render(float delta) {
        // Draw semi-transparent overlay
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.6f);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Draw UI
        stage.act(delta);
        stage.draw();
    }

    /**
     * Adjusts the viewport to handle window resizing.
     *
     * @param width  new window width
     * @param height new window height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /**
     * Releases all LibGDX resources used by this screen, including fonts,
     * the stage, and the overlay renderer.
     */
    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
