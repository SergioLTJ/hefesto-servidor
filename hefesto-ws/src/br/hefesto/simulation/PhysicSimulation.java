package br.hefesto.simulation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.law123.collide.CollisionData;
import br.law123.collide.CollisionDetector;
import br.law123.collide.CollisionPlane;
import br.law123.core.Vector3;
import br.law123.force.Force;
import br.law123.rigidbody.contact.Contact;
import br.law123.rigidbody.contact.ContactResolver;

public class PhysicSimulation {

	private final long id;

	private final Map<String, HRigidBody> rigidBodys = new LinkedHashMap<String, HRigidBody>();

	private final Map<String, HForce> forces = new LinkedHashMap<String, HForce>();
	private final Map<HRigidBody, List<HForce>> forcesByBody = new HashMap<HRigidBody, List<HForce>>();

	private final Map<String, Collision> collisions = new LinkedHashMap<String, Collision>();
	private final Map<HRigidBody, List<Collision>> collisionByBody = new HashMap<HRigidBody, List<Collision>>();

	private final Map<String, CollisionData> collisionDatas = new HashMap<String, CollisionData>();

	private final int maxContact = 256;
	private final ContactResolver resolver = new ContactResolver(maxContact * 8);

	// contacts from a integration step
	private final List<Contact> contacts = new ArrayList<Contact>();

