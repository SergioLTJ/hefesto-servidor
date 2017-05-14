package br.hefesto.ws.conn;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import br.hefesto.ws.PhysicSimulationOrchestrator;
import br.hefesto.ws.SimulationProcessor;

/**
 * Representa a conexão WebSocket da simulação 3D.
 * 
 * @author teixeira
 */
@ServerEndpoint(value = "/physics3DSimulationWS")
public final class PhysicSimulationEndPoint {

    @OnOpen
    public void onOpen(Session session) {
        try {
            SimulationProcessor simulation = PhysicSimulationOrchestrator.bindConnection(session);
            //System.out.println("Begin simulation: " + simulation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        SimulationProcessor simulation = PhysicSimulationOrchestrator.kill(session);
        //System.out.println("End simulation: " + simulation);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
    	synchronized (this) {
    		//System.out.println("Start process message: " + session);
    		try {
    			Thread.sleep(5);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		PhysicSimulationOrchestrator.processMessage(session, message);
    		//System.out.println("Message processed: " + message);
		}
    }

}
