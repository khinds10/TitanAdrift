package com.kevinhinds.taskblaster;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.kevinhinds.taskblaster.tiles.TileManager;

public class ResourceManager {

	private static final ResourceManager INSTANCE = new ResourceManager();

	public Engine engine;
	public TaskBlasterActivity activity;
	public Camera camera;
	public VertexBufferObjectManager vbom;

	private BuildableBitmapTextureAtlas menuTextureAtlas;
	public ITextureRegion play_button_region;
	public ITextureRegion exit_button_region;

	private BuildableBitmapTextureAtlas gameTextureAtlas;
	public ITextureRegion player_region;
	public ITextureRegion control_knob_region;
	public ITextureRegion control_base_region;
	public ITextureRegion control_jump_region;

	private BuildableBitmapTextureAtlas tileTextureAtlas;
	public ITextureRegion grass_region;
	public ITextureRegion grass_platform_region;

	public TileManager tileManager;

	public void loadMenuResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		play_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play_button.png");
		exit_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "exit_button.png");
		try {
			menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			menuTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	public void unloadMenuResources() {
		menuTextureAtlas.unload();
		menuTextureAtlas = null;
	}

	public void loadGameResources() {
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		player_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "character/player.png");
		control_knob_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/control_knob.png");
		control_base_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/control_base.png");
		control_jump_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/control_knob.png");

		try {
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			gameTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	public void loadTileResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/tiles/");
		tileTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		grass_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tileTextureAtlas, activity, "grass.png");
		grass_platform_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tileTextureAtlas, activity, "grass_platform.png");
		try {
			tileTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			tileTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	public void unloadGameResources() {
		gameTextureAtlas.unload();
		gameTextureAtlas = null;
	}

	public void loadFonts() {

	}

	public void loadTileManager() {
		loadTileResources();
		tileManager = new TileManager(vbom);
	}

	public static void prepareManager(Engine engine, TaskBlasterActivity activity, Camera camera, VertexBufferObjectManager vbom) {
		getIntance().engine = engine;
		getIntance().activity = activity;
		getIntance().camera = camera;
		getIntance().vbom = vbom;
	}

	public static ResourceManager getIntance() {
		return INSTANCE;
	}
}
