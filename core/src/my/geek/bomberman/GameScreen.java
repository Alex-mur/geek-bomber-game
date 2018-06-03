package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

    private float botCreationTimer;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public Map getMap() {
        return map;
    }

    public BotEmitter getBotEmitter() {
        return botEmitter;
    }

    @Override
    public void show() {
        map = new Map(this);
        animationEmitter = new AnimationEmitter();
        botEmitter = new BotEmitter(this);
        botCreationTimer = 10;

        player = new ManActor(this);
        font32 = Assets.getInstance().getAssetManager().get("gomarice32.ttf", BitmapFont.class);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        map.render(batch);
        Mgmt.renderActiveActors(batch);
        animationEmitter.render(batch);
        player.renderGUI(batch, font32);
        batch.end();
    }

    public void update(float dt) {

        botCreationTimer += dt;
        if (botCreationTimer >= 5) {
            botEmitter.createBotInRandomPosition();
            botCreationTimer = 0;
        }

        Mgmt.updateActiveActors(dt);
        animationEmitter.update(dt);
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

