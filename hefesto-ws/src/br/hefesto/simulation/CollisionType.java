package br.hefesto.simulation;

/**
 * Tipos de colisão suportados.
 * 
 * @author teixeira
 */
public enum CollisionType {

	/** */
	SPHERE_AND_HALFSPACE("SPHERE_AND_HALFSPACE"),
	/** */
	SPHERE_AND_TRUEPLANE("SPHERE_AND_TRUEPLANE"),
	/** */
	SPHERE_AND_SPHERE("SPHERE_AND_SPHERE"),
	/** */
	BOX_AND_HALFSPACE("BOX_AND_HALFSPACE"),
	/** */
	BOX_AND_BOX("BOX_AND_BOX"),
	/** */
	BOX_AND_POINT("BOX_AND_POINT"),
	/** */
	BOX_AND_SPHERE("BOX_AND_SPHERE"),
	/** */
	ALL("ALL");

	private String type;

	private CollisionType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