	public PhysicSimulation(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Map<String, HRigidBody> getRigidBodys() {
		return rigidBodys;
	}

	public List<Contact> getContacts() {
		List<Contact> result = new ArrayList<Contact>(contacts);
		contacts.clear();
		return result;
	}

	public boolean addRigidBody(HRigidBody body) {
		if (rigidBodys.containsKey(body.getId())) {
			return false;
		}
		rigidBodys.put(body.getId(), body);
		return true;
	}

	public HRigidBody getRigidBody(String id) {
		return rigidBodys.get(id);
	}

	public boolean removeRigidBody(String id) {
		HRigidBody body = rigidBodys.remove(id);

		if (body != null) {
			List<Collision> list = collisionByBody.get(body);

			if (list != null) {
				for (Collision c : list) {
					collisions.remove(c.getId());
				}
				collisionByBody.remove(body);
			}
			return true;
		}
		return false;
	}

	public boolean addForce(HForce force) {
		if (forces.containsKey(force.getId())) {
			return false;
		}
		forces.put(force.getId(), force);
		return true;
	}

	public HForce getForce(String id) {
		return forces.get(id);
	}

	public boolean addForceToBody(HForce force, HRigidBody body) {
		if (!forces.containsKey(force.getId())) {
			return false;
		}
		if (!rigidBodys.containsKey(body.getId())) {
			return false;
		}
		List<HForce> list = forcesByBody.get(body);
		if (list == null) {
			list = new ArrayList<>();
			forcesByBody.put(body, list);
		}
		list.add(force);
		return true;
	}

	public boolean removeForce(String id) {
		Force force = forces.remove(id);

		if (force != null) {
			for (List<HForce> list : forcesByBody.values()) {
				if (list != null) {
					list.remove(force);
				}
			}
			return true;
		}
		return false;
	}

	public boolean addCollision(Collision collision) {
		if (collisions.containsKey(collision.getId())) {
			return false;
		}
		collisions.put(collision.getId(), collision);

		setBodyCollision(collision, collision.getRb1());
		if (collision.getRb2() != null) {
			setBodyCollision(collision, collision.getRb2());
		}
		return true;
	}

	private void setBodyCollision(Collision collision, HRigidBody rb) {
		List<Collision> list = collisionByBody.get(rb);
		if (list == null) {
			list = new ArrayList<Collision>();
			collisionByBody.put(collision.getRb1(), list);
		}
		list.add(collision);
	}

	public Collision getCollision(String id) {
		return collisions.get(id);
	}

	public boolean removeCollision(String id) {
		return collisions.remove(id) != null;
	}

	public boolean addCollisionData(CollisionData collisionData) {
		if (collisionDatas.containsKey(collisionData.getId())) {
			return false;
		}
		collisionDatas.put(collisionData.getId(), collisionData);
		return true;
	}

	public CollisionData getCollisionData(String id) {
		return collisionDatas.get(id);
	}
	
	private List<Long> forcas = new ArrayList<Long>();
	private List<Long> integracoes = new ArrayList<Long>();
	private List<Long> deteccoes = new ArrayList<Long>();
	private List<Long> resolucoes = new ArrayList<Long>();
	
	private int qtdIntegracoes = 0;

	public void integrate(double duration) {

		// Update the objects
		integrateObjects(duration);

		// Perform the contact generation
		generateContacts(duration);
		if (++qtdIntegracoes > 20) {
			printValores();
			qtdIntegracoes = 0;
			forcas.clear();
			integracoes.clear();
			deteccoes.clear();
			resolucoes.clear();
		}
	}

	private void printValores() {
		BigDecimal mediaForcas = getMedia(forcas);
		BigDecimal mediaIntegracoes = getMedia(integracoes);
		BigDecimal mediaDeteccoes = getMedia(deteccoes);
		BigDecimal mediaResolucoes = getMedia(resolucoes);
		/*System.out.println("=================================================");
		System.out.println("Forcas      :" + mediaForcas);
		System.out.println("Integracoes :" + mediaIntegracoes);
		System.out.println("Deteccoes   :" + mediaDeteccoes);
		System.out.println("Resolucoes  :" + mediaResolucoes);
		System.out.println("TOTAL       :" + mediaForcas.add(mediaIntegracoes).add(mediaDeteccoes).add(mediaResolucoes));
		System.out.println("=================================================");*/
	}

	private BigDecimal getMedia(List<Long> list) {
		BigDecimal media = BigDecimal.ZERO;
		for (Long l : list) {
			media = media.add(BigDecimal.valueOf(l));
		}
		try {
			return media.divide(BigDecimal.valueOf(list.size()), 5, RoundingMode.CEILING);
		} catch (ArithmeticException e) {
			return BigDecimal.ZERO;
		}
	}

	protected void integrateObjects(double duration) {
		for (HRigidBody rb : rigidBodys.values()) {

			long init = System.nanoTime();
			Collection<HForce> forces = forcesByBody.get(rb);
			if (rb.isUseWorldForces()) {
				forces = this.forces.values();
			}
			if (forces != null) {
				for (Force f : forces) {
					f.updateForce(rb.getBody(), duration);
				}
			}
			forcas.add(System.nanoTime() - init);
			
			init = System.nanoTime();
			rb.getBody().integrate(duration);
			rb.calculateInternals();
			integracoes.add(System.nanoTime() - init);
		}
	}

	protected void generateContacts(double duration) {
		// Create the ground plane data
		CollisionPlane plane = new CollisionPlane();
		plane.setDirection(new Vector3(0, 1, 0));
		plane.setOffset(0);

		long init = System.nanoTime();
		for (Collision col : collisions.values()) {
			if (!col.getData().hasMoreContacts() || !col.isEnable()) {
				continue;
			}
			switch (col.getType()) {
			case BOX_AND_BOX:
				CollisionDetector.boxAndBox(col.getRb1(), col.getRb2(),
						col.getData());
				break;
			case BOX_AND_HALFSPACE:
				CollisionDetector.boxAndHalfSpace(col.getRb1(), plane,
						col.getData());
				break;
			case BOX_AND_POINT:
				// CollisionDetector.boxAndPoint(col.getRb1(), col.getRb2(),
				// col.getData());
				break;
			case BOX_AND_SPHERE:
				CollisionDetector.boxAndSphere(col.getRb1(), col.getRb2(),
						col.getData());
				break;
			case SPHERE_AND_HALFSPACE:
				CollisionDetector.sphereAndHalfSpace(col.getRb1(), plane,
						col.getData());
				break;
			case SPHERE_AND_SPHERE:
				CollisionDetector.sphereAndSphere(col.getRb1(), col.getRb2(),
						col.getData());
				break;
			case SPHERE_AND_TRUEPLANE:
				CollisionDetector.sphereAndTruePlane(col.getRb1(), plane,
						col.getData());
				break;
			default:
				System.err.println("Unkown collision type: " + col.getType());
			}
		}
		deteccoes.add(System.nanoTime() - init);

		init = System.nanoTime();
		for (CollisionData cd : collisionDatas.values()) {
			resolver.resolveContacts(cd.collectContacts(), duration);
			cd.reset(maxContact);
		}
		resolucoes.add(System.nanoTime() - init);
	}

	public boolean bindContact(Contact contact) {
		return contacts.add(contact);
	}

}
