package my.geek.bomberman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class ActorsKeeper {
    private static final ActorsKeeper ourInstance = new ActorsKeeper();
    static ActorsKeeper getInstance() {
        return ourInstance;
    }

    private Set<Actor> activeActorsList;
    private Set<Actor> passiveActorsList;

    private ActorsKeeper() {
        activeActorsList = ConcurrentHashMap.newKeySet();
        passiveActorsList = ConcurrentHashMap.newKeySet();
    }

    public void renderActiveActors(SpriteBatch batch) {
        for (Actor a : activeActorsList) {
            a.render(batch);
        }
    }

    public void updateActiveActors(float dt) {
        for (Actor a : activeActorsList) {
            a.update(dt);
        }
    }

    public Set<Actor> getActiveActorsList() {
        return activeActorsList;
    }

    public Set<Actor> getPassiveActorsList() {
        return passiveActorsList;
    }

    public void clear() {
        activeActorsList.clear();
        passiveActorsList.clear();
    }
}
