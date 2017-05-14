package br.hefesto.simulation;

/**
 * Tipos de for�a suportados.
 * 
 * @author teixeira
 */
public enum ForceType {

	/** */
	GRAVITY("GRAVITY");

	private String type;

	private ForceType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
