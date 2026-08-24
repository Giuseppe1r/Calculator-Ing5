# Deploy backend automatically on Windows
# Ejecuta desde el directorio del proyecto o desde la raíz con la ruta correcta.
Set-ExecutionPolicy Bypass -Scope Process -Force
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $scriptDir

Write-Host "[BACKEND] Preparando despliegue del backend..."

# Crear regla de firewall para el puerto 8080
Write-Host "[BACKEND] Aprobando puerto 8080 en firewall..."
if (-not (Get-NetFirewallRule -DisplayName 'CalculatorBackend8080' -ErrorAction SilentlyContinue)) {
    New-NetFirewallRule -DisplayName 'CalculatorBackend8080' -Direction Inbound -LocalPort 8080 -Protocol TCP -Action Allow -Profile Any | Out-Null
    Write-Host "[BACKEND] Regla de firewall creada: CalculatorBackend8080"
} else {
    Write-Host "[BACKEND] La regla de firewall ya existe: CalculatorBackend8080"
}

# Instalar dependencias y compilar si es necesario
Write-Host "[BACKEND] Compilando el proyecto con Maven..."
if (Test-Path "pom.xml") {
    & mvn clean package -DskipTests
    if ($LASTEXITCODE -ne 0) {
        Write-Error "[BACKEND] Maven falló. Revisa los errores anteriores."
        exit $LASTEXITCODE
    }
} else {
    Write-Error "[BACKEND] No se encontró pom.xml en $scriptDir"
    exit 1
}

# Ejecutar el JAR empaquetado
$jarPath = Join-Path $scriptDir 'target\calculator-0.0.1-SNAPSHOT.jar'
if (-Not (Test-Path $jarPath)) {
    Write-Error "[BACKEND] No se encontró el JAR en $jarPath"
    exit 1
}

Write-Host "[BACKEND] Iniciando el backend..."
Start-Process -NoNewWindow -FilePath 'java' -ArgumentList "-jar `"$jarPath`"" -WorkingDirectory $scriptDir
Write-Host "[BACKEND] Backend iniciado. Accede a http://<IP-del-backend>:8080"
