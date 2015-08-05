package com.kevinhinds.spacebots.actors;

import java.util.ArrayList;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.kevinhinds.spacebots.GameConfiguation;
import com.kevinhinds.spacebots.ResourceManager;

/**
 * create tiles to attach to scenes
 * 
 * @author khinds
 */
public class ActorsManager {
	private ArrayList<Actor> actors = new ArrayList<Actor>();

	/**
	 * construct tile manager with predefined tiles
	 * 
	 * @param vbom
	 */
	public ActorsManager(VertexBufferObjectManager vbom) {

		/** add all the tiles from the platform map to the list of available tiles */
		for (int i = 0; i <= (GameConfiguation.actorMapColumns * GameConfiguation.actorMapRows); i++) {
			actors.add(new Actor("Actor Image " + Integer.toString(i), i, i, "stationary", 0, "none", "right", false, GameConfiguation.villianAnimationSpeed, GameConfiguation.villianMovementSpeed, GameConfiguation.explosionDefault, 0, 0, 10f, 0f, 10f, ResourceManager.getIntance().adversary_region, vbom));
		}
	}

	/**
	 * find a particular tile by name
	 * 
	 * @param id
	 * @return
	 */
	public Actor getVillainByName(String name) {
		for (Actor t : actors)
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
	public Actor getVillainById(int id) {
		for (Actor t : actors)
			if (t.getId() == id)
				return t;
		return null;
	}
}