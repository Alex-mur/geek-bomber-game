package my.geek.bomberman;

import com.badlogic.gdx.math.MathUtils;

public class BotEmitter {

    private BotActor[] botActors;
    private GameScreen gs;
    
    public BotEmitter(GameScreen gs) {
        this.gs = gs;
        botActors = new BotActor[Mgmt.BOT_QUANTITY];
        for (int i = 0; i < botActors.length; i++) {
            botActors[i] = new BotActor(gs);
        }
    }

    public void createBotInRandomPosition() {
        for (BotActor b : botActors) {
            int cellX = -200;
            int cellY = -200;
            if (!b.isActive) {
                while (true) {
                    cellX = MathUtils.random(1, Map.MAP_CELLS_WIDTH - 1);
                    cellY = MathUtils.random(1, Map.MAP_CELLS_HEIGHT - 1);
                    if (gs.getMap().isCellEmpty(cellX, cellY))
                        break;
                }

                b.activate(cellX, cellY);
                break;
            }
        }
    }
}
