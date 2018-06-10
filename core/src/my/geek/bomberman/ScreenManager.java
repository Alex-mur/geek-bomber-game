package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.*;

public class ScreenManager {
    public enum ScreenType {
        MENU, GAME, MAP_EDITOR, GAME_OVER, NEXT_LEVEL;
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
    private NextLevelScreen nextLevelScreen;

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
        this.viewport = new FitViewport(1280, 720, camera);
        this.menuScreen = new MenuScreen(batch);
        this.gameScreen = new GameScreen(batch, camera, currentLevelID);
        this.mapEditorScreen = new MapEditorScreen(batch, camera, 20, 20);
        this.gameOverScreen = new GameOverScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
        this.nextLevelScreen = new NextLevelScreen(batch);
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
        resetCamera();
        if (screen != null) {
            screen.dispose();
        }
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
                break;
            case NEXT_LEVEL:
                targetScreen = nextLevelScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAME_OVER:
                targetScreen = gameOverScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME_OVER);
                break;
        }
    }

    public void nextLevel() {
        currentLevelID += 1;
        if (currentLevelID > 3)
            currentLevelID = 1;
        gameScreen.changeMap(currentLevelID);
        System.out.println(currentLevelID);
        changeScreen(ScreenType.GAME);
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
