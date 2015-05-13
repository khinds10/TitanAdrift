package com.kevinhinds.taskblaster.level;

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

import com.kevinhinds.taskblaster.ResourceManager;
import com.kevinhinds.taskblaster.tiles.Tile;

public class LevelManager {

	private final LevelLoader levelLoader;
	private final AssetManager assetManager;
	private final ArrayList<Level> levels = new ArrayList<Level>();

	private static final String TAG_TILE = "tile";
	private static final String TAG_TILE_ATTR_X = "x";
	private static final String TAG_TILE_ATTR_Y = "y";
	private static final String TAG_TILE_ATTR_TILE = "tile";

	public LevelManager(AssetManager assetManager) {
		levelLoader = new LevelLoader();
		levelLoader.setAssetBasePath("levels/");
		this.assetManager = assetManager;
		addNewLevel(1, "example.lvl");
	}

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
			public IEntity onLoadEntity(String name, Attributes attr) {
				final int x = SAXUtils.getIntAttributeOrThrow(attr, TAG_TILE_ATTR_X);
				final int y = SAXUtils.getIntAttributeOrThrow(attr, TAG_TILE_ATTR_Y);
				final int id = SAXUtils.getIntAttributeOrThrow(attr, TAG_TILE_ATTR_TILE);
				Tile t = ResourceManager.getIntance().tileManager.getTileById(id);
				level.addTile(t.getInstance(x, y));
				return null;
			}
		});

		try {
			levelLoader.loadLevelFromAsset(assetManager, name);
		} catch (IOException e) {

		}
		levels.add(level);
	}

	public void loadLevel(int id, Scene scene, PhysicsWorld physicsWorld) {
		for (Level level : levels) {
			level.load(scene, physicsWorld);
		}
	}
}
