$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$contentsDir = Join-Path $projectRoot "src\main\mod-contents"
$variantsDir = Join-Path $projectRoot "src\variants"
$buildDir = Join-Path $projectRoot "build"
$libsDir = Join-Path $buildDir "libs"
$stagingRoot = Join-Path $buildDir "staging"
$modVersion = "2.3.4"

$targets = @(
    @{
        Minecraft = "1.21.11"
        Source = (Join-Path $variantsDir "1.21.11\mod-contents")
        Overlay = $null
    },
    @{ Minecraft = "26.1.2"; Source = $contentsDir; Overlay = $null },
    @{ Minecraft = "26.2"; Source = $contentsDir; Overlay = (Join-Path $variantsDir "26.2") }
)

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

function Copy-Overlay([string]$source, [string]$destination) {
    Get-ChildItem -LiteralPath $source -Recurse -File | ForEach-Object {
        $relativePath = $_.FullName.Substring($source.Length).TrimStart('\')
        $targetPath = Join-Path $destination $relativePath
        New-Item -ItemType Directory -Force -Path (Split-Path -Parent $targetPath) | Out-Null
        Copy-Item -LiteralPath $_.FullName -Destination $targetPath -Force
    }
}

function Get-PngDimensions([string]$path) {
    $bytes = [IO.File]::ReadAllBytes($path)
    if ($bytes.Length -lt 24 -or $bytes[0] -ne 137 -or $bytes[1] -ne 80 -or $bytes[2] -ne 78 -or $bytes[3] -ne 71) {
        throw "Invalid PNG icon: $path"
    }
    $width = ([int]$bytes[16] -shl 24) -bor ([int]$bytes[17] -shl 16) -bor ([int]$bytes[18] -shl 8) -bor [int]$bytes[19]
    $height = ([int]$bytes[20] -shl 24) -bor ([int]$bytes[21] -shl 16) -bor ([int]$bytes[22] -shl 8) -bor [int]$bytes[23]
    return @{ Width = $width; Height = $height }
}

Remove-Item -LiteralPath $buildDir -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path $libsDir | Out-Null
New-Item -ItemType Directory -Force -Path $stagingRoot | Out-Null

foreach ($target in $targets) {
    $minecraftVersion = $target.Minecraft
    $sourceDir = $target.Source
    $stagingDir = Join-Path $stagingRoot $minecraftVersion
    $outputJar = Join-Path $libsDir "NamNBTview-$modVersion-mc$minecraftVersion-fabric.jar"

    if (-not (Test-Path -LiteralPath $sourceDir)) {
        throw "Missing Minecraft $minecraftVersion source: $sourceDir"
    }

    New-Item -ItemType Directory -Force -Path $stagingDir | Out-Null
    Copy-Item -Path (Join-Path $sourceDir "*") -Destination $stagingDir -Recurse -Force

    if ($target.Overlay) {
        if (-not (Test-Path -LiteralPath $target.Overlay)) {
            throw "Missing Minecraft $minecraftVersion overlay: $($target.Overlay)"
        }
        Copy-Overlay $target.Overlay $stagingDir
    }

    $modJsonPath = Join-Path $stagingDir "fabric.mod.json"
    $modJson = Get-Content -LiteralPath $modJsonPath -Raw | ConvertFrom-Json
    $modJson.version = $modVersion
    $modJson.depends.minecraft = $minecraftVersion
    [IO.File]::WriteAllText(
        $modJsonPath,
        ($modJson | ConvertTo-Json -Depth 20) + [Environment]::NewLine,
        [Text.UTF8Encoding]::new($false)
    )

    $manifestPath = Join-Path $stagingDir "META-INF\MANIFEST.MF"
    $manifest = Get-Content -LiteralPath $manifestPath -Raw
    $manifest = $manifest -replace '(?m)^Implementation-Version:.*$', "Implementation-Version: $modVersion"
    $manifest = $manifest -replace '(?m)^Fabric-Minecraft-Version:.*$', "Fabric-Minecraft-Version: $minecraftVersion"
    [IO.File]::WriteAllText($manifestPath, $manifest, [Text.UTF8Encoding]::new($false))

    $iconPath = Join-Path $stagingDir "assets\nbtviewer\logo.png"
    $iconDimensions = Get-PngDimensions $iconPath
    if ($iconDimensions.Width -ne $iconDimensions.Height) {
        throw "Mod icon must be square; found $($iconDimensions.Width)x$($iconDimensions.Height)"
    }

    if ($minecraftVersion -like "26.*") {
        $notebookClass = Join-Path $stagingDir "org\hohigamer\nbtviewer\client\MobNbtNotebookScreen.class"
        $notebookClassText = [Text.Encoding]::UTF8.GetString([IO.File]::ReadAllBytes($notebookClass))
        if ($notebookClassText.Contains("renderTransparentBackground")) {
            throw "Stale screen API reference in Minecraft $minecraftVersion notebook class"
        }
        if (-not $notebookClassText.Contains("extractTransparentBackground")) {
            throw "Missing transparent background API reference in Minecraft $minecraftVersion notebook class"
        }
    }

    if ($minecraftVersion -eq "26.2") {
        $tooltipClass = Join-Path $stagingDir "org\hohigamer\nbtviewer\client\NbtTooltipHandler.class"
        $tooltipClassText = [Text.Encoding]::UTF8.GetString([IO.File]::ReadAllBytes($tooltipClass))
        if (-not $tooltipClassText.Contains("net/minecraft/advancements/predicates/NbtPredicate")) {
            throw "Missing Minecraft 26.2 NbtPredicate API overlay"
        }
        if (-not $tooltipClassText.Contains("net/minecraft/client/gui/Gui")) {
            throw "Missing Minecraft 26.2 GUI API overlay"
        }
    }

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
}

# Minecraft 26.3 is built from Java source because its client/input APIs changed
# substantially and the compatibility fix for Ctrl+U must be compiled against 26.3.
$source263Dir = Join-Path $projectRoot "source\26.3"
$gradle263 = Join-Path $source263Dir "gradlew.bat"
if (-not (Test-Path -LiteralPath $gradle263)) {
    throw "Missing Minecraft 26.3 source build: $gradle263"
}

Push-Location $source263Dir
try {
    & $gradle263 clean build --no-daemon --stacktrace
    if ($LASTEXITCODE -ne 0) {
        throw "Minecraft 26.3 source build failed with exit code $LASTEXITCODE"
    }
} finally {
    Pop-Location
}

$source263Jar = Join-Path $source263Dir "build\libs\NamNBTview-$modVersion-mc26.3-fabric.jar"
$output263Jar = Join-Path $libsDir "NamNBTview-$modVersion-mc26.3-fabric.jar"
if (-not (Test-Path -LiteralPath $source263Jar)) {
    throw "Missing Minecraft 26.3 build artifact: $source263Jar"
}
Copy-Item -LiteralPath $source263Jar -Destination $output263Jar -Force
Write-Host "Built $output263Jar from Java source"
