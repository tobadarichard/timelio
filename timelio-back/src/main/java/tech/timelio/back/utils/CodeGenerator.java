package tech.timelio.back.utils;

import java.security.SecureRandom;

public class CodeGenerator {
	public static final String VALID_CHARACTERS = "azertyuiopqsdfghjklmwxcvbn"
			+ "AZERTYUIOPQSDFGHJKLMWXCVBN0123456789";
	public static final int LENGTH_VALID_CHARACTERS = 62;
	
	private CodeGenerator() {
		throw new IllegalStateException("Util class");
	}

	public static String genererCodeAcces() {
		SecureRandom random = new SecureRandom();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i< 30; i++) {
			int index = random.nextInt(LENGTH_VALID_CHARACTERS);
			builder.append(VALID_CHARACTERS.charAt(index));
		}
		return builder.toString();	
	}	
}
