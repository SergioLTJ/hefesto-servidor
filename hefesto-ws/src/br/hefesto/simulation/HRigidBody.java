package br.hefesto.simulation;

import br.law123.collide.CollisionPrimitive;
import br.law123.collide.util.BoxCollisor;
import br.law123.collide.util.SphereCollisor;
import br.law123.core.Vector3;
import br.law123.rigidbody.RigidBody;
import br.law123.rigidbody.contact.Contact;

/**
 * Representação um corpo rígido que pode colidir.
 * 
 * @author teixeira
 */
public class HRigidBody extends CollisionPrimitive implements BoxCollisor,
		SphereCollisor {

	private final String id;

	private double radius = 0.0;
	private Vector3 halfSize;

	private boolean ignoreIntegration = false;

	private boolean bindContactData = false;

	private final PhysicSimulation owner;

	private boolean useWorldForces = false;

	public HRigidBody(String id, PhysicSimulation owner) {
		this.id = id;
		this.owner = owner;
	}

	public RigidBody initBody() {
		RigidBody body = ignoreIntegration ? new LazyRigidBody()
				: new RigidBody();
		setBody(body);
		return body;
	}

	@Override
	public RigidBody getBody() {
		RigidBody b = super.getBody();
		if (!ignoreIntegration) {
			return b;
		}

		try {
			return (RigidBody) b.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return b;
		}
	}

	public String getId() {
		return id;
	}

	@Override
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public Vector3 getHalfSize() {
		return halfSize;
	}

	public void setHalfSize(Vector3 halfSize) {
		this.halfSize = halfSize;
	}

	public boolean isIgnoreIntegration() {
		return ignoreIntegration;
	}

	public void setIgnoreIntegration(boolean ignoreIntegration) {
		this.ignoreIntegration = ignoreIntegration;
	}

	public boolean isBindContactData() {
		return bindContactData;
	}

	public void setBindContactData(boolean bindContactData) {
		this.bindContactData = bindContactData;
	}

	public boolean isUseWorldForces() {
		return useWorldForces;
	}

	public void setUseWorldForces(boolean useWorldForces) {
		this.useWorldForces = useWorldForces;
	}

	@Override
	public void bindContact(Contact contact) {
		if (bindContactData) {
			owner.bindContact(contact);
		}
	}

	/**
	 * Class para viabilizar o uso de lazy.
	 * 
	 * @author Teixeira
	 */
	private final class LazyRigidBody extends RigidBody {

		@Override
		public void integrate(double duration) {
			if (!HRigidBody.this.ignoreIntegration) {
				super.integrate(duration);
			}
			// nao faz nada, preguicinha memo
		}
	}

}
