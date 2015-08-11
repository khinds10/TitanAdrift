package com.kevinhinds.spacebots.level;

import java.io.IOException;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.SAXUtils;
import org.andengine.util.level.IEntityLoader;
import org.andengine.util.level.LevelLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.xml.sax.Attributes;

import com.kevinhinds.spacebots.ResourceManager;
import com.kevinhinds.spacebots.objects.Actor;
import com.kevinhinds.spacebots.objects.Tile;

import android.content.res.AssetManager;

public class LevelXMLBuilder {
	private final LevelLoader levelLoader;
	private final AssetManager assetManager;
	public Level level;

	private static final String TAG_TILE = "tile";
	private static final String TAG_TILE_ATTR_X = "x";
	private static final String TAG_TILE_ATTR_Y = "y";
	private static final String TAG_TILE_ATTR_TILE = "block";
	private static final String TAG_TILE_TYPE_TILE = "type";

	private static final String TAG_ACTOR = "adversary";
	private static final String TAG_ACTOR_ATTR_X = "x";
	private static final String TAG_ACTOR_ATTR_Y = "y";
	private static final String TAG_ACTOR_ATTR_TILE = "villain";
	private static final String TAG_ACTOR_TYPE_TILE = "type";
	private static final String TAG_ACTOR_STRING_WEAPON = "weapon";
	private static final String TAG_ACTOR_STRING_SHOOTS = "shoots";
	private static final String TAG_ACTOR_STRING_LIFE = "life";
	private static final String TAG_ACTOR_STRING_FACING = "facing";
	private static final String TAG_ACTOR_ATTR_ANIMATION_SPEED = "animationSpeed";
	private static final String TAG_ACTOR_ATTR_MOTION_SPEED = "movementSpeed";
	private static final String TAG_ACTOR_ATTR_EXPLOSION_TYPE = "explosion";

	/**
	 * construct level manager which will load into memory all the level specified by XML for the game
	 * 
	 * @param assetManager
	 */
	public LevelXMLBuilder(AssetManager assetManager) {
		levelLoader = new LevelLoader();
		levelLoader.setAssetBasePath("levels/");
		this.assetManager = assetManager;
	}

	/**
	 * load level information from XML via SAX parser
	 */
	public void createLevelFromXML(int levelNumber) {

		level = new Level();
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
				Tile t = ResourceManager.getIntance().getGameTileById(id);
				level.addTile(t.getInstance(x, y, type));
				return null;
			}
		});

		try {
			levelLoader.loadLevelFromAsset(assetManager, "level" + Integer.toString(levelNumber) + "_platforms.xml");
		} catch (IOException e) {

		}

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

		levelLoader.registerEntityLoader(TAG_ACTOR, new IEntityLoader() {

			@Override
			public IEntity onLoadEntity(final String name, final Attributes attr) {
				final int x = SAXUtils.getIntAttributeOrThrow(attr, TAG_ACTOR_ATTR_X);
				final int y = SAXUtils.getIntAttributeOrThrow(attr, TAG_ACTOR_ATTR_Y);
				final int id = SAXUtils.getIntAttributeOrThrow(attr, TAG_ACTOR_ATTR_TILE);
				final String type = SAXUtils.getAttributeOrThrow(attr, TAG_ACTOR_TYPE_TILE);
				final int life = SAXUtils.getIntAttributeOrThrow(attr, TAG_ACTOR_STRING_LIFE);
				final String weapon = SAXUtils.getAttributeOrThrow(attr, TAG_ACTOR_STRING_WEAPON);
				final String facing = SAXUtils.getAttributeOrThrow(attr, TAG_ACTOR_STRING_FACING);
				final Boolean shoots = SAXUtils.getBooleanAttributeOrThrow(attr, TAG_ACTOR_STRING_SHOOTS);
				final int animationSpeed = SAXUtils.getIntAttributeOrThrow(attr, TAG_ACTOR_ATTR_ANIMATION_SPEED);
				final int movementSpeed = SAXUtils.getIntAttributeOrThrow(attr, TAG_ACTOR_ATTR_MOTION_SPEED);
				final int explosionType = SAXUtils.getIntAttributeOrThrow(attr, TAG_ACTOR_ATTR_EXPLOSION_TYPE);
				Actor v = ResourceManager.getIntance().getGameActorById(id);

				/** add new villain with a unique name based on current XML options set */
				level.addActor(v.getInstance(x, y, type, life, weapon, facing, shoots, "Actor: " + Float.toString(x) + "-" + Float.toString(y) + "-" + Integer.toString(id), animationSpeed, movementSpeed, explosionType));
				return null;
			}
		});

		try {
			levelLoader.loadLevelFromAsset(assetManager, "level" + Integer.toString(levelNumber) + "_actors.xml");
		} catch (IOException e) {

		}
	}

	/**
	 * load level by id and apply it to the scene in question
	 * 
	 * @param id
	 * @param scene
	 * @param physicsWorld
	 */
	public void loadLevel(Scene scene, PhysicsWorld physicsWorld) {
		level.load(scene, physicsWorld);
	}
}
