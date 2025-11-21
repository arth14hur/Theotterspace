package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;

public class WinnerScreen implements Screen {

    private final Main main;
    private final Texture winnerTexture;
    private final SpriteBatch batch;

    private final Rectangle nextLevelButton = new Rectangle();
    private final Rectangle quitButton = new Rectangle();

    private float imgX, imgY, imgW, imgH;

    public WinnerScreen(Main main) {
        this.main = main;
        this.winnerTexture = new Texture(Gdx.files.internal("WinScreen.png"));
        this.batch = new SpriteBatch();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(winnerTexture, imgX, imgY, imgW, imgH);
        batch.end();

        handleInput();
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        Vector2 scaledSize = Scaling.fit.apply(
            winnerTexture.getWidth(),
            winnerTexture.getHeight(),
            width,
            height
        );

        this.imgW = scaledSize.x;
        this.imgH = scaledSize.y;
        this.imgX = (width - imgW) / 2;
        this.imgY = (height - imgH) / 2;
        float btnW = imgW * 0.13f;
        float btnH = imgH * 0.20f;
        float btnY = imgY + (imgH * 0.14f);
        float nextX = imgX + (imgW * 0.38f) - (btnW / 2);
        nextLevelButton.set(nextX, btnY, btnW, btnH);
        float quitX = imgX + (imgW * 0.62f) - (btnW / 2);
        quitButton.set(quitX, btnY, btnW, btnH);
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float clickX = Gdx.input.getX();
            float clickY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (quitButton.contains(clickX, clickY)) {
                handleQuit();
            } else if (nextLevelButton.contains(clickX, clickY)) {
                handleNextLevel();
            }
        }
    }

    private void handleQuit() {
        this.dispose();
        main.setScreen(new TitleScreen(main));
    }

    private void handleNextLevel() {
        System.out.println("Next Level Clicked");
    }

    @Override
    public void dispose() {
        winnerTexture.dispose();
        batch.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
