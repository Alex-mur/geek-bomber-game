package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.*;

public class ScreenManager {
    public enum ScreenType {
        MENU, GAME, MAP_EDITOR, GAME_OVER;
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
    private GameOverScreen gameOverScreen;
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
        currentLevelID = 4;
        this.camera = new OrthographicCamera(1280, 720);
        this.viewport = new FitViewport(1280, 720);
        this.menuScreen = new MenuScreen(batch);
        this.gameScreen = new GameScreen(batch, camera, currentLevelID);
        this.mapEditorScreen = new MapEditorScreen(batch, camera, 5, 5);
        this.gameOverScreen = new GameOverScreen(batch);
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
        ActorsKeeper.getInstance().clear();
        //if (screen != null) {
        //    screen.dispose();
        //}
        resetCamera();
        //game.setScreen(loadingScreen);
        switch (type) {
            case MENU:
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                game.setScreen(targetScreen);
                break;
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                game.setScreen(targetScreen);
                break;
            case MAP_EDITOR:
                targetScreen = mapEditorScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                game.setScreen(targetScreen);
                break;
            case GAME_OVER:
                targetScreen = gameOverScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME_OVER);
                game.setScreen(targetScreen);
                break;
        }
    }

    public void nextLevel() {
        currentLevelID += 1;
        if (currentLevelID > 3)
            currentLevelID = 1;
        ActorsKeeper.getInstance().clear();
        gameScreen.changeMap(currentLevelID);
    }

    public void endGame() {
        currentLevelID = 1;
        changeScreen(ScreenType.GAME_OVER);
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
