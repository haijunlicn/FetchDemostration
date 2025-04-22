package com.spring.model;

import java.security.SecureRandom;

public class PasswordGenerator {
	private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
	private static final String DIGITS = "0123456789";
	private static final String SPECIALS = "!@#$%^&*";
	private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIALS;
	private static final int PASSWORD_LENGTH = 12;
	private static final SecureRandom RANDOM = new SecureRandom();

	public static String generatePassword() {
		StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

		// Ensure at least one character from each required category
		password.append(UPPERCASE.charAt(RANDOM.nextInt(UPPERCASE.length())));
		password.append(LOWERCASE.charAt(RANDOM.nextInt(LOWERCASE.length())));
		password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
		password.append(SPECIALS.charAt(RANDOM.nextInt(SPECIALS.length())));

		// Fill the remaining slots with random characters
		for (int i = 4; i < PASSWORD_LENGTH; i++) {
			password.append(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())));
		}

		// Shuffle the password to avoid predictable order
		return shuffleString(password.toString());
	}

	private static String shuffleString(String input) {
		char[] array = input.toCharArray();
		for (int i = array.length - 1; i > 0; i--) {
			int j = RANDOM.nextInt(i + 1);
			char temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
		return new String(array);
	}

	public static void main(String[] args) {
		System.out.println(generatePassword());
	}
}
