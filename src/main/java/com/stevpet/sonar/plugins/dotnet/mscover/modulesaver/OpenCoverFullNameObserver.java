package com.stevpet.sonar.plugins.dotnet.mscover.modulesaver;

import org.apache.commons.lang.StringUtils;

import com.stevpet.sonar.plugins.common.parser.observer.ObserverRegistrar;


public class OpenCoverFullNameObserver  extends ModuleFullNameObserver {
    private String moduleName ;
    private boolean skipped;

	/* (non-Javadoc)
	 * @see com.stevpet.sonar.plugins.dotnet.mscover.coverageparsers.opencovercoverageparser.ModuleFullNameObserver#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return moduleName;
	}

	@Override
	public void registerObservers(ObserverRegistrar registrar) {
	    registrar.inPath("Modules",
            (modules -> modules.inPath("Module",
                (module -> module.onAttribute("skippedDueTo",this::observeSkippedDueAttribute)
                    .onElement("ModuleName",this::setModuleName)))));
	    registrar.inPath("Modules", modules -> modules.onEntry("Module",() -> skipped=false));
   
	}

    public void setModuleName(String value) {
    	int lastDirSep = value.lastIndexOf("\\");
    	moduleName=value.substring(lastDirSep+1);	
    }
    
    public void observeSkippedDueAttribute(String value) {
        if(StringUtils.isNotEmpty(value)) {
            skipped=true;
        }
    }
    
    @Override
    public boolean getSkipped() {
        return skipped;
    }
    

}
