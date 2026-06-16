$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$contentsDir = Join-Path $projectRoot "src\main\mod-contents"
$buildDir = Join-Path $projectRoot "build"
$libsDir = Join-Path $buildDir "libs"
$stagingDir = Join-Path $buildDir "staging"
$outputJar = Join-Path $libsDir "NamNBTview-2.2.0-mc26.1.2-fabric.jar"

if (-not (Test-Path -LiteralPath $contentsDir)) {
    throw "Missing mod contents: $contentsDir"
}

$jarCandidates = @()
if ($env:JAVA_HOME) {
    $jarCandidates += (Join-Path $env:JAVA_HOME "bin\jar.exe")
}
$jarCandidates += "C:\Users\novic\.jdks\temurin-24.0.2\bin\jar.exe"
$jarCandidates += "jar.exe"

$jarExe = $null
foreach ($candidate in $jarCandidates) {
    $cmd = Get-Command $candidate -ErrorAction SilentlyContinue
    if ($cmd) {
        $jarExe = $cmd.Source
        break
    }
}

if (-not $jarExe) {
    throw "Could not find jar.exe. Install a JDK or set JAVA_HOME."
}

Remove-Item -LiteralPath $buildDir -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path $libsDir | Out-Null
New-Item -ItemType Directory -Force -Path $stagingDir | Out-Null

Copy-Item -Path (Join-Path $contentsDir "*") -Destination $stagingDir -Recurse -Force

Push-Location $stagingDir
try {
    & $jarExe cMf $outputJar .
} finally {
    Pop-Location
}

if (-not (Test-Path -LiteralPath $outputJar)) {
    throw "Build failed: $outputJar was not created"
}

Write-Host "Built $outputJar"
