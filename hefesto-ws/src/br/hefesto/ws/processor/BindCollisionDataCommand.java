package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.hefesto.simulation.PhysicSimulation;
import br.law123.collide.CollisionData;

public class BindCollisionDataCommand implements CommandProcessor {

    @Override
    public JSONObject process(PhysicSimulation simulation, JSONObject data) {
    	
    	String id = data.getString("id");
    	Integer maxContacts = data.getInt("maxContacts");
    	Double friction = data.getDouble("friction");
    	Double restitution = data.getDouble("restitution");
    	Double tolerance = data.getDouble("tolerance");
    	
    	CollisionData cd = new CollisionData(id, friction, restitution, tolerance, maxContacts);
    	
        boolean added = simulation.addCollisionData(cd);

        JSONObject result = new JSONObject();
        result.put("added", added);
        result.put("id", cd.getId());

        return result;
    }

}
