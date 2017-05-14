package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.hefesto.simulation.PhysicSimulation;

public class RemoveForceCommand extends AbstractCommandProcessor {

	@Override
	public JSONObject process(PhysicSimulation simulation, JSONObject data) {

		String force = data.getString("id");

		JSONObject result = new JSONObject();

		boolean removed = simulation.removeForce(force);
		result.put("removed", removed);

		return result;
	}

}
