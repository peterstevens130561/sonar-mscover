package com.stevpet.sonar.plugins.dotnet.specflowtests.opencoverrunner;

public class IntegrationTestState {

    enum State {
        WAITING,RUNNING_TESTS,COMPLETING_TESTS,WRITING_COVERAGE,DONE      
    };
    
    private State state = State.WAITING;
    
    public void setState(State state) {
        this.state = state;
    }
    
    public State getState() {
        return state;
    }
    
    @Override public String toString() {
        return state.toString();
    }
}
