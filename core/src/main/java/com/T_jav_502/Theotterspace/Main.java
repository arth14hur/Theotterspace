package com.T_jav_502.Theotterspace;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture loutreTexture;
    private Sprite loutreSprite;

    private OrthographicCamera camera;
    private Viewport viewport;

    // Résolution virtuelle (au choix)
    private final float VIRTUAL_WIDTH = 1280;
    private final float VIRTUAL_HEIGHT = 720;

    private float VITESSE_LOUTRE = 200f;

    @Override
    public void create() {

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();

        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        camera.update();

        batch = new SpriteBatch();

        loutreTexture = new Texture(Gdx.files.internal("loutrespacemarine.png"));
        loutreSprite = new Sprite(loutreTexture);
        loutreSprite.setSize(128f, 128f);
        loutreSprite.setPosition(0, 0);
    }

    @Override
    public void render() {

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        float deltaTime = Gdx.graphics.getDeltaTime();
        float deplacement = VITESSE_LOUTRE * deltaTime;

        float x = loutreSprite.getX();
        float y = loutreSprite.getY();

        if (Gdx.input.isKeyPressed(Keys.W)) y += deplacement;
        if (Gdx.input.isKeyPressed(Keys.S)) y -= deplacement;
        if (Gdx.input.isKeyPressed(Keys.A)) x -= deplacement;
        if (Gdx.input.isKeyPressed(Keys.D)) x += deplacement;

        loutreSprite.setPosition(x, y);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        loutreSprite.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        loutreTexture.dispose();
    }
}
