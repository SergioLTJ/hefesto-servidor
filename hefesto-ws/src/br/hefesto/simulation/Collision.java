package br.hefesto.simulation;

import br.law123.collide.CollisionData;

/**
 * Representação de uma possível colisão.
 * 
 * @author teixeira
 */
public class Collision {

	private final String id;
	private CollisionType type;
	private final CollisionData data;
	private HRigidBody rb1;
	private HRigidBody rb2;
	private boolean enable = true;

	public Collision(String id, CollisionType type, CollisionData data,
			HRigidBody rb1, HRigidBody rb2) {
		this.id = id;
		this.type = type;
		this.data = data;
		this.rb1 = rb1;
		this.rb2 = rb2;
	}

	public String getId() {
		return id;
	}

	public CollisionType getType() {
		return type;
	}

	public void setType(CollisionType type) {
		this.type = type;
	}

	public CollisionData getData() {
		return data;
	}

	public HRigidBody getRb1() {
		return rb1;
	}

	public void setRb1(HRigidBody rb1) {
		this.rb1 = rb1;
	}

	public HRigidBody getRb2() {
		return rb2;
	}

	public void setRb2(HRigidBody rb2) {
		this.rb2 = rb2;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public boolean isEnable() {
		return enable;
	}

}
