package com.stevpet.sonar.plugins.dotnet.mscover.parser;

public interface ParserObserver {
    /**
     * Determine whether the current element with text is to be observed
     * @param path to current element
     * @return true if the path matches the element to observe
     */
    boolean isMatch(String path);
    /**
     * Invoked when element is found of which the localName matches the pattern
     * @param name of this element
     * @param text of this element
     */
    void observeElement(String name,String text);
}
