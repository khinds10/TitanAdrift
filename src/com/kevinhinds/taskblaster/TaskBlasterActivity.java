package com.kevinhinds.taskblaster;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.ui.activity.BaseGameActivity;

import com.kevinhinds.taskblaster.scene.SceneManager;

import android.view.KeyEvent;
import android.widget.Toast;

public class TaskBlasterActivity extends BaseGameActivity {

	private Camera camera;

	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, 800, 480);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(800, 480), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);

		if (!MultiTouch.isSupported(this)) {
			Toast.makeText(this, "Sorry, your device does not support multitouch to play.", Toast.LENGTH_LONG).show();
		}

		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback cb) throws Exception {
		ResourceManager.prepareManager(getEngine(), this, camera, getVertexBufferObjectManager());
		cb.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback cb) throws Exception {
		SceneManager.getInstance().setMenuScene(cb);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback cb) throws Exception {
		cb.onPopulateSceneFinished();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SceneManager.getInstance().getCurrentScene().onBackPressed();
		}
		return false;
	}

}
