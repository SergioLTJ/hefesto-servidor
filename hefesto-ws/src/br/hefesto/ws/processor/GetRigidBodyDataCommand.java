package br.hefesto.ws.processor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import br.hefesto.simulation.HRigidBody;
import br.hefesto.simulation.PhysicSimulation;
import br.law123.core.Quaternion;
import br.law123.core.Vector3;

/**
 * Processador do comando {@link CommandType#INTEGRATE}.
 * 
 * @author teixeira
 */
public class GetRigidBodyDataCommand implements CommandProcessor {

    @Override
    public JSONObject process(PhysicSimulation simulation, JSONObject data) {
        String body = data.getString("body");
        
        JSONArray datas = new JSONArray();
        List<HRigidBody> bodys = new ArrayList<HRigidBody>();
        if (body != null) {
	        if ("all".equalsIgnoreCase(body)) {
	        	bodys.addAll(simulation.getRigidBodys().values());
	        } else {
	        	bodys.add(simulation.getRigidBody(body));
	        }
	        
	        for (HRigidBody b : bodys) {
	        	JSONObject d = new JSONObject();
	        	
	        	d.put("id", b.getId());
	        	d.put("position", getJSONVector3(b.getBody().getPosition()));
	        	d.put("orientation", getJSONQuaternion(b.getBody().getOrientation()));
	        	d.put("velocity", getJSONVector3(b.getBody().getVelocity()));
	        	d.put("aceleration", getJSONVector3(b.getBody().getAcceleration()));
	        	d.put("rotation", getJSONVector3(b.getBody().getRotation()));
	        	d.put("inverseInertiaTensor", b.getBody().getInverseInertiaTensor().data);
	        	
	        	datas.put(d);
	        }
	    }        
        JSONObject _result = new JSONObject();
        _result.put("bodys", datas);

        return _result;
    }

    private JSONObject getJSONVector3(Vector3 v) {
        JSONObject data = new JSONObject();
        data.put("x", v.getX());
        data.put("y", v.getY());
        data.put("z", v.getZ());
        return data;
    }

    private JSONObject getJSONQuaternion(Quaternion q) {
        JSONObject data = new JSONObject();
        data.put("_w", q.getR());
        data.put("_x", q.getI());
        data.put("_y", q.getJ());
        data.put("_z", q.getK());

        return data;
    }

}
