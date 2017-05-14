package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.hefesto.simulation.PhysicSimulation;

public class RemoveRigidBodyCommand implements CommandProcessor {

	@Override
	public JSONObject process(PhysicSimulation simulation, JSONObject data) {
		String id = data.getString("id");

		boolean removed = simulation.removeRigidBody(id);

		JSONObject result = new JSONObject();
		result.put("id", id);
		result.put("removed", removed);
		
		if (removed) {
			System.err.println("Removeu: " + id);
		} else {
			System.err.println("Não removeu: " + id);
		}

		return result;
	}

}
