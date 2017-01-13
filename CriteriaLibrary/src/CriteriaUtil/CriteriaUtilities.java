/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CriteriaUtil;

/**
 *
 * @author jluis
 */
public class CriteriaUtilities {
    
    /**
	 * @author Arturo Sánchez
	 * 
	 * Método que agrega Restriccions a un Criteria, esto con base a las propiedades de un 
	 * parametro y los valores asignados.
	 * 
	 * @param criteria Criteria para asignar Restrictions
	 * @param properties Propiedades para interpretacion de parametro a Restriction
	 * @param values Valores para implementacion de Restriction sobre las propiedades de un parametro.
	 * @return Criteria con el resultado de la interpretacion de propiedades-valor.
	 */
	public static Criteria getRectrictionsCriteria( Criteria criteria, SearchProperties properties, List<String> values ){
		
		String attributeName 	= properties.getObjectAttributeName( );
		Integer attributeType 	= properties.getObjectAttributeType( );
		Integer paramOperator 	= properties.getRestrictionOperation( );
		
		if( paramOperator == SearchProperties.RESTRICTION_OPERATION_SEARCH ){
			String value = values.get( 0 );
			if( !value.isEmpty( ) ){
				getRectrictionsCriteriaSearch( criteria, attributeName, value );
			}
		}else{		
			Disjunction disjunction = Restrictions.disjunction( );
			for (String value : values) {
				if( !value.isEmpty() ){
					Object parsedValue = null;
					
					switch( attributeType ){
						case SearchProperties.ATTRIBUTE_TYPE_BOOLEAN:
							if( Convertidor.controlParseBoolean( value ) ){
								parsedValue = Boolean.parseBoolean( value );
							}
						break;
						case SearchProperties.ATTRIBUTE_TYPE_INTEGER:
							if( Convertidor.controlParseInteger( value ) ){
								parsedValue = Integer.parseInt( value );
							}
						break;
						case SearchProperties.ATTRIBUTE_TYPE_LONG:
							if( Convertidor.controlParseLong( value ) ){
								parsedValue = Long.parseLong( value );
							}
						break;
						case SearchProperties.ATTRIBUTE_TYPE_STRING:
							parsedValue = value;
						break;
						case SearchProperties.ATTRIBUTE_TYPE_DATE:
							parsedValue = Convertidor.convierteFecha( value );
						break;
					}
					
					if( parsedValue != null ){
						switch( paramOperator ){
							case SearchProperties.RESTRICTION_OPERATION_EQUALS:
							case SearchProperties.RESTRICTION_OPERATION_EQUALS_OR_NULL:
								if( attributeType.equals( SearchProperties.ATTRIBUTE_TYPE_DATE ) ){
									Calendar  calendar = Calendar.getInstance();
									calendar.setTime( (Date) parsedValue );
									calendar.add( Calendar.HOUR, +23 );
									calendar.add( Calendar.MINUTE, +59 );
									calendar.add( Calendar.SECOND, +59 );
									disjunction.add( Restrictions.between( attributeName, parsedValue, calendar.getTime( ) ) );
								}else{
									disjunction.add( Restrictions.eq( attributeName, parsedValue ) );
								}
							break;
							case SearchProperties.RESTRICTION_OPERATION_ILIKE:
								disjunction.add( Restrictions.ilike( attributeName, parsedValue+"", MatchMode.ANYWHERE ) );
							break;
							case SearchProperties.RESTRICTION_OPERATION_GREATER_THAN:
								disjunction.add( Restrictions.gt( attributeName, parsedValue ) );
							case SearchProperties.RESTRICTION_OPERATION_GREATER_OR_EQUALS:
								disjunction.add( Restrictions.ge( attributeName, parsedValue ) );
							case SearchProperties.RESTRICTION_OPERATION_LESS_THAN:
								disjunction.add( Restrictions.lt( attributeName, parsedValue ) );
							case SearchProperties.RESTRICTION_OPERATION_LESS_OR_EQUALS:
								if( attributeType.equals( SearchProperties.ATTRIBUTE_TYPE_DATE ) ){
									Calendar  calendar = Calendar.getInstance();
									calendar.setTime( (Date) parsedValue );
									calendar.add( Calendar.HOUR, +23 );
									calendar.add( Calendar.MINUTE, +59 );
									calendar.add( Calendar.SECOND, +59 );
									parsedValue = (Date) calendar.getTime( );{}
								}
								disjunction.add( Restrictions.le( attributeName, parsedValue ) );
							default: 
							break;
						}
					}else if( paramOperator.equals( SearchProperties.RESTRICTION_OPERATION_EQUALS_OR_NULL ) ){
						disjunction.add( Restrictions.isNull( attributeName ) );
					}
				}
			}
			
			criteria.add( disjunction );
		}
		
		return criteria;
	}
	
    
    
