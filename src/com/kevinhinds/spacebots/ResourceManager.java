package com.kevinhinds.spacebots;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
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
import org.andengine.util.color.Color;

import com.kevinhinds.spacebots.objects.Actor;
import com.kevinhinds.spacebots.objects.Tile;

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

	/** game fonts */
	public Font gameFont;
	public Font gameFontGray;
	public Font levelSelectFont;
	public Font titleFont;
	public Font menuRedFont;
	public Font menuBlueFont;
	public Font menuGreenFont;

	/** menu regions */
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	public ITextureRegion galaxy_background_region;
	public ITextureRegion illuminate_background_region;
	public ITextureRegion planet_background_region;
	public ITextureRegion void_background_region;
	public ITextureRegion andengine_region;

	/** player region */
	public ITiledTextureRegion player_region;

	/** weapon region */
	public ITiledTextureRegion bullet_region;

	/** adversary region */
	public ITiledTextureRegion actors_region;
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

	/** complete set of the game's actors, tiles and items to render on levels via XML descriptions */
	private ArrayList<Actor> gameActors = new ArrayList<Actor>();
	private ArrayList<Tile> gameTiles = new ArrayList<Tile>();

	/**
	 * load resources to create the menu into memory
	 */
	public void loadMenuResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 4096, 4096, TextureOptions.BILINEAR);
		galaxy_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu/galaxy.jpg");
		illuminate_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu/illuminate.jpg");
		planet_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu/planet.jpg");
		void_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu/void.jpg");
		andengine_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu/andengine.png");

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
		actors_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "actors/creatures.png", GameConfiguation.actorMapColumns, GameConfiguation.actorMapRows);
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

		/** load platforms */
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

		/** add all the gameActors from the platform map to the list of available gameActors */
		for (int i = 0; i <= (GameConfiguation.actorMapColumns * GameConfiguation.actorMapRows); i++) {
			gameActors.add(new Actor("Actor Image " + Integer.toString(i), i, i, "stationary", 0, "none", "right", false, GameConfiguation.actorAnimationSpeed, GameConfiguation.actorMovementSpeed, GameConfiguation.explosionDefault, 0, 0, 10f, 0f, 10f, ResourceManager.getIntance().actors_region, vbom));
		}

		/** add all the gameTiles from the platform map to the list of available gameTiles */
		for (int i = 0; i <= (GameConfiguation.platformMapColumns * GameConfiguation.platformMapRows); i++) {
			gameTiles.add(new Tile("Platform Tile " + Integer.toString(i), i, i, "physical", 0, 0, 0f, 10f, 00f, ResourceManager.getIntance().platform_region, vbom));
		}
	}

	/**
	 * find a particular tile by id
	 * 
	 * @param id
	 * @return
	 */
	public Actor getGameActorById(int id) {
		for (Actor t : gameActors)
			if (t.getId() == id)
				return t;
		return null;
	}

	/**
	 * find a particular tile by id
	 * 
	 * @param id
	 * @return
	 */
	public Tile getGameTileById(int id) {
		for (Tile t : gameTiles)
			if (t.getId() == id)
				return t;
		return null;
	}

	/**
	 * load fonts for the game
	 */
	public void loadFonts() {
		FontFactory.setAssetBasePath("fonts/");

		gameFont = buildFont("game.ttf", 20, android.graphics.Color.argb(255, 255, 255, 255));
		gameFont.load();

		gameFontGray = buildFont("game.ttf", 20, android.graphics.Color.argb(255, 255, 255, 255));
		gameFontGray.load();

		titleFont = buildFont("game.ttf", 60, android.graphics.Color.argb(255, 255, 255, 255));
		titleFont.load();

		menuRedFont = buildFont("game.ttf", 40, android.graphics.Color.argb(255, 255, 255, 255));
		menuRedFont.load();

		menuBlueFont = buildFont("game.ttf", 40, android.graphics.Color.argb(255, 255, 255, 255));
		menuBlueFont.load();

		menuGreenFont = buildFont("game.ttf", 40, android.graphics.Color.argb(255, 255, 255, 255));
		menuGreenFont.load();

		levelSelectFont = buildFont("game.ttf", 30, android.graphics.Color.argb(255, 255, 255, 255));
		levelSelectFont.load();
	}

	/**
	 * build the font from texture and return to apply to game font resources
	 * 
	 * @param fontFile
	 * @param fontSize
	 * @return
	 */
	private Font buildFont(String fontFile, int fontSize, int color) {
		final ITexture levelSelectFontTexture = new BitmapTextureAtlas(this.engine.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		return FontFactory.createFromAsset(this.engine.getFontManager(), levelSelectFontTexture, activity.getAssets(), fontFile, fontSize, true, color);
	}

	/**
	 * create a new menu item from text
	 * 
	 * @param font
	 * @param menuItemText
	 * @param itemId
	 * @param isAnimated
	 * @return
	 */
	public IMenuItem createTextMenuItem(Font font, CharSequence menuItemText, int itemId, boolean isAnimated) {
		Color color = new Color(1, 1, 1, 1);
		IMenuItem menuItem = null;
		if (isAnimated) {
			menuItem = new ScaleMenuItemDecorator(new ColorMenuItemDecorator(new TextMenuItem(itemId, font, menuItemText, vbom), color, color), 1.1f, 1);
		} else {
			menuItem = new ColorMenuItemDecorator(new TextMenuItem(itemId, font, menuItemText, vbom), color, color);
		}
		menuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		return menuItem;
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
