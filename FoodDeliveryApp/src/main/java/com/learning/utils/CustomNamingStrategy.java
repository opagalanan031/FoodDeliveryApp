package com.learning.utils;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CustomNamingStrategy extends PhysicalNamingStrategyStandardImpl {

	// to modify table name
	// Identifier - holds table details
	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		
		String newName = name.getText().concat("_TABLE");
		
		return Identifier.toIdentifier(newName);
	}
}
