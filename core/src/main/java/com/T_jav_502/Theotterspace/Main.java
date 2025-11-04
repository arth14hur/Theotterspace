package com.T_jav_502.Theotterspace;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys; // NOUVEL IMPORT pour Gdx.input
import com.badlogic.gdx.graphics.GL20; // NOUVEL IMPORT pour Gdx.gl.glClear
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite; // NOUVEL IMPORT
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.T_jav_502.Theotterspace.tiles.*;
//import com.T_jav_502.Theotterspace.buildings.*;
//import com.T_jav_502.Theotterspace.units.*;
/**
 * @jllink com.badlogic.gdx.ApplicationListener implementation shared by all platforms. */
public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture loutreTexture;
    private Sprite loutreSprite;
    private float VITESSE_LOUTRE = 200f;

    @Override
    public void create() {
        batch = new SpriteBatch();

        loutreTexture = new Texture(Gdx.files.internal("loutrespacemarine.png"));
        loutreSprite = new Sprite(loutreTexture);

        loutreSprite.setSize(128f, 128f);

        loutreSprite.setPosition(0, 0); // Position de départ

    }

    @Override
    public void render() {

        float deltaTime = Gdx.graphics.getDeltaTime();
        float deplacement = VITESSE_LOUTRE * deltaTime;

        float x = loutreSprite.getX();
        float y = loutreSprite.getY();

        if (Gdx.input.isKeyPressed(Keys.W)) {
            y += deplacement;
        }

        if (Gdx.input.isKeyPressed(Keys.S)) {
            y -= deplacement;
        }

        if (Gdx.input.isKeyPressed(Keys.A)) {
            x -= deplacement;
        }

        if (Gdx.input.isKeyPressed(Keys.D)) {
            x += deplacement;
        }

        loutreSprite.setPosition(x, y);

        Gdx.gl.glClearColor(0.1f, 0.2f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        loutreSprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        loutreTexture.dispose();
    }
}
