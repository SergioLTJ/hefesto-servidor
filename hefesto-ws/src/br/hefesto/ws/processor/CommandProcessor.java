package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.hefesto.simulation.PhysicSimulation;

/**
 * Represetação de uma processador de comando.
 * 
 * @author teixeira
 */
public interface CommandProcessor {

    /**
     * Processa um comando.
     * 
     * @param simulation simulação para a qual o comando é destinado.
     * @param data dados.
     */
    JSONObject process(PhysicSimulation simulation, JSONObject data);

}
