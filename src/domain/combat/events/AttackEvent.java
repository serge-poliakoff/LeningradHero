package domain.combat.events;

import java.util.Objects;

import domain.combat.Attack;

public record AttackEvent(Object issuer, Attack attack) {
	public AttackEvent {
		Objects.requireNonNull(issuer);
		Objects.requireNonNull(attack);
	}
}
