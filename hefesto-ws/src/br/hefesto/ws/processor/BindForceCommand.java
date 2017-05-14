package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.hefesto.simulation.ForceType;
import br.hefesto.simulation.HForce;
import br.hefesto.simulation.PhysicSimulation;
import br.law123.core.Vector3;
import br.law123.force.Gravity;

public class BindForceCommand extends AbstractCommandProcessor {

	@Override
	public JSONObject process(PhysicSimulation simulation, JSONObject data) {

		String id = data.getString("id");
		ForceType type = ForceType.valueOf(data.getString("type"));

		JSONObject result = new JSONObject();

		boolean added = false;
		if (type == ForceType.GRAVITY) {
			Vector3 gravity = getVector3(data.getJSONObject("gravity"));
			Gravity g = new Gravity(gravity);
			HForce f = new HForce(id, g);

			added = simulation.addForce(f);
			result.put("added", added);
		}
		return result;
	}

}
