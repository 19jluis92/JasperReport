//Constantes para operacion con atributos de objeto.
	public static final int RESTRICTION_OPERATION_EQUALS 				= 1;
	public static final int RESTRICTION_OPERATION_ILIKE 				= 2;
	public static final int RESTRICTION_OPERATION_EQUALS_OR_NULL 		= 3;
	public static final int RESTRICTION_OPERATION_GREATER_OR_EQUALS 	= 4;
	public static final int RESTRICTION_OPERATION_LESS_OR_EQUALS 		= 5;
	public static final int RESTRICTION_OPERATION_GREATER_THAN 			= 6;
	public static final int RESTRICTION_OPERATION_LESS_THAN 			= 7;
	public static final int RESTRICTION_OPERATION_SEARCH 				= 8;
	
	//Constantes para operaciones con el atributo de objeto.
	public static final int ATTRIBUTE_TYPE_STRING 					= 1;
	public static final int ATTRIBUTE_TYPE_LONG 					= 2;
	public static final int ATTRIBUTE_TYPE_BOOLEAN 					= 3;
	public static final int ATTRIBUTE_TYPE_DOUBLE 					= 4;
	public static final int ATTRIBUTE_TYPE_DATE 					= 5;
	public static final int ATTRIBUTE_TYPE_UNKNOWN 					= 6;
	public static final int ATTRIBUTE_TYPE_INTEGER 					= 7;
	
	private String objectAttributeName;
	private Integer objectAttributeType;
	private Integer restrictionOperation;	
	
	/**
	 * 
	 * @param objectAttributeName Nombre del atributo de objeto
	 * @param objectAttributeType Valor del tipo de dato del atributo de objeto
	 * @param restrictionOperation Valor de la operacion a realizar con el atributo
	 */
	public SearchProperties(String objectAttributeName, Integer objectAttributeType, Integer restrictionOperation) {
		super();
		this.objectAttributeName = objectAttributeName;
		this.objectAttributeType = objectAttributeType;
		this.restrictionOperation = restrictionOperation;
	}

	/**
	 * @return the objectAttributeName
	 */
	public String getObjectAttributeName() {
		return objectAttributeName;
	}

	/**
	 * @param objectAttributeName the objectAttributeName to set
	 */
	public void setObjectAttributeName(String objectAttributeName) {
		this.objectAttributeName = objectAttributeName;
	}

	/**
	 * @return the objectAttributeType
	 */
	public Integer getObjectAttributeType() {
		return objectAttributeType;
	}

	/**
	 * @param objectAttributeType the objectAttributeType to set
	 */
	public void setObjectAttributeType(Integer objectAttributeType) {
		this.objectAttributeType = objectAttributeType;
	}

	/**
	 * @return the restrictionOperation
	 */
	public Integer getRestrictionOperation() {
		return restrictionOperation;
	}

	/**
	 * @param restrictionOperation the restrictionOperation to set
	 */
	public void setRestrictionOperation(Integer restrictionOperation) {
		this.restrictionOperation = restrictionOperation;
	}
	
