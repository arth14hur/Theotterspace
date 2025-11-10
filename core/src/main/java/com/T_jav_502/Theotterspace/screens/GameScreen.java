package com.T_jav_502.Theotterspace.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen implements Screen {

    private OrthographicCamera camera;
    private FitViewport viewport;

    private final float TILE_SIZE = 64f;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;


    public GameScreen() {

        map = new TmxMapLoader().load("../assets/Maps/test.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();

        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();

        camera.position.set(1280 / 2f, 720 / 2f, 0);

    }

    @Override
    public void render(float delta) {
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.setView((OrthographicCamera) viewport.getCamera());
        renderer.render();


        float camX = camera.position.x;
        float camY = camera.position.y;

        float viewWidth  = camera.viewportWidth * camera.zoom;
        float viewHeight = camera.viewportHeight * camera.zoom;

        float minX = camX - viewWidth / 2;
        float maxX = camX + viewWidth / 2;
        float minY = camY - viewHeight / 2;
        float maxY = camY + viewHeight / 2;



    }

    @Override public void resize(int width, int height) { viewport.update(width, height); }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
