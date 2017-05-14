package br.hefesto.ws.processor;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import br.hefesto.simulation.HRigidBody;
import br.hefesto.simulation.PhysicSimulation;
import br.law123.core.Quaternion;
import br.law123.core.Vector3;
import br.law123.rigidbody.RigidBody;
import br.law123.rigidbody.contact.Contact;

/**
 * Processador do comando {@link CommandType#INTEGRATE}.
 * 
 * @author teixeira
 */
public class IntegrateCommand implements CommandProcessor {

    @Override
    public JSONObject process(PhysicSimulation simulation, JSONObject data) {
        double duration = data.getDouble("duration");
        simulation.integrate(duration);

        Map<String, HRigidBody> bodys = simulation.getRigidBodys();

        JSONArray array = new JSONArray();

        for (Entry<String, HRigidBody> e : bodys.entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("id", e.getKey());

            RigidBody body = e.getValue().getBody();
			obj.put("position", getJSONVector3(body.getPosition()));

            //obj.put("_orientation", getJSONQuaternion(e.getValue().getBody().getOrientation()));

            float[] mat = new float[16];
            body.getGLTransform(mat);
            obj.put("transform", mat);
            
            array.put(obj);
        }

        List<Contact> contacts = simulation.getContacts();
        
        JSONArray conts = new JSONArray();
        
        for (Contact c : contacts) {
        	JSONObject cont = new JSONObject();
        	
        	cont.put("friction", c.getFriction());
        	cont.put("restitution", c.getRestitution());
        	cont.put("penetration", c.getPenetration());
        	cont.put("contactNormal", getJSONVector3(c.getContactNormal()));
        	cont.put("contactPoint", getJSONVector3(c.getContactPoint()));
        	cont.put("contactToWorld", c.getContactToWorld().data);
        	
        	cont.put("body1", c.getBody()[0]);
        	cont.put("body2", c.getBody()[1]);
        	
        	conts.put(cont);
        }
        
        JSONObject _result = new JSONObject();
        _result.put("_rigidBodys", array);
        _result.put("_contacts", conts);

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
