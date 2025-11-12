package com.T_jav_502.Theotterspace.screens;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TiledMapStage extends Stage {

    private TiledMap tiledMap;

    public TiledMapStage(TiledMap tiledMap, int scale) {
        this.tiledMap = tiledMap;
        TiledMapTileLayer tiledLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        createActorsForLayer(tiledLayer, scale);

    }

    private void createActorsForLayer(TiledMapTileLayer tiledLayer, int scale) {
        for (int x = 0; x < tiledLayer.getWidth(); x++) {
            for (int y = 0; y < tiledLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
                TiledMapActor actor = new TiledMapActor(tiledMap, tiledLayer, cell);
                actor.setBounds(x * tiledLayer.getTileWidth()* scale, y * tiledLayer.getTileHeight()* scale, tiledLayer.getTileWidth()* scale,
                    tiledLayer.getTileHeight()* scale);
                addActor(actor);
                EventListener eventListener = new TiledMapClickListener(actor);
                actor.addListener(eventListener);
            }
        }
    }
}

