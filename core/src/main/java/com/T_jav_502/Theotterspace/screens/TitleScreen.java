package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.T_jav_502.Theotterspace.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TitleScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Texture backgroundTexture;

    public TitleScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("TitleScreen.png"));
        Image background = new Image(backgroundTexture);
        background.setScaling(Scaling.fill);
        background.setFillParent(true);
        stage.addActor(background);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();

        Button startButton = new Button(buttonStyle);
        Button quitButton = new Button(buttonStyle);

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                TiledMap tiledMap = new TmxMapLoader().load("Maps/test.tmx");
                game.setScreen(new GameScreen(game, tiledMap, 1));
            }
        });

        float buttonSize = 300f;
        float buttonPadding = 50f;

        table.add(startButton).size(buttonSize).padRight(buttonPadding);
        table.add(quitButton).size(buttonSize).padLeft(buttonPadding);
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
        backgroundTexture.dispose();
    }
}
