package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.PlaceUnit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.T_jav_502.Theotterspace.Main;
import com.T_jav_502.Theotterspace.Map;

public class TitleScreen implements Screen {

    private final Main game;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font;

    public TitleScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // font par défaut
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        Label title = new Label("THE OTTER SPACE", labelStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        TextButton startButton = new TextButton("Commencer", buttonStyle);
        TextButton quitButton  = new TextButton("Quitter", buttonStyle);

        startButton.addListener(event -> {
            if (!startButton.isPressed()) return false;
            Map map = new Map("../assets/Maps/mapTuto.json");
            PlaceUnit placeUnit = new PlaceUnit();
            game.setScreen(new GameScreen(map, placeUnit));
            return true;
        });

        quitButton.addListener(event -> {
            if (!quitButton.isPressed()) return false;
            Gdx.app.exit();
            return true;
        });

        table.add(title).padBottom(40);
        table.row();
        table.add(startButton).width(200).height(40).padBottom(20);
        table.row();
        table.add(quitButton).width(200).height(40);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
        font.dispose();
        batch.dispose();
    }
}
