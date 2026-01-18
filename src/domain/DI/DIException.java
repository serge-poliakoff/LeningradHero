package domain.DI;

public class DIException extends RuntimeException {
	public DIException(Object notPresent) {
		super("Required serveice " + notPresent.toString() + " was not present in DI container");
	}
}
