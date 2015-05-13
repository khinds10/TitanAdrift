package com.kevinhinds.taskblaster.level;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.kevinhinds.taskblaster.tiles.Tile;

public class Level {

	private final int id;
	private int width, height;
	private final ArrayList<Tile> levelTiles = new ArrayList<Tile>();

	public Level(int id) {
		this.id = id;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getId() {
		return id;
	}

	public void addTile(Tile t) {
		levelTiles.add(t);
	}

	public void load(Scene scene, PhysicsWorld physicsWorld) {
		for (Tile t : levelTiles) {
			t.createBodyAndAttach(scene, physicsWorld);
		}
	}
}
