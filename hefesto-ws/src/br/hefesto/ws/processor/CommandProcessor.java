package br.hefesto.ws.processor;

import org.json.JSONObject;

import br.hefesto.simulation.PhysicSimulation;

/**
 * Represeta��o de uma processador de comando.
 * 
 * @author teixeira
 */
public interface CommandProcessor {

    /**
     * Processa um comando.
     * 
     * @param simulation simula��o para a qual o comando � destinado.
     * @param data dados.
     */
    JSONObject process(PhysicSimulation simulation, JSONObject data);

}
