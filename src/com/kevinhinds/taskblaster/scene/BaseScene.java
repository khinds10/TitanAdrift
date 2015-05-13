package com.kevinhinds.taskblaster.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.kevinhinds.taskblaster.TaskBlasterActivity;
import com.kevinhinds.taskblaster.ResourceManager;

public abstract class BaseScene extends Scene {

	protected Engine engine;
	protected VertexBufferObjectManager vbom;
	protected TaskBlasterActivity activity;
	protected Camera camera;

	public BaseScene() {
		engine = ResourceManager.getIntance().engine;
		vbom = ResourceManager.getIntance().vbom;
		activity = ResourceManager.getIntance().activity;
		camera = ResourceManager.getIntance().camera;
	}

	protected Sprite createSprite(float x, float y, ITextureRegion region, VertexBufferObjectManager vbom) {
		Sprite sprite = new Sprite(x, y, region, vbom) {
			protected void preDraw(GLState glState, Camera camera){
				super.preDraw(glState, camera);
				glState.enableDither();
			}
		};
		return sprite;
	}

	public abstract void createScene();

	public abstract void onBackPressed();

	public abstract void disposeScene();
}
