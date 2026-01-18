package Game.bag.GI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.github.forax.zen.*;
import com.github.forax.zen.KeyboardEvent.Action;
import com.github.forax.zen.KeyboardEvent.Key;

import Game.bag.Bag;
import Game.bag.items.Baggable;
import Game.rendering.BaseLayoutInformation;
import domain.DrawableObject;
import domain.DI.ServiceResolver;
import domain.Graphics.Vector2;
import domain.eventing.EventBus;
import domain.playerInput.KeyboardHandler;
import domain.playerInput.clicking.OnClickEvent;
import domain.playerInput.dragndrop.DragDropEvent;
import domain.playerInput.dragndrop.DragManager;
import domain.playerInput.dragndrop.DragStartEvent;
import domain.playerInput.dragndrop.DroppableArea;

//SPACE for accept
public class BagGI extends DroppableArea implements KeyboardHandler{
	private Bag bagManager;
	private Baggable inHand;
	
	//computed on creation
	private int maxXCell;
	private int maxYCell;
	public final float bagGridScale = 1f;
	
	///action, which will be executed when user tries to use the object
	///use case: turn off usage of weapons on curse attack
	private Consumer<Baggable> usage;
	
	public BagGI(Bag bagManager){
		Objects.requireNonNull(bagManager);
		
		var layout = ServiceResolver.getService(BaseLayoutInformation.class);
		Vector2 position = layout.bagPosition();
		super(position, new BagRenderer());
		setScale(bagGridScale);
		
		this.bagManager = bagManager;
		
		//maximal index of cells on X and Y axis
		maxXCell = bagManager.getBagSpace().length - 1;
		maxYCell = bagManager.getBagSpace()[0].length - 1;
		
		//may as well just put it into DroppableArea
		Consumer<OnClickEvent> clickListener = ev -> {
			if(this.getActive()) {
				onClicked(ev);
			}
		};
		addListener(OnClickEvent.class, clickListener);
		
		enableUsage();
		onEnabled();
	}
	
	///disables use of objects inside the bag, so that only rearrangement is possible
	public void disableUsage() {
		usage = x -> {};
	}
	
	///enables use of objects inside the bag
	public void enableUsage() {
		usage = x -> {x.use();};
	}
	
	@Override
	protected void onEnabled() {
		bagManager.getEquipement().forEach(
				(item) -> item.setActive(true));
	}
	
	@Override
	protected void onDisabled() {
		bagManager.getEquipement().forEach(
				(item) -> item.setActive(false));
	}
	
	private void onClicked(OnClickEvent ev) {
		if (bagManager.getEquipement().contains(ev.issuer())) {
			inHand = (Baggable)ev.issuer();
			IO.println("Click found to be " + ev.issuer());
		}
	}
	
	
	@Override
	protected void onDrag(DragStartEvent ev) {
		if (ev.captured() instanceof Baggable) {
			var captured = (Baggable)ev.captured();
			
			var present = bagManager.contains(captured);
			if (!present) return;
			
			var place = captured.getPosition();
			captured.setGetBack(() -> {
				tryAddAtPlace(captured, place);
			});
			
			bagManager.dropItem(captured);
			captured.resetParent();
			inHand = null;
		}
	}

	@Override
	protected void onDrop(DragDropEvent ev) {
		if (!(ev.dropped() instanceof Baggable)) return;
		
		var dropPos = ev.dropped().getPosition();
		var pos = this.getPosition();
		var corner = pos.add(new Vector2(maxXCell, maxYCell));
		//IO.println("BAG GI: catched drop of Baggable " + ev.dropped() + " at " + dropPos);
		//IO.println("BAG GI: checking if within bound " + pos + " " + corner);
		if (Vector2.isInsideRect(dropPos, pos, corner)) {
			var item = (Baggable)ev.dropped();
			var res = tryAdd(item);
			IO.println("BAG GI: result of adding " + item + " to bag: " + res);
			if (res) {
				var dragManager = ServiceResolver.getService(DragManager.class);
				if (dragManager == null)
					throw new RuntimeException("No drag manager in Di");	//Di exception or Engine exception
				dragManager.signalDropSuccess();
			}
		}
		
	}
	
	private Boolean tryAdd(Baggable item) {
		item.setParent(this);
		//IO.println("Bag GI: adding " + item + " at rel pos " + item.getPosition());
		var res = bagManager.tryAddItem(item);
		if (res)
			return true;
		item.resetParent();
		return false;
	}
	
	private Boolean tryAddAtPlace(Baggable item, Vector2 pos) {
		item.setParent(this);
		item.moveTo(pos);
		//IO.println("Bag GI: adding " + item + " at rel pos " + item.getPosition());
		var res = bagManager.tryAddItem(item);
		if (res)
			return true;
		item.resetParent();
		return false;
	}
	
	@Override
	protected final void Update() {
		((BagRenderer)getRenderer()).setBagSpace(bagManager.getBagSpace());
	}
	
	
	public boolean handleKey(KeyboardEvent ev) {
		if (!this.getActive()) return false;
		if (ev.action() != Action.KEY_PRESSED) return false;
		var key = ev.key();
		switch(key) {
			case Key.SPACE:
				if (inHand != null)
					usage.accept(inHand);
				return true;
			case Key.ESCAPE:
				inHand = null;
				this.setActive(false);
				return true;
			default:
				return false;
		}
	}
	
	protected void onDispose() {
		
	}
}