    	private static Criteria getRectrictionsCriteriaSearch( Criteria criteria, String attributeName, String value ){
		
		CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;
		String className = criteriaImpl.getEntityOrClassName( );
		
		HashMap<String,String>  aliasMap = new HashMap<String, String>( );
		
		
		Iterator<Subcriteria> listSub = criteriaImpl.iterateSubcriteria();
			while(listSub.hasNext())
			{
				Subcriteria data = listSub.next();
			
					aliasMap.put(data.getPath(),data.getAlias() );
					
				
				
			}
		attributeName = attributeName.trim( );
		String[] fields = attributeName.split( "/" );
		
		if( fields != null ){
			
			Disjunction disjunction = Restrictions.disjunction( );
			
			for ( String field : fields ) {
				if( !field.isEmpty( ) ){
					int fieldType = getFieldType( className, field );
					
					if( field.contains( "." ) )
					{
 						String[] aliasAux = field.split( "\\." );
 						
 						if( !aliasMap.containsKey( aliasAux[ 0 ] ) ){
 							criteria.createAlias( aliasAux[ 0 ], aliasAux[ 0 ] );
 							aliasMap.put( aliasAux[ 0 ], aliasAux[ 0 ] );
 						}
 						for( int i = 0; i < aliasAux.length - 2 ; i++ )
 						{
 							if( !aliasMap.containsKey( aliasAux[ i ] + "." + aliasAux[ i+1  ] ) ){
 								criteria.createAlias( aliasAux[ i ] + "." + aliasAux[ i+1  ], aliasAux[ i+1  ] );
 								aliasMap.put( aliasAux[ i ] + "." + aliasAux[ i +1], aliasAux[ i+1  ] );
 							}
 						}
 						field = aliasAux[ aliasAux.length - 2 ] + "." + aliasAux[ aliasAux.length - 1 ];
					}
					
					Object parsedValue = null;
					
					switch( fieldType ){
						case SearchProperties.ATTRIBUTE_TYPE_BOOLEAN:
							if( Convertidor.controlParseBoolean( value ) ){
								parsedValue = Boolean.parseBoolean( value );
							}
						break;
						case SearchProperties.ATTRIBUTE_TYPE_INTEGER:
							if( Convertidor.controlParseInteger( value ) ){
								parsedValue = Integer.parseInt( value );
							}
						break;
						case SearchProperties.ATTRIBUTE_TYPE_LONG:
							if( Convertidor.controlParseLong( value ) ){
								parsedValue = Long.parseLong( value );
							}
						break;
						case SearchProperties.ATTRIBUTE_TYPE_STRING:
							parsedValue = value;
						break;
						case SearchProperties.ATTRIBUTE_TYPE_DATE:
							parsedValue = Convertidor.convierteFecha( value );
						break;
					}
					
					if( parsedValue != null ){	
						
						switch( fieldType ){
						case SearchProperties.ATTRIBUTE_TYPE_BOOLEAN:
						case SearchProperties.ATTRIBUTE_TYPE_LONG:
						case SearchProperties.ATTRIBUTE_TYPE_INTEGER:
							disjunction.add( Restrictions.eq( field, parsedValue ) );
						break;
						case SearchProperties.ATTRIBUTE_TYPE_STRING:
							disjunction.add( Restrictions.ilike( field, parsedValue+"", MatchMode.ANYWHERE ) );
						break;
						case SearchProperties.ATTRIBUTE_TYPE_DATE:
							Calendar  calendar = Calendar.getInstance();
							calendar.setTime( (Date) parsedValue );
							calendar.add( Calendar.HOUR, +23 );
							calendar.add( Calendar.MINUTE, +59 );
							calendar.add( Calendar.SECOND, +59 );
							disjunction.add( Restrictions.between( field, parsedValue, calendar.getTime( ) ) );
						break;
						}
						criteria.add( disjunction );
					}
				}
			}
		}
		 
		return criteria;
	}
    
    /**
	 * @author Arturo Sánchez
	 * @author Jose Luis Velazquez
	 * 
	 * Método que devuelve un int que indica el tipo de dato con base en la cadena de entrada
	 * 
	 * @param className String con la clase referencia
	 * @param fieldName Referencia del campo dentro de la clase
	 * @return int con el tipo de dato del campo, si no lo encuentra el tipo de dato es desconocido.
	 */
	private static int getFieldType( String className, String fieldName ){
		
		try {
			
			Class<?> c = Class.forName( className );
			Field f = null;
			
			String fieldsArray[ ] = fieldName.split("\\.");
			
			for (String field : fieldsArray) {
				f = c.getDeclaredField( field );
				c = f.getType( );
			}
			
			String fieldType = f != null ? f.getType( ).toString( ) : "";
			
			switch ( fieldType ) {
				case "class java.lang.Long": 
				case "long":
					return SearchProperties.ATTRIBUTE_TYPE_LONG;
				case "class java.lang.Integer": 
				case "int":
					return SearchProperties.ATTRIBUTE_TYPE_INTEGER;
				case "class java.lang.Boolean": 
				case "boolean":
					return SearchProperties.ATTRIBUTE_TYPE_BOOLEAN;
				case "class java.lang.Double": 
				case "double":
					return SearchProperties.ATTRIBUTE_TYPE_DOUBLE;
				case "class java.lang.String":
					return SearchProperties.ATTRIBUTE_TYPE_STRING;
				case "class java.util.Date": 
					return SearchProperties.ATTRIBUTE_TYPE_DATE;
				default:
					logger.info( "Unknown FieldType " + fieldType + " from field with name " + fieldName + " in the class " + className );
				break;
			}
			
		}catch (ClassNotFoundException | NoSuchFieldException | SecurityException e) {
			logger.info( "Can't get FieldType from field with name " + fieldName + " in the class " + className );
		}

		
		return SearchProperties.ATTRIBUTE_TYPE_UNKNOWN;
	}
}
