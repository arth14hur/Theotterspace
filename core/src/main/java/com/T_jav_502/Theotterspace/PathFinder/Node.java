package com.T_jav_502.Theotterspace.PathFinder;

import com.badlogic.gdx.math.Vector2;

public class Node {
    public Vector2 position;
    public Node parent;
    public float gCost;
    public float hCost;
    public float fCost;

    public Node(Vector2 position, Node parent, float gCost, float hCost) {
        this.position = position;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
    }
}
