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

		tiles.add(new Tile("Cave Floor 1", 1, 0, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));
		tiles.add(new Tile("Cave Floor 2", 2, 1, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));
		tiles.add(new Tile("Cave Floor 3", 3, 2, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));
		tiles.add(new Tile("Cave Floor 4", 4, 3, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));

		tiles.add(new Tile("Cave Block 1", 5, 4, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));
		tiles.add(new Tile("Cave Block 2", 6, 5, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));
		tiles.add(new Tile("Cave Block 3", 7, 6, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));
		tiles.add(new Tile("Cave Block 4", 8, 7, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));

		tiles.add(new Tile("Cave Roof Empty", 9, 8, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));
		tiles.add(new Tile("Cave Roof 1"	, 10, 9, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));
		tiles.add(new Tile("Cave Roof 2"	, 11, 10, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));
		tiles.add(new Tile("Cave Roof 3"	, 12, 11, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().cave_platform_region, vbom));

		tiles.add(new Tile("Pipe Large Top Left Corner" , 13, 0, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
		tiles.add(new Tile("Pipe Large Top Center"	    , 14, 1, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
		tiles.add(new Tile("Pipe Large Top Right Corner", 15, 2, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
		
		tiles.add(new Tile("Pipe Large Top Left Joint"  , 16, 3, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
		tiles.add(new Tile("Pipe Large Top Verticle"    , 17, 4, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
		tiles.add(new Tile("Pipe Large Top Right Joint" , 18, 5, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
		
		tiles.add(new Tile("Pipe Small Top Left Corner" , 19, 6, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
		tiles.add(new Tile("Pipe Small Top Center"	    , 20, 7, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
		tiles.add(new Tile("Pipe Small Top Right"	    , 21, 8, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
		
		tiles.add(new Tile("Pipe Small Top Left Joint"  , 22, 9, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
		tiles.add(new Tile("Pipe Small Top Verticle"    , 23, 10, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
		tiles.add(new Tile("Pipe Small Top Right Joint" , 24, 11, 0, 0, 0, 0f, 0f, ResourceManager.getIntance().pipes_platform_region, vbom));
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
