package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MapEditorScreen implements Screen {

    private SpriteBatch batch;
    private Map map;
    private MapEditor mapEditor;
    private BitmapFont font32;


    public MapEditorScreen(SpriteBatch batch) {
        this.batch = batch;
    }
    public Map getMap() {
        return map;
    }

    @Override
    public void show() {
        map = new Map(0);
        mapEditor = new MapEditor(this);
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
        batch.end();
    }

    public void update(float dt) {
        mapEditor.update();
        Mgmt.updateActiveActors(dt);
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
