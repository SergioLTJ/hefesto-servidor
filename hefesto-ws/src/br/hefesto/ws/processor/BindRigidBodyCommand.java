package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.hefesto.simulation.HRigidBody;
import br.hefesto.simulation.PhysicSimulation;
import br.law123.core.Matrix3;
import br.law123.core.Quaternion;
import br.law123.core.Vector3;
import br.law123.rigidbody.RigidBody;

public class BindRigidBodyCommand extends AbstractCommandProcessor {

    @Override
    public JSONObject process(PhysicSimulation simulation, JSONObject data) {
    	try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	String id = data.getString("id");
		HRigidBody hbody = new HRigidBody(id, simulation);
		
		hbody.setIgnoreIntegration(data.getBoolean("ignoreIntegration"));
		hbody.setBindContactData(data.getBoolean("bindContactData"));
		RigidBody body = hbody.initBody();

        body.setPosition(getVector3(data.getJSONObject("position")));
        body.setOrientation(getQuaternion(data.getJSONObject("orientation")));
        body.setVelocity(getVector3(data.getJSONObject("velocity")));
        body.setAcceleration(getVector3(data.getJSONObject("acceleration")));
        body.setRotation(getVector3(data.getJSONObject("rotation")));

        body.setMass(data.getDouble("mass"));
        body.setInertiaTensor(getMatrix3(data.getJSONObject("inertiaTensor").getJSONObject("elements")));
        body.setLinearDamping(data.getDouble("linearDamping"));
        body.setAngularDamping(data.getDouble("angularDamping"));

        body.setCanSleep(data.getBoolean("canSleep"));
        hbody.setUseWorldForces(data.getBoolean("useWorldForces"));
        
        if (data.has("halfSize")) {
        	hbody.setHalfSize(getVector3(data.getJSONObject("halfSize")));
        }
        if (data.has("radius")) {
        	hbody.setRadius(data.getDouble("radius"));
        }
        
        
        body.setAwake();
        body.calculateDerivedData();
        hbody.calculateInternals();

        boolean added = simulation.addRigidBody(hbody);

        JSONObject result = new JSONObject();
        result.put("added", added);
        result.put("id", hbody.getId());

        return result;
    }

}
