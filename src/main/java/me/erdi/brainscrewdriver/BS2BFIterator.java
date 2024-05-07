package me.erdi.brainscrewdriver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

public class BS2BFIterator implements Iterator<TokenType> {
    private final BufferedReader reader;
    private final boolean forgiving;

    private PrimitiveIterator.OfInt lineIterator = IntStream.of().iterator();
    private TokenType currentToken = TokenType.NONE;

    public BS2BFIterator(String text) {
        this(text, false);
    }

    public BS2BFIterator(String text, boolean forgiving) {
        this(new ByteArrayInputStream(text.getBytes()), forgiving);
    }

    public BS2BFIterator(InputStream stream) {
        this(stream, false);
    }

    public BS2BFIterator(InputStream stream, boolean forgiving) {
        this.reader = new BufferedReader(new InputStreamReader(stream));
        this.forgiving = forgiving;
    }

    private TokenType readToken() throws IOException {
        if(!lineIterator.hasNext()) {
            String line = reader.readLine();

            if(line == null)
                return null;

            while(line.length() == 0) {
                line = reader.readLine();
                if(line == null)
                    return null;
            }

            lineIterator = line.codePoints().iterator();
            if(!lineIterator.hasNext())
                return null;
        }

        TokenType token = TokenType.getFromCodePoint(lineIterator.next());
        currentToken = token;

        return token;
    }

    @Override
    public boolean hasNext() {
        try {
            if(!forgiving) {
                currentToken = TokenType.NONE;

                while(currentToken == TokenType.NONE)
                    if(readToken() == null)
                        return false;

                return currentToken != TokenType.NONE;
            }

            return readToken() != null;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public TokenType next() {
        return currentToken;
    }
}
