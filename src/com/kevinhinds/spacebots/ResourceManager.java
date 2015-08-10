package com.kevinhinds.spacebots;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
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
import android.graphics.Color;

import com.kevinhinds.spacebots.actors.ActorsSet;
import com.kevinhinds.spacebots.level.AdversaryManager;
import com.kevinhinds.spacebots.level.LevelManager;
import com.kevinhinds.spacebots.tiles.TileSet;

/**
 * deal with game resources via singleton design pattern for re-usability
 * 
 * @author khinds
 */
public class ResourceManager {

	private static final ResourceManager INSTANCE = new ResourceManager();

	/** universal resources */
	public Engine engine;
	public SpaceBotsActivity activity;
	public Camera camera;
	public VertexBufferObjectManager vbom;
	public Font gameFont;
	public LevelManager levelManager;
	public AdversaryManager adversaryManager;

	/** menu regions */
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	public ITextureRegion play_button_region;
	public ITextureRegion exit_button_region;
	public ITextureRegion back_button_region;
	public ITextureRegion title_region;
	public ITextureRegion level_select_region;

	/** player region */
	public ITiledTextureRegion player_region;

	/** weapon region */
	public ITiledTextureRegion bullet_region;

	/** adversary region */
	public ITiledTextureRegion adversary_region;
	public ITiledTextureRegion explosion_region;

	/** controls regions */
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	public ITextureRegion control_jump_region;
	public ITextureRegion control_shoot_region;
	public ITextureRegion control_left_region;
	public ITextureRegion control_right_region;

	/** level regions */
	private BuildableBitmapTextureAtlas tileTextureAtlas;
	public ITiledTextureRegion platform_region;
	public TileSet tileManager;
	public ActorsSet actorsManager;

	/**
	 * load resources to create the menu into memory
	 */
	public void loadMenuResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		play_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu/play.png");
		exit_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu/exit.png");
		title_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu/title.png");
		level_select_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu/select.png");
		back_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu/back.png");
		
		/** have to run everything through black pawn to render it visibly */
		try {
			menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			menuTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	/**
	 * load resources for the game into memory
	 */
	public void loadGameResources() {

		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);
		
		/** player and weapons */
		player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "character/player.png", GameConfiguation.playerMapColumns, GameConfiguation.playerMapRows);
		bullet_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "weapons/bullet.png", 4, 1);
		
		/** adversaries and explosions */
		adversary_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "adversary/creatures.png", GameConfiguation.actorMapColumns, GameConfiguation.actorMapRows);
		explosion_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "explosions/explosions.png", GameConfiguation.explosionMapColumns, GameConfiguation.explosionMapRows);
		
		/** game controls */
		control_jump_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/jump.png");
		control_left_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/left.png");
		control_right_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/right.png");
		control_shoot_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "controls/shoot.png");
		
		/** have to run everything through black pawn to render it visibly */
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
		platform_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(tileTextureAtlas, activity, "platforms.png", 22, 13);
		
		/** have to run everything through black pawn to render it visibly */
		try {
			tileTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			tileTextureAtlas.load();
		} catch (Exception e) {
			Debug.e(e);
		}
	}

	/**
	 * produce the local tile manager to render level tiles via XML based descriptions
	 */
	public void loadTileManager() {
		loadTileResources();
		tileManager = new TileSet(vbom);
		actorsManager = new ActorsSet(vbom);
	}

	/**
	 * load fonts for the game
	 */
	public void loadFonts() {
		FontFactory.setAssetBasePath("fonts/");
		final ITexture fontTexture = new BitmapTextureAtlas(this.engine.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		gameFont = FontFactory.createFromAsset(this.engine.getFontManager(), fontTexture, activity.getAssets(), "game.ttf", 40, true, Color.BLACK);
		gameFont.load();
	}

	/**
	 * prepare the manager to use via singleton in the future
	 * 
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 */
	public static void prepareManager(Engine engine, SpaceBotsActivity activity, Camera camera, VertexBufferObjectManager vbom) {
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