$Git="C:\Program Files\Git\mingw64\bin\git.exe"
$SonarQube="C:\Program Files\sonarqube-5.6.6"
$DestFolder="$SonarQube\extensions\plugins"
$ScriptPath = Split-Path $MyInvocation.InvocationName
$TargetSpec="sonar-mscover-plugin-4.0-*.jar"

&$Git diff-index --quiet HEAD --
if($? -eq $false) {
    Write-Host "There are uncommitted files, please commit first"
    &$Git diff-index HEAD --
    exit 1 ;
    }
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
#&$SonarQube\bin\windows-x86-64\StartSonar.bat