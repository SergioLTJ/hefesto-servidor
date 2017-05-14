package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.hefesto.simulation.HRigidBody;
import br.hefesto.simulation.PhysicSimulation;
import br.law123.core.Vector3;

public class AddForceToRigidBodyCommand extends AbstractCommandProcessor {

	@Override
	public JSONObject process(PhysicSimulation simulation, JSONObject data) {

		String body = data.getString("body");
		Vector3 force = getVector3(data.getJSONObject("force"));

		HRigidBody b = simulation.getRigidBody(body);
		b.getBody().addForce(force);

		JSONObject result = new JSONObject();
		result.put("added", true);

		return result;
	}

}
