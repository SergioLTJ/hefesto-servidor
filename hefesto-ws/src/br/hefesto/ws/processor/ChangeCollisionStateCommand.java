package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.hefesto.simulation.Collision;
import br.hefesto.simulation.PhysicSimulation;

public class ChangeCollisionStateCommand implements CommandProcessor {

    @Override
    public JSONObject process(PhysicSimulation simulation, JSONObject data) {
    	
    	String id = data.getString("id");
    	Boolean state = data.getBoolean("state");
    	System.out.println("State:" + state);
    	
    	JSONObject result = new JSONObject();
    	result.put("id", id);
    	
        Collision collision = simulation.getCollision(id);
        if (collision != null) {
        	System.out.println("CHANGE ColllisionId:" + collision.getId());
        	collision.setEnable(state);
        	result.put("enabled", collision.isEnable());
        } else {
        	result.put("enabled", "Collision not found.");
        }

        return result;
    }

}
