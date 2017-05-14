package br.hefesto.ws;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import org.json.JSONObject;

import br.hefesto.simulation.PhysicSimulation;
import br.hefesto.ws.processor.CommandType;

/**
 * Orquestrador para as conexões de simulações 3D.
 * 
 * @author teixeira
 */
public class PhysicSimulationOrchestrator {

    private static final Map<Session, SimulationProcessor> sessions = new HashMap<Session, SimulationProcessor>();

    public static SimulationProcessor bindConnection(Session session) throws IllegalArgumentException {
        SimulationProcessor processor = new SimulationProcessor(session, new PhysicSimulation(System.currentTimeMillis()));

        sessions.put(session, processor);

        JSONObject data = new JSONObject();
        data.put("id", processor.getSimulation().getId());

        processor.sendAckMessage(-1, CommandType.NEW_SIMULATION, data);
        return processor;
    }

    public static SimulationProcessor kill(Session session) {
        return sessions.remove(session);
    }

    public static void processMessage(Session session, String message) {
        sessions.get(session).processMessage(message);
    }

}
