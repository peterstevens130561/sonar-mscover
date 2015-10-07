package com.stevpet.sonar.plugins.dotnet.resharper;

public enum ReSharperSeverity {
    ERROR,   //Errors have the highest priority of all - they prevent your code from compiling.
    WARNING,  // ReSharper provides you with warnings that do not prevent your code from compiling but may nevertheless represent serious coding inefficiencies.
    SUGGESTION,  //Code suggestions provide insights into code structure, drawing your attention to things that aren't necessarily bad or wrong, but probably useful to know.
    INFO, //See IssueType "InvocationIsSkipped" has undocumented "INFO" severity -- http://youtrack.jetbrains.com/issue/RSRP-390375
    HINT, //This is the lowest possible severity level. A hint simply brings your attention to a particular code detail and recommends a way of improvement.
    DO_NOT_SHOW
}
