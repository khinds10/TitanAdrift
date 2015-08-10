package com.kevinhinds.spacebots.items;

import java.util.ArrayList;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.kevinhinds.spacebots.GameConfiguation;
import com.kevinhinds.spacebots.ResourceManager;

/**
 * create items to attach to scenes
 * 
 * @author khinds
 */
public class ItemSet {
	private ArrayList<Item> items = new ArrayList<Item>();

	/**
	 * construct tile manager with predefined items
	 * 
	 * @param vbom
	 */
	public ItemSet(VertexBufferObjectManager vbom) {

		/** add all the items from the platform map to the list of available items */
		for (int i = 0; i <= (GameConfiguation.platformMapColumns * GameConfiguation.platformMapRows); i++) {
			items.add(new Item("Platform Tile " + Integer.toString(i), i, i, "physical", 0, 0, 0f, 10f, 00f, ResourceManager.getIntance().platform_region, vbom));
		}
	}

	/**
	 * find a particular tile by id
	 * 
	 * @param id
	 * @return
	 */
	public Item getTileById(int id) {
		for (Item t : items)
			if (t.getId() == id)
				return t;
		return null;
	}
}