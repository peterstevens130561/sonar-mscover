package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.util.List;

import org.sonar.api.batch.TimeMachine;
import org.sonar.api.batch.TimeMachineQuery;
import org.sonar.api.measures.Measure;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import com.stevpet.sonar.plugins.dotnet.mscover.mock.GenericClassMock;

public class TimeMachineMock extends GenericClassMock<TimeMachine> {

    public TimeMachineMock() {
        super(TimeMachine.class);
        // TODO Auto-generated constructor stub
    }

    public void givenQuery(List<Measure> measures) {
        when(instance.getMeasures(any(TimeMachineQuery.class))).thenReturn(measures);
    }

}
