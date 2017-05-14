package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.hefesto.simulation.HForce;
import br.hefesto.simulation.HRigidBody;
import br.hefesto.simulation.PhysicSimulation;

public class AddForceToBodyCommand implements CommandProcessor {

	@Override
	public JSONObject process(PhysicSimulation simulation, JSONObject data) {

		String body = data.getString("body");
		String force = data.getString("force");

		HRigidBody b = simulation.getRigidBody(body);
		HForce f = simulation.getForce(force);

		JSONObject result = new JSONObject();

		boolean added = simulation.addForceToBody(f, b);
		result.put("added", added);

		return result;
	}

}
