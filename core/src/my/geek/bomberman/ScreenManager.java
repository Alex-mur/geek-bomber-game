package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.*;

public class ScreenManager {
    public enum ScreenType {
        MENU, GAME, MAP_EDITOR;
    }

    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    private BomberManGame game;
    private Screen targetScreen;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private MapEditorScreen mapEditorScreen;

    private SpriteBatch batch;
    private Viewport viewport;
    private Camera camera;
    private int currentLevelID;

    public Viewport getViewport() {
        return viewport;
    }

    private ScreenManager() {
    }

    public void init(BomberManGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        currentLevelID = 1;
        this.camera = new OrthographicCamera(1280, 720);
        this.viewport = new FitViewport(1280, 720);
        this.gameScreen = new GameScreen(batch, camera, currentLevelID);
        this.menuScreen = new MenuScreen(batch);
        this.mapEditorScreen = new MapEditorScreen(batch, camera, 1280, 2560);
        this.loadingScreen = new LoadingScreen(batch);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    public void changeScreen(ScreenType type) {
        Screen screen = game.getScreen();
        Gdx.input.setInputProcessor(null); // ?
        Assets.getInstance().clear();
        if (screen != null) {
            screen.dispose();
        }
        resetCamera();
        game.setScreen(loadingScreen);
        switch (type) {
            case MENU:
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
            case MAP_EDITOR:
                targetScreen = mapEditorScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
        }
    }

    public void nextLevel(SpriteBatch batch) {
        currentLevelID += 1;
        game.setScreen(loadingScreen);
        gameScreen = new GameScreen(batch, camera, currentLevelID);
        changeScreen(ScreenType.GAME);
    }

    public void resetCamera() {
        camera.position.set(640, 360, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public void goToTarget() {
        game.setScreen(targetScreen);
    }
}
