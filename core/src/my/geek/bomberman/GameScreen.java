package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Map map;
    private ManActor player;
    private AnimationEmitter animationEmitter;
    private BotEmitter botEmitter;
    private BitmapFont font32;
    private Camera camera;
    private int levelID;

    private float botCreationTimer;

    public GameScreen(SpriteBatch batch, Camera camera, int levelID) {
        this.batch = batch;
        this.camera = camera;
        this.levelID = levelID;
    }

    public Map getMap() {
        return map;
    }

    public BotEmitter getBotEmitter() {
        return botEmitter;
    }

    @Override
    public void show() {
        map = new Map(this, Map.Level.getLevelByID(levelID).getMapPath());
        animationEmitter = new AnimationEmitter();
        botEmitter = new BotEmitter(this);
        player = new ManActor(this);
        font32 = Assets.getInstance().getAssetManager().get("gomarice32.ttf", BitmapFont.class);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        map.render(batch);
        Mgmt.renderActiveActors(batch);
        animationEmitter.render(batch);
        ScreenManager.getInstance().resetCamera();
        player.renderGUI(batch, font32);
        batch.end();
    }

    public void update(float dt) {
        cameraUpdate();
        Mgmt.updateActiveActors(dt);
        animationEmitter.update(dt);
    }

    public void cameraUpdate() {
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}

