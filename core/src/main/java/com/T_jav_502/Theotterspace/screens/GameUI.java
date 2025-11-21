package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameUI implements Disposable {

    private final Stage stage;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final ScreenViewport uiViewport;
    private Label pauseLabel;
    private TextButton continueButton, quitButton;
    private TextButton endTurnButton;

    public GameUI(Main main, GameScreen gameScreen, FitViewport viewport) {
        this.uiViewport = new ScreenViewport();
        this.stage = new Stage(uiViewport);
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        createPauseMenu(main, gameScreen, buttonStyle);
        createHUD(gameScreen, buttonStyle);
    }

    private void createPauseMenu(Main main, GameScreen gameScreen, TextButton.TextButtonStyle buttonStyle) {
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        pauseLabel = new Label("PAUSED", labelStyle);
        pauseLabel.setFontScale(2f);
        pauseLabel.setVisible(false);

        continueButton = new TextButton("Continue", buttonStyle);
        continueButton.setVisible(false);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.togglePause(false);
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

        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.center();
        pauseTable.add(pauseLabel).padBottom(30).row();
        pauseTable.add(continueButton).width(200).height(40).padBottom(10).row();
        pauseTable.add(quitButton).width(200).height(40);

        stage.addActor(pauseTable);
    }

    private void createHUD(GameScreen gameScreen, TextButton.TextButtonStyle buttonStyle) {
        Table hudTable = new Table();
        hudTable.setFillParent(true);
        hudTable.bottom().right();

        endTurnButton = new TextButton("Next Turn", buttonStyle);
        endTurnButton.getLabel().setFontScale(1.2f);

        endTurnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!gameScreen.isPaused()) {
                    gameScreen.endTurn();
                }
            }
        });
        hudTable.add(endTurnButton).pad(20);
        stage.addActor(hudTable);
    }

    public void setPaused(boolean paused) {
        pauseLabel.setVisible(paused);
        continueButton.setVisible(paused);
        quitButton.setVisible(paused);
        endTurnButton.setVisible(!paused);
    }

    public void render(float delta, boolean isPaused) {
        stage.getViewport().apply();
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

    public Stage getStage() {
        return stage;
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
