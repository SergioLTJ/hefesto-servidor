package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.hefesto.simulation.PhysicSimulation;

public class RemoveCollisionCommand implements CommandProcessor {

    @Override
    public JSONObject process(PhysicSimulation simulation, JSONObject data) {
		String id = data.getString("id");

		boolean removed = simulation.removeCollision(id);

		JSONObject result = new JSONObject();
		result.put("removed", removed);

		return result;
	}

}
