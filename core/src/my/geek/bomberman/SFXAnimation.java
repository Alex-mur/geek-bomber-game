package my.geek.bomberman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

//Class was renamed to not confuse with libgdx Animation class
public class SFXAnimation {
    private Vector2 position;
    private TextureRegion[] keyFrames;
    public int framesCount;
    private float time;
    private float maxTime;
    private float timePerFrame;
    private Vector2 scale;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public SFXAnimation() {
        this.position = new Vector2(0, 0);
        this.keyFrames = null;
        this.framesCount = 0;
        this.time = 0.0f;
        this.maxTime = 0.0f;
        this.timePerFrame = 0.0f;
        this.active = false;
        this.scale = new Vector2(1, 1);
    }

    public void activate(float x, float y, Array<? extends TextureRegion> keyFrames, float timePerFrame, float scaleX, float scaleY) {
        this.position.set(x, y);
        this.keyFrames = new TextureRegion[keyFrames.size];
        for (int i = 0, n = keyFrames.size; i < n; i++) {
            this.keyFrames[i] = keyFrames.get(i);
        }
        framesCount = this.keyFrames.length;
        this.timePerFrame = timePerFrame;
        this.maxTime = timePerFrame * framesCount;
        this.active = true;
        this.scale.set(scaleX, scaleY);
    }

    public void render(SpriteBatch batch) {
        int frameIndex = (int) (time / timePerFrame);
        batch.draw(keyFrames[frameIndex], position.x, position.y,0, 0, keyFrames[frameIndex].getRegionWidth(), keyFrames[frameIndex].getRegionHeight(), scale.x, scale.y, 0.0f);
    }

    public void update(float dt) {
        time += dt;
        if (time >= maxTime) {
            active = false;
            time = 0;
        }
    }
}
