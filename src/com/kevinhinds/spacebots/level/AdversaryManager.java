package com.kevinhinds.spacebots.level;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.SAXUtils;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

import android.content.res.AssetManager;

import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.actors.Actor;

/**
 * manager to load in all game levels via XML descriptions of them
 * 
 * @author khinds
 */
public class AdversaryManager {

	private final LevelLoader levelLoader;
	private final AssetManager assetManager;
	private final ArrayList<Level> levels = new ArrayList<Level>();

	private static final String TAG_TILE = "adversary";
	private static final String TAG_TILE_ATTR_X = "x";
	private static final String TAG_TILE_ATTR_Y = "y";
	private static final String TAG_TILE_ATTR_TILE = "villain";
	private static final String TAG_TILE_TYPE_TILE = "type";
	private static final String TAG_TILE_STRING_WEAPON = "weapon";
	private static final String TAG_TILE_STRING_SHOOTS = "shoots";
	private static final String TAG_TILE_STRING_LIFE = "life";
	private static final String TAG_TILE_STRING_FACING = "facing";
	private static final String TAG_TILE_ATTR_ANIMATION_SPEED = "animationSpeed";
	private static final String TAG_TILE_ATTR_MOTION_SPEED = "movementSpeed";
	private static final String TAG_TILE_ATTR_EXPLOSION_TYPE = "explosion";

	/**
	 * construct level manager which will load into memory all the level specified by XML for the game
	 * 
	 * @param assetManager
	 */
	public AdversaryManager(AssetManager assetManager) {
		levelLoader = new LevelLoader();
		levelLoader.setAssetBasePath("levels/");
		this.assetManager = assetManager;
		addNewLevel(1, "level1_villians.xml");
		addNewLevel(2, "level2_villians.xml");
	}

	/**
	 * load level information from XML via SAX parser
	 * 
	 * @param id
	 * @param name
	 */
	private void addNewLevel(int id, String name) {
		final Level level = new Level(id);

		levelLoader.registerEntityLoader(LevelConstants.TAG_LEVEL, new IEntityLoader() {
			@Override
			public IEntity onLoadEntity(String name, Attributes attr) {
				final int width = SAXUtils.getIntAttributeOrThrow(attr, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(attr, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
				level.setWidth(width);
				level.setHeight(height);
				return null;
			}
		});

		levelLoader.registerEntityLoader(TAG_TILE, new IEntityLoader() {

			@Override
			public IEntity onLoadEntity(final String name, final Attributes attr) {
				final int x = SAXUtils.getIntAttributeOrThrow(attr, TAG_TILE_ATTR_X);
				final int y = SAXUtils.getIntAttributeOrThrow(attr, TAG_TILE_ATTR_Y);
				final int id = SAXUtils.getIntAttributeOrThrow(attr, TAG_TILE_ATTR_TILE);
				final String type = SAXUtils.getAttributeOrThrow(attr, TAG_TILE_TYPE_TILE);
				final int life = SAXUtils.getIntAttributeOrThrow(attr, TAG_TILE_STRING_LIFE);
				final String weapon = SAXUtils.getAttributeOrThrow(attr, TAG_TILE_STRING_WEAPON);
				final String facing = SAXUtils.getAttributeOrThrow(attr, TAG_TILE_STRING_FACING);
				final Boolean shoots = SAXUtils.getBooleanAttributeOrThrow(attr, TAG_TILE_STRING_SHOOTS);
				final int animationSpeed = SAXUtils.getIntAttributeOrThrow(attr, TAG_TILE_ATTR_ANIMATION_SPEED);
				final int movementSpeed = SAXUtils.getIntAttributeOrThrow(attr, TAG_TILE_ATTR_MOTION_SPEED);
				final int explosionType = SAXUtils.getIntAttributeOrThrow(attr, TAG_TILE_ATTR_EXPLOSION_TYPE);
				Actor v = ResourceManager.getIntance().actorsManager.getVillainById(id);

				/** add new villain with a unique name based on current XML options set */
				level.addVillain(v.getInstance(x, y, type, life, weapon, facing, shoots, "Actor: " + Float.toString(x) + "-" + Float.toString(y) + "-" + Integer.toString(id), animationSpeed, movementSpeed, explosionType));
				return null;
			}
		});

		try {
			levelLoader.loadLevelFromAsset(assetManager, name);
		} catch (IOException e) {

		}
		levels.add(level);
	}

	/**
	 * get level by id
	 * 
	 * @param id
	 * @return
	 */
	public Level getLevelById(int id) {
		for (Level l : levels)
			if (l.id == id)
				return l;
		return null;
	}

	/**
	 * load level by id and apply it to the scene in question
	 * 
	 * @param id
	 * @param scene
	 * @param physicsWorld
	 */
	public void loadLevel(int id, Scene scene, PhysicsWorld physicsWorld) {
		Level level = getLevelById(id);
		level.load(scene, physicsWorld);
	}
}