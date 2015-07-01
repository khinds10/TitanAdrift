package com.kevinhinds.taskblaster.characters;

import java.util.ArrayList;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.kevinhinds.taskblaster.GameConfiguation;
import com.kevinhinds.taskblaster.ResourceManager;

/**
 * create tiles to attach to scenes
 * 
 * @author khinds
 */
public class CharactersManager {
	private ArrayList<Character> characters = new ArrayList<Character>();

	/**
	 * construct tile manager with predefined tiles
	 * 
	 * @param vbom
	 */
	public CharactersManager(VertexBufferObjectManager vbom) {

		/** add all the tiles from the platform map to the list of available tiles */
		for (int i = 0; i <= (GameConfiguation.characterMapColumns * GameConfiguation.characterMapRows); i++) {
			characters.add(new Character("Character Image " + Integer.toString(i), i, i, "stationary", 0, "none", "right", false, 0, 0, 10f, 0f, 10f, ResourceManager.getIntance().adversary_region, vbom));
		}
	}

	/**
	 * find a particular tile by name
	 * 
	 * @param id
	 * @return
	 */
	public Character getVillainByName(String name) {
		for (Character t : characters)
			if (t.getName().equals(name))
				return t;
		return null;
	}

	/**
	 * find a particular tile by id
	 * 
	 * @param id
	 * @return
	 */
	public Character getVillainById(int id) {
		for (Character t : characters)
			if (t.getId() == id)
				return t;
		return null;
	}
}