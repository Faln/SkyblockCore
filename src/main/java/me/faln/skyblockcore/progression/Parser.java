package me.faln.skyblockcore.progression;

/**
 * Functional interface for parsing a @String to @T
 *
 * @author Faln
 */
@FunctionalInterface
public interface Parser<T> {

    T  parse(final String s);

}
