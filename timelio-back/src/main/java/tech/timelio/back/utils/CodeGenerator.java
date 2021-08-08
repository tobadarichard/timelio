package tech.timelio.back.utils;

import java.security.SecureRandom;

public class CodeGenerator {
	public static final String VALID_CHARACTERS = "azertyuiopqsdfghjklmwxcvbn"
			+ "AZERTYUIOPQSDFGHJKLMWXCVBN0123456789";
	public static final int LENGTH_VALID_CHARACTERS = 62;
	
	private CodeGenerator() {
		throw new IllegalStateException("Util class");
	}
	
	public static String genererSequence(int size) {
		if (size < 1) throw new IllegalArgumentException();
		SecureRandom random = new SecureRandom();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i< size; i++) {
			int index = random.nextInt(LENGTH_VALID_CHARACTERS);
			builder.append(VALID_CHARACTERS.charAt(index));
		}
		return builder.toString();	
	}

	public static String genererCodeAcces() {
		return CodeGenerator.genererSequence(50);
	}
	
	public static String genererValeurToken() {
		return CodeGenerator.genererSequence(50);
	}
	
	public static String genererSelToken() {
		return CodeGenerator.genererSequence(30);
	}
}
