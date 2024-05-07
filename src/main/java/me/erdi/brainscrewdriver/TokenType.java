package me.erdi.brainscrewdriver;

import java.util.Arrays;

public enum TokenType {
    INCREMENT('+', "\uD83D\uDC4D"), // thumbs up
    DECREMENT('-', "\uD83D\uDC4E"), // thumbs down
    PRINT('.', "☝"),  // index pointing up
    INPUT(',', "\uD83D\uDC47"), // (backhand) index pointing down
    LOOP_BEGIN('[', "\uD83D\uDE0E"), // sunglasses
    LOOP_END(']', "\uD83D\uDC40"), // looking eyes
    TAPE_LEFT('<', "\uD83D\uDC48"), // pointing left
    TAPE_RIGHT('>', "\uD83D\uDC49"), // pointing right
    NONE('\0', null); // for forgiving iterators

    private final char plain;
    private final char[] emoji;

    TokenType(char plain, String emoji) {
        this.plain = plain;

        if(emoji == null) {
            this.emoji = new char[0];
            return;
        }

        if(emoji.length() == 1) {
            this.emoji = new char[1];
            this.emoji[0] = emoji.charAt(0);

            return;
        }

        this.emoji = new char[2];
        this.emoji[0] = emoji.charAt(0);
        this.emoji[1] = emoji.charAt(1);
    }

    public char getPlain() {
        return plain;
    }

    public char[] getEmoji() {
        return Arrays.copyOf(emoji, emoji.length);
    }

    public static TokenType getFromCodePoint(int codePoint) {
        return getFromChars(Character.toChars(codePoint));
    }

    public static TokenType getFromChars(char[] chars) {
        for(TokenType type : values())
            if(Arrays.equals(type.emoji, chars))
                return type;

        return NONE;
    }

    public static TokenType getFromPlainOperator(char operator) {
        for(TokenType type : values())
            if(type.plain == operator)
                return type;

        return NONE;
    }
}
