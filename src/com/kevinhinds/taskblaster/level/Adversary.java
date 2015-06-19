package com.kevinhinds.taskblaster.level;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import com.kevinhinds.taskblaster.villains.Villain;

/**
 * basic level object that attaches itself and respective tiles to the game scene
 * 
 * @author khinds
 */
public class Adversary {

	public final int id;
	public int width, height;
	private final ArrayList<Villain> levelAdversaries = new ArrayList<Villain>();

	/**
	 * create new level by id identifier
	 * 
	 * @param id
	 */
	public Adversary(int id) {
		this.id = id;
	}

	/**
	 * width of the level
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * height of the level
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * return the id of the level
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * add a new tile to the level
	 * 
	 * @param t
	 */
	public void addTile(Villain t) {
		levelAdversaries.add(t);
	}

	/**
	 * for all the tiles currently present in the current level, attach them to the scene in question
	 * 
	 * @param scene
	 * @param physicsWorld
	 */
	public void load(Scene scene, PhysicsWorld physicsWorld) {
		for (Villain v : levelAdversaries) {
			v.createBodyAndAttach(scene, physicsWorld);
		}
		scene.sortChildren();
	}
}