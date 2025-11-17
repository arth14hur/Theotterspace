package com.T_jav_502.Theotterspace.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.T_jav_502.Theotterspace.screens.GameScreen;
import com.T_jav_502.Theotterspace.tiles.Selector;

/**
 * Gère les entrées utilisateur (clavier et souris) pour l'écran de jeu.
 * Remplace la gestion anonyme précédente dans GameScreen pour réduire le couplage.
 */
public class GameInputProcessor extends InputAdapter {

    private final GameScreen gameScreen;
    private final Selector selector;
    private final CameraDrag cameraDrag;
    private final CameraZoom cameraZoom;
    private final float mapWidth; // Limite X (en tuiles)
    private final float mapHeight; // Limite Y (en tuiles)

    /**
     * Constructeur du processeur d'entrées.
     *
     * @param gameScreen Instance de l'écran de jeu (pour la pause)
     * @param selector Le sélecteur de tuiles à déplacer
     * @param cameraDrag Gestionnaire de déplacement de caméra
     * @param cameraZoom Gestionnaire de zoom de caméra
     * @param mapWidth Largeur de la carte pour limiter le sélecteur
     * @param mapHeight Hauteur de la carte pour limiter le sélecteur
     */
    public GameInputProcessor(GameScreen gameScreen, Selector selector, CameraDrag cameraDrag, CameraZoom cameraZoom, float mapWidth, float mapHeight) {
        this.gameScreen = gameScreen;
        this.selector = selector;
        this.cameraDrag = cameraDrag;
        this.cameraZoom = cameraZoom;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    @Override
    public boolean keyDown(int keycode) {
        // Gestion de la Pause (Echap)
        if (keycode == Input.Keys.ESCAPE) {
            // Note: Assurez-vous que togglePause() est accessible (public/package) dans GameScreen
            gameScreen.togglePause(!gameScreen.isPaused());
            return true;
        }

        // Si le jeu est en pause, on ne traite pas les autres touches
        if (gameScreen.isPaused()) {
            return false;
        }

        // Déplacement du sélecteur (Flèches directionnelles)
        Vector2 co = selector.getCoordinates();
        if (keycode == Input.Keys.DOWN) {
            selector.setCoordinates(new Vector2(co.x, co.y - 1), mapWidth, mapHeight);
            return true;
        } else if (keycode == Input.Keys.UP) {
            selector.setCoordinates(new Vector2(co.x, co.y + 1), mapWidth, mapHeight);
            return true;
        } else if (keycode == Input.Keys.LEFT) {
            selector.setCoordinates(new Vector2(co.x - 1, co.y), mapWidth, mapHeight);
            return true;
        } else if (keycode == Input.Keys.RIGHT) {
            selector.setCoordinates(new Vector2(co.x + 1, co.y), mapWidth, mapHeight);
            return true;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Gestion du déplacement de la caméra (Drag)
        if (!gameScreen.isPaused()) {
            cameraDrag.update();
        }
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        // Gestion du Zoom (Molette)
        if (!gameScreen.isPaused()) {
            cameraZoom.zoom(amountY * 0.1f, Gdx.input.getX(), Gdx.input.getY());
        }
        return true;
    }
}
