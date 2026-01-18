package Game.resources.RatMeat;

import java.util.Objects;

import Game.bag.items.Baggable;
import Game.player.Player;
import Game.rendering.BaseLayoutInformation;
import domain.DI.ServiceResolver;

public class RatMeat extends Baggable{

    public RatMeat() {

        int cost = 12;
        var shape = new int[][]{
            new int[]{ 1 }
        };
        var renderer = new RatMeatRenderer();
        var description = new String[]{
            "I doubt i want to eat this...",
            " however...."
        };
        var initPos = ServiceResolver.getService(BaseLayoutInformation.class).bagPosition();
        super(shape, cost, description, initPos, renderer);
        renderer.setGameObject(this);
    }

    private final int healAmount = 2;
    @Override
    public Boolean dropPossible() {
        return true;
    }

    @Override
    public Boolean rotatePossible() {
        return true;
    }

    @Override
    public void use() {
        if (!this.getActive()) return;

        var playerResources = ServiceResolver.getService(Player.class).getResources();
        playerResources.heal(healAmount);
		var bag = Objects.requireNonNull(getBag());
		this.setActive(false);
		bag.dropItem(this);
		dispose();
    }

    @Override
    protected void onObjectDispose() {
        
    }
    
}
