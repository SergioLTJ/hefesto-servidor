package br.hefesto.ws.processor;

public enum CommandType {

	/** Gera um novo corpo rigido */
	NEW_SIMULATION("NEW_SIMULATION", NewSimulationCommand.class),
	
	/** Gera um novo corpo rigido */
	BIND_RIGID_BODY("BIND_RIGID_BODY", BindRigidBodyCommand.class),
	/** Remove um novo corpo rigido */
	REMOVE_RIGID_BODY("REMOVE_RIGID_BODY", RemoveRigidBodyCommand.class),
	/** Adiciona força para em um corpo rigido */
	ADD_FORCE_TO_RIGID_BODY("ADD_FORCE_TO_RIGID_BODY", AddForceToRigidBodyCommand.class),
	
	/** Gera uma nova particula */
	BIND_PARTICLE("BIND_PARTICLE", BindParticleCommand.class),
	
	/** Gera uma nova força */
	BIND_FORCE("BIND_FORCE", BindForceCommand.class),
	/** Adiciona uma força a um corpo */
	ADD_FORCE_TO_BODY("ADD_FORCE_TO_BODY", AddForceToBodyCommand.class),
	/** REmove uma forca */
	REMOVE_FORCE("REMOVE_FORCE", RemoveForceCommand.class),
	
	/** Gera uma nova collisao */
	BIND_COLLISION("BIND_COLLISION", BindCollisionCommand.class),
	/** REmove uma nova collisao */
	REMOVE_COLLISION("REMOVE_COLLISION", RemoveCollisionCommand.class),
	/** Gera novos dados de collisao */
	BIND_COLLISION_DATA("BIND_COLLISION_DATA", BindCollisionDataCommand.class),
	
	/** Altera o status da collisao */
	CHANGE_COLLISION_STATE("CHANGE_COLLISION_STATE", ChangeCollisionStateCommand.class),
	
	/** Integrate */
	INTEGRATE("INTEGRATE", IntegrateCommand.class),
	/** Obtem as informacoes de um corpo rigido */
	GET_RIGID_BODY_DATA("GET_RIGID_BODY_DATA", GetRigidBodyDataCommand.class);

	private String command;
	private Class<? extends CommandProcessor> processor;

	private CommandType(String command, Class<? extends CommandProcessor> processor) {
		this.command = command;
		this.processor = processor;
	}

	public String getCommand() {
		return command;
	}

	public Class<? extends CommandProcessor> getProcessor() {
		return processor;
	}

	public CommandType valueOfString(String cmd) {
		for (CommandType c : values()) {
			if (c.getCommand().equals(cmd)) {
				return c;
			}
		}
		return null;
	}

}
