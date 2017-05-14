package br.hefesto.ws.processor;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import br.hefesto.simulation.Collision;
import br.hefesto.simulation.CollisionType;
import br.hefesto.simulation.HRigidBody;
import br.hefesto.simulation.PhysicSimulation;
import br.law123.collide.CollisionData;

public class BindCollisionCommand implements CommandProcessor {

    @Override
    public JSONObject process(PhysicSimulation simulation, JSONObject data) {
    	
    	System.out.println("BindCollisionCommand");
    	
    	String id = data.getString("id");
    	CollisionType type = CollisionType.valueOf(data.getString("type"));
    	Boolean enable = data.getBoolean("enable");
    	
    	String dataId = data.getString("data");
    	CollisionData cData = simulation.getCollisionData(dataId);
    	
    	String body1Id = data.getString("body1");
    	HRigidBody rb1 = simulation.getRigidBody(body1Id);
    	
    	JSONObject result = new JSONObject();
    	
    	boolean added = false;
    	if (type == CollisionType.ALL) {
    		Collection<HRigidBody> bodys = simulation.getRigidBodys().values();
    		
    		int idAux = 0;
    		JSONArray ids = new JSONArray();
    		for (HRigidBody b : bodys) {
    			if (rb1 == b) {
    				continue;
    			}
    			
    			String idCollision = id + "_" + idAux++;
				Collision collision = new Collision(idCollision, null, cData, b, rb1);
				defineType(collision);
        		collision.setEnable(enable);
        		
        		added = simulation.addCollision(collision);
        		
        		JSONObject colId = new JSONObject();
        		colId.put("id", idCollision);
        		colId.put("rbId", b.getId());
        		ids.put(colId);
			}
    		result.put("id", ids);
    	} else {
    		HRigidBody rb2 = null;
    		if (!data.isNull("body2")) {
    			rb2 = simulation.getRigidBody(data.getString("body2"));
    		}
    		Collision collision = new Collision(id, type, cData, rb1, rb2);
    		collision.setEnable(enable);
    		
    		added = simulation.addCollision(collision);
    		result.put("id", collision.getId());
    	}
    	

        result.put("added", added);

        return result;
    }

	private void defineType(Collision collision) {
		HRigidBody rb1 = collision.getRb1();
		HRigidBody rb2 = collision.getRb2();
		
		if (rb1.getRadius() > 0.0 && rb2.getRadius() > 0.0) {
			collision.setType(CollisionType.SPHERE_AND_SPHERE);
		} else if (rb1.getRadius() == 0.0 && rb2.getRadius() == 0.0) {
			collision.setType(CollisionType.BOX_AND_BOX);
		} else if (rb1.getRadius() == 0.0 && rb2.getRadius() > 0.0) {
			collision.setType(CollisionType.BOX_AND_SPHERE);
		} else if (rb1.getRadius() > 0.0 && rb2.getRadius() == 0.0) {
			collision.setRb1(rb2);
			collision.setRb2(rb1);
			
			collision.setType(CollisionType.BOX_AND_SPHERE);
		} else {
			throw new IllegalArgumentException("Can't define collision type.");
		}
	}

}
