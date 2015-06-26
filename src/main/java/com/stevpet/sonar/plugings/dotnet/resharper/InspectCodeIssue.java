package com.stevpet.sonar.plugings.dotnet.resharper;


public class InspectCodeIssue {
    private String typeId;
    private String relativePath;
    private String line;
    private String message;
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    /**
     * @param relativePath - relative to solution dir
     */
    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
    public String getLine() {
        return line;
    }
    public void setLine(String line) {
        this.line = line;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getRelativePath() {
        return relativePath;
    }

}
