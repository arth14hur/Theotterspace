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
 * pauses the game. Displays a dimmed background and allows resuming
 * the game or returning to the title screen.
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *   <li>Displays a semi-transparent background overlay.</li>
 *   <li>Provides "Continue" and "Return to Menu" buttons.</li>
 *   <li>Can resume gameplay or navigate to {@link TitleScreen}.</li>
 * </ul>
 */
public class PausedScreen implements Screen {

    /** Reference to the main application */
    private final Main main;

    /** Stage for displaying pause UI elements */
    private final Stage stage;

    /** Font used for labels and buttons */
    private final BitmapFont font = new BitmapFont();

    /** ShapeRenderer for drawing the translucent overlay */
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    /** The game screen to return to when resuming */
    private final GameScreen gameScreen;

    /**
     * Constructs a pause screen overlay.
     *
     * @param main       Reference to the main application
     * @param gameScreen The current {@link GameScreen} to resume later
     */
    public PausedScreen(Main main, GameScreen gameScreen) {
        this.main = main;
        this.gameScreen = gameScreen;
        this.stage = new Stage(new FitViewport(1280, 720));

        createUI();
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Creates the pause menu UI components and layout.
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

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /** Disposes all resources used by this screen. */
    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
