package com.stevpet.sonar.plugins.dotnet.mscover.opencover.sensor;

import java.util.ArrayList;
import java.util.List;

import org.sonar.plugins.dotnet.api.microsoft.VisualStudioProject;
import org.sonar.plugins.dotnet.api.microsoft.VisualStudioSolution;

import com.stevpet.sonar.plugins.dotnet.mscover.opencover.command.OpenCoverCommand;

public class OpenCoverFilterBuilder {

    private OpenCoverCommand openCoverCommand;
    private VisualStudioSolution solution;
    
    public void setOpenCoverCommand(OpenCoverCommand openCoverCommand) {
        this.openCoverCommand = openCoverCommand;
    }
    
    public void setSolution(VisualStudioSolution solution) {
        this.solution = solution;
    }
    
    public void build() {
        List<String> excludeFilters = new ArrayList<String>();
        excludeFilters.add("*\\*.Designer.cs");
        openCoverCommand.setExcludeByFileFilter(excludeFilters);
        openCoverCommand.setExcludeFromCodeCoverageAttributeFilter();
        String filter = getAssembliesToIncludeInCoverageFilter();
        openCoverCommand.setFilter(filter); 
    }
    
    public String getAssembliesToIncludeInCoverageFilter() {
        final StringBuilder filterBuilder = new StringBuilder();
        // We add all the covered assemblies
        for (String assemblyName : listCoveredAssemblies()) {
          filterBuilder.append("+[" + assemblyName + "]* ");
        }
        return filterBuilder.toString();
    }
    
    public List<String> listCoveredAssemblies() {
        List<String> coveredAssemblyNames = new ArrayList<String>();
        for (VisualStudioProject visualProject : solution.getProjects()) {
            coveredAssemblyNames.add(visualProject.getAssemblyName());
        }
        return coveredAssemblyNames;
      }

}
