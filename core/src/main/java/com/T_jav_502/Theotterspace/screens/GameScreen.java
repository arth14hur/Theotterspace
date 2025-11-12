package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Map;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.util.List;

public class GameScreen implements Screen {

    private final OrthographicCamera camera;
    private final FitViewport viewport;

    private final OrthogonalTiledMapRenderer renderer;
    private Map map = new Map("../assets/Maps/mapTuto.json");
    private Stage stage;

    public GameScreen(TiledMap tiledMap, Map map, int scale) {
        renderer = new OrthogonalTiledMapRenderer(tiledMap, scale);
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();
        camera.position.set(1280 / 2f, 720 / 2f, 0);
        this.map = map;

        stage = new TiledMapStage(tiledMap, scale);
        Gdx.input.setInputProcessor(stage);
        stage.getViewport().setCamera(camera);
    }

    @Override
    public void render(float delta) {
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.setView((OrthographicCamera) viewport.getCamera());
        renderer.render();
        stage.act();

        renderer.getBatch().begin();
        for (Team team : map.getTeams()) {
            for (aUnit unit : team.getUnits()) {
                unit.draw(renderer.getBatch());
            }
        }
        renderer.getBatch().end();
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(width / 2f, height / 2f, 0);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        renderer.getMap().dispose();
        renderer.dispose();
    }
}
