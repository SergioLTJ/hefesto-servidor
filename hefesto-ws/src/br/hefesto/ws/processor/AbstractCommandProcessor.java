package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.law123.core.Matrix3;
import br.law123.core.Quaternion;
import br.law123.core.Vector3;

abstract class AbstractCommandProcessor implements CommandProcessor{
	

    protected Vector3 getVector3(JSONObject obj) {
        return new Vector3(obj.getDouble("x"), obj.getDouble("y"), obj.getDouble("z"));
    }

    protected Quaternion getQuaternion(JSONObject obj) {
        return new Quaternion(obj.getDouble("_w"), obj.getDouble("_x"), obj.getDouble("_y"), obj.getDouble("_z"));
    }

    protected Matrix3 getMatrix3(JSONObject obj) {
        return new Matrix3(
        //
        obj.getDouble("0"), obj.getDouble("1"), obj.getDouble("2"), //
        obj.getDouble("3"), obj.getDouble("4"), obj.getDouble("5"), //
        obj.getDouble("6"), obj.getDouble("7"), obj.getDouble("8"));
    }

}
