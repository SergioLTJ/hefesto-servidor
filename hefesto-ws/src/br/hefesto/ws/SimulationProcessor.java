package br.hefesto.ws;

import javax.websocket.Session;

import org.json.JSONObject;

import br.hefesto.simulation.PhysicSimulation;
import br.hefesto.ws.processor.CommandProcessor;
import br.hefesto.ws.processor.CommandType;

public class SimulationProcessor {

    private final Session session;
    private final PhysicSimulation simulation;

    public SimulationProcessor(Session session, PhysicSimulation simulation) {
        this.session = session;
        this.simulation = simulation;
    }

    public PhysicSimulation getSimulation() {
        return simulation;
    }

    public void processMessage(String message) {
        JSONObject object = new JSONObject(message);

        Integer _id = object.getInt("id");

        String strType = object.getString("type");
        CommandType cmd = CommandType.valueOf(strType);

        try {
        	CommandProcessor processor = cmd.getProcessor().newInstance();
        	JSONObject data = processor.process(simulation, object.getJSONObject("data"));
            //if (cmd == CommandType.INTEGRATE || cmd == CommandType.NEW_SIMULATION) {
            	sendAckMessage(_id, cmd, data);
            //}
        } catch (Exception e) {
            e.printStackTrace();
            //pense nisso e tenha um bom dia
        }
    }

    void sendAckMessage(Integer messageId, CommandType cmd, JSONObject data) {

        JSONObject master = new JSONObject();
        master.put("id", messageId);
        master.put("type", cmd.getCommand());
        master.put("data", data);

        // Adiciona essa nova conexão a lista de conexões
        sendMessage(master);
    }

	private void sendMessage(JSONObject master) {
		synchronized (this) {
			//System.out.println("Message sended: " + master);
			try {
				session.getAsyncRemote().sendText(master.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
