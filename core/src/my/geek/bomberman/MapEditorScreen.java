package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.omg.PortableInterceptor.ACTIVE;

public class MapEditorScreen implements Screen {

    private SpriteBatch batch;
    private Map map;
    private MapEditorActor mapEditor;
    private BitmapFont font32;
    private int width;
    private int height;
    private Camera camera;


    public MapEditorScreen(SpriteBatch batch, Camera camera, int width, int height) {
        this.batch = batch;
        this.width = width;
        this.height = height;
        this.camera = camera;
    }
    public Map getMap() {
        return map;
    }


    @Override
    public void show() {
        map = new Map(this, width, height);
        mapEditor = new MapEditorActor(this, camera);
        font32 = Assets.getInstance().getAssetManager().get("gomarice32.ttf", BitmapFont.class);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        map.render(batch);
        ActorsKeeper.getInstance().renderActiveActors(batch);
        ScreenManager.getInstance().resetCamera();
        mapEditor.renderGUI(batch, font32);
        batch.end();
    }

    public void update(float dt) {
        cameraUpdate();
        ActorsKeeper.getInstance().updateActiveActors(dt);
    }

    public void cameraUpdate() {
        camera.position.set(mapEditor.getPosition().x, mapEditor.getPosition().y, 0);
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
