﻿$Git="C:\Program Files\Git\mingw64\bin\git.exe"
$SonarQube="C:\Program Files\sonarqube-5.6.6"
$Destinations="\\dftweb02\SonarQube\sonarqube-5.6.4-Test","C:\Program Files\sonarqube-5.6.6"
$DestFolder="$SonarQube\extensions\plugins"
$ScriptPath = Split-Path "$($MyInvocation.InvocationName)"
$TargetSpec="sonar-mscover-plugin-4.0-*.jar"

&$Git diff-index --quiet HEAD --
if($? -eq $false) {
    Write-Host "There are uncommitted files, please commit first"
    &$Git diff-index HEAD --
    exit 1 ;
 }

[xml]$myXML = Get-Content pom.xml
$TimeStamp = Get-Date -Format "yyMMddHHmm"
$Version="4.0-SN$($TimeStamp)"
$myXML.project.version = $Version
$myXML.Save("$ScriptPath/pom.xml")

echo $ScriptPath
cd $ScriptPath
if(Test-Path target) {
    Remove-Item -Path target -Force -Recurse
}
&mvn -o clean install
if($? -eq $false) {
        Write-Error "Build failed"
        exit 1
 } 
Remove-Item -Path "$DestFolder\$TargetSpec"
Get-ChildItem -Path target -Filter "$TargetSpec" -Recurse -File | Copy-Item -Destination $DestFolder
ls $DestFolder
&$Git add pom.xml
&$Git commit --message "snapshot build $Version"
$myXML.project.version = "4.0-SNAPSHOT"
&$Git add pom.xml
&$Git commit --message "back to snapshot"
$myXML.Save("$ScriptPath/pom.xml")

#&$SonarQube\bin\windows-x86-64\StartSonar.bat