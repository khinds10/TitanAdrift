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
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import com.kevinhinds.taskblaster.tiles.TileManager;

/**
 * deal with game resources via singleton design pattern for reuseability
 * 
 * @author khinds
 */
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
	public ITiledTextureRegion player_region;

	public ITextureRegion control_jump_region;
	public ITextureRegion control_left_region;
	public ITextureRegion control_right_region;

	private BuildableBitmapTextureAtlas tileTextureAtlas;
	public ITextureRegion grass_region;
	public ITextureRegion grass_platform_region;

	public TileManager tileManager;

	/**
	 * load resources to create the menu into memory
	 */
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

	/**
	 * unload the menu after you've started the game scene
	 */
	public void unloadMenuResources() {
		menuTextureAtlas.unload();
		menuTextureAtlas = null;
	}

	/**
	 * load resources for the game into memory
	 */
	public void loadGameResources() {
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "character/player.png", 8, 3);
		control_jump_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/control_knob.png");		
		control_left_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/control_knob.png");
		control_right_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/control_knob.png");

		try {
			gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			gameTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	/**
	 * load game tiles into memory for usage in multiple game scenes
	 */
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

	/**
	 * unload the game resource if return to main menu
	 */
	public void unloadGameResources() {
		gameTextureAtlas.unload();
		gameTextureAtlas = null;
	}

	/**
	 * load game fonts
	 */
	public void loadFonts() {

	}

	/**
	 * produce the local tile manager to render level tiles via XML based descriptions
	 */
	public void loadTileManager() {
		loadTileResources();
		tileManager = new TileManager(vbom);
	}

	/**
	 * prepare the manager to use via singleton in the future
	 * 
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 */
	public static void prepareManager(Engine engine, TaskBlasterActivity activity, Camera camera, VertexBufferObjectManager vbom) {
		getIntance().engine = engine;
		getIntance().activity = activity;
		getIntance().camera = camera;
		getIntance().vbom = vbom;
	}

	/**
	 * get existing resources from the resource manager via singleton
	 * 
	 * @return
	 */
	public static ResourceManager getIntance() {
		return INSTANCE;
	}
}