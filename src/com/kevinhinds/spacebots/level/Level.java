package com.kevinhinds.spacebots.level;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.kevinhinds.spacebots.objects.Actor;
import com.kevinhinds.spacebots.objects.Bullet;
import com.kevinhinds.spacebots.objects.Item;
import com.kevinhinds.spacebots.objects.Piece;
import com.kevinhinds.spacebots.objects.Tile;

/**
 * basic level object that attaches all actors and respective tiles requested for the game scene
 * 
 * @author khinds
 */
public class Level {

	public int width, height;
	public ArrayList<Tile> tiles = new ArrayList<Tile>();
	public ArrayList<Actor> actors = new ArrayList<Actor>();
	public ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	public ArrayList<Piece> pieces = new ArrayList<Piece>();

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
	 * add a new tile to the level
	 * 
	 * @param t
	 */
	public void addTile(Tile t) {
		tiles.add(t);
	}

	/**
	 * add a new actor to the level
	 * 
	 * @param t
	 */
	public void addActor(Actor v) {
		actors.add(v);
	}

	/**
	 * add a new item to the level
	 * 
	 * @param t
	 */
	public void addItem(Item i) {
		items.add(i);
	}
	
	/**
	 * add a new piece to the level
	 * 
	 * @param t
	 */
	public void addPiece(Piece i) {
		pieces.add(i);
	}
	
	/**
	 * add a new bullet to the level
	 * 
	 * @param t
	 */
	public void addBullet(Bullet i) {
		bullets.add(i);
	}

	/**
	 * find a particular actor by name
	 * 
	 * @param id
	 * @return
	 */
	public Actor getActorByName(String name) {
		for (Actor v : actors)
			if (v.getName().equals(name))
				return v;
		return null;
	}
	
	/**
	 * find a particular actor by name
	 * 
	 * @param name
	 * @return
	 */
	public Item getItemByName(String name) {
		for (Item i : items)
			if (i.getName().equals(name))
				return i;
		return null;
	}
	
	/**
	 * find a particular piece by name
	 * 
	 * @param name
	 * @return
	 */
	public Piece getPieceByName(String name) {
		for (Piece p : pieces)
			if (p.getName().equals(name))
				return p;
		return null;
	}
	
	/**
	 * find a particular bullet by name
	 * 
	 * @param name
	 * @return
	 */
	public Bullet getBulletByName(String name) {
		for (Bullet b : bullets)
			if (b.getName().equals(name))
				return b;
		return null;
	}

	/**
	 * for all the tiles, actors and items currently present in the current level, attach them to the scene in question
	 * 
	 * @param scene
	 * @param physicsWorld
	 */
	public void load(Scene scene, PhysicsWorld physicsWorld) {
		for (Tile t : tiles) {
			t.createBodyAndAttach(scene, physicsWorld);
		}
		for (Actor v : actors) {
			v.createBodyAndAttach(scene, physicsWorld);
		}
		for (Item i : items) {
			i.createBodyAndAttach(scene, physicsWorld);
		}
		for (Piece p : pieces) {
			p.createBodyAndAttach(scene, physicsWorld);
		}
		scene.sortChildren();
	}
}