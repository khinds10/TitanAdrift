package com.kevinhinds.taskblaster.villains;

import java.util.ArrayList;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.kevinhinds.taskblaster.GameConfiguation;
import com.kevinhinds.taskblaster.ResourceManager;

/**
 * create tiles to attach to scenes
 * 
 * @author khinds
 */
public class VillainManager {
	private ArrayList<Villain> villians = new ArrayList<Villain>();

	/**
	 * construct tile manager with predefined tiles
	 * 
	 * @param vbom
	 */
	public VillainManager(VertexBufferObjectManager vbom) {

		/** add all the tiles from the platform map to the list of available tiles */
		for (int i = 0; i <= (GameConfiguation.villainMapColumns * GameConfiguation.villainMapRows); i++) {
			villians.add(new Villain("Villain Image " + Integer.toString(i), i, i, "stationary", 0, "none", "right", false, 0, 0, 10f, 0f, 10f, ResourceManager.getIntance().adversary_region, vbom));
		}
	}

	/**
	 * find a particular tile by name
	 * 
	 * @param id
	 * @return
	 */
	public Villain getVillainByName(String name) {
		for (Villain t : villians)
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
	public Villain getVillainById(int id) {
		for (Villain t : villians)
			if (t.getId() == id)
				return t;
		return null;
	}
}