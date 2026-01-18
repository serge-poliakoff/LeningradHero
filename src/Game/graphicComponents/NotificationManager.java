package Game.graphicComponents;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

import Game.rendering.BaseLayoutInformation;
import domain.DI.ServiceResolver;
import domain.eventing.EventBus;

public class NotificationManager implements UIComponent{
	private BaseLayoutInformation layout;
	private Queue<NotificationEvent> messages;
	
	public NotificationManager() {
		messages = new ArrayDeque<NotificationEvent>();
		
		Consumer<NotificationEvent> notificationConsumer = ev -> {
			messages.add(ev);
		};
		EventBus.registerListener(NotificationEvent.class, notificationConsumer);
	}
	
	public void renderSelf(Graphics2D gr) {
		if (layout == null) {
			layout = ServiceResolver.getService(BaseLayoutInformation.class);
		}
		int mesPosX = 20;
		int mesPosY = layout.height() - 80;
		gr.clearRect(mesPosX, mesPosY, 400, layout.height() - mesPosY);
		gr.setColor(Color.white);
		while(!messages.isEmpty()) {
			var cur = messages.remove();
			var text = cur.issuer().toString() + " : " + cur.text();
			gr.drawBytes(text.getBytes(), 0, text.length(), mesPosX, mesPosY);
			mesPosY += 25;
		}
		
	}
}
