package br.hefesto.simulation;

import br.law123.force.Force;
import br.law123.rigidbody.RigidBody;

/**
 * Representação um corpo rígido que pode colidir.
 * 
 * @author teixeira
 */
public class HForce implements Force {

	private final String id;
	private final Force force;

	public HForce(String id, Force force) {
		this.id = id;
		this.force = force;
	}

	public String getId() {
		return id;
	}

	public Force getForce() {
		return force;
	}

	@Override
	public void updateForce(RigidBody body, double duration) {
		force.updateForce(body, duration);
	}

}
