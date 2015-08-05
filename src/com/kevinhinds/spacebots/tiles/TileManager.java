package com.kevinhinds.spacebots.tiles;

import java.util.ArrayList;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.kevinhinds.spacebots.GameConfiguation;
import com.kevinhinds.spacebots.ResourceManager;

/**
 * create tiles to attach to scenes
 * 
 * @author khinds
 */
public class TileManager {
	private ArrayList<Tile> tiles = new ArrayList<Tile>();

	/**
	 * construct tile manager with predefined tiles
	 * 
	 * @param vbom
	 */
	public TileManager(VertexBufferObjectManager vbom) {

		/** add all the tiles from the platform map to the list of available tiles */
		for (int i = 0; i <= (GameConfiguation.platformMapColumns * GameConfiguation.platformMapRows); i++) {
			tiles.add(new Tile("Platform Tile " + Integer.toString(i), i, i, "physical", 0, 0, 0f, 10f, 00f, ResourceManager.getIntance().platform_region, vbom));
		}
	}

	/**
	 * find a particular tile by id
	 * 
	 * @param id
	 * @return
	 */
	public Tile getTileById(int id) {
		for (Tile t : tiles)
			if (t.getId() == id)
				return t;
		return null;
	}
}