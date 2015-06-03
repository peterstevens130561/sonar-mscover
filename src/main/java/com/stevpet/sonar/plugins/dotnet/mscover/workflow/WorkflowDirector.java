package com.stevpet.sonar.plugins.dotnet.mscover.workflow;

import org.picocontainer.DefaultPicoContainer;

public interface WorkflowDirector {

    void wire(DefaultPicoContainer container);

    void execute();


}