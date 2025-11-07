package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Map;
import com.T_jav_502.Theotterspace.PlaceUnit;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.tiles.aTile;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.util.List;

public class GameScreen implements Screen {

    private final Map map;
    private PlaceUnit teams;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;

    private final float TILE_SIZE = 64f;

    public GameScreen(Map map, PlaceUnit placeUnit) {
        this.map = map;
        this.teams = placeUnit;
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 720, camera);
        viewport.apply();

        camera.position.set(1280 / 2f, 720 / 2f, 0);

        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        aTile[][] grid = map.getMap();
        if (grid == null) {
            System.out.println("Map grid is null !");
            batch.end();
            return;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                aTile tile = grid[i][j];
                if (tile == null) continue; // évite les crashs

                batch.draw(
                    tile.getTexture(),
                    j * TILE_SIZE,
                    (rows - 1 - i) * TILE_SIZE,
                    TILE_SIZE,
                    TILE_SIZE
                );
            }
        }
        List<Team> teamList = teams.getTeams();
        for (Team team : teamList) {
            List<aUnit> unitList = team.getUnits();
            for (aUnit unit : unitList) {
                Vector2 coordinates = unit.getCoordinates();
                batch.draw(
                    unit.getTexture(),
                    coordinates.x * TILE_SIZE,
                    coordinates.y * TILE_SIZE
                );
            }
        }

        batch.end();
        //batch.begin();
        //aUnit[][] map = PlaceUnit.getMap();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height); }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { batch.dispose(); }
}
