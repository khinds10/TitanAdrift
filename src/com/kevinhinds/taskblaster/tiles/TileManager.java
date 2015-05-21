package com.kevinhinds.taskblaster.tiles;

import java.util.ArrayList;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.kevinhinds.taskblaster.ResourceManager;

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
		tiles.add(new Tile("Grass", 1, 0, 0, 5, 0f, 0.5f, ResourceManager.getIntance().grass_region, vbom));
		tiles.add(new Tile("Grass Platform", 2, 0, 0, 5, 0f, 0.5f, ResourceManager.getIntance().grass_platform_region, vbom));
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
