# Deploy frontend automatically on Windows
# Ejecuta desde el directorio frontend/ing5-t1 o desde la raíz con la ruta correcta.
#
# NOTA: este script es la via de despliegue SIN Docker (Vite dev server contra un
# backend que corre en otra maquina). Para el despliegue con contenedores usa
# "docker compose up -d --build" desde la raiz del repositorio.
param(
    [Parameter(Mandatory = $true)]
    [ValidateNotNullOrEmpty()]
    [string]$BackendIp
)

Set-ExecutionPolicy Bypass -Scope Process -Force
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $scriptDir

Write-Host "[FRONTEND] Preparando despliegue del frontend..."

# Crear .env.local con la URL del backend
$envFile = Join-Path $scriptDir '.env.local'
$backendUrl = "http://${BackendIp}:8080"
Write-Host "[FRONTEND] Escribiendo configuración de backend en $envFile"
# Se escribe sin BOM a proposito: "Set-Content -Encoding UTF8" lo anade en
# PowerShell 5.1 y Vite acaba leyendo la clave como "<BOM>VITE_API_BASE_URL",
# de modo que la ignora y el frontend se queda sin URL de backend.
$utf8SinBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllText($envFile, "VITE_API_BASE_URL=$backendUrl`r`n", $utf8SinBom)

# Instalar dependencias si es necesario
if (-not (Test-Path 'node_modules')) {
    Write-Host "[FRONTEND] node_modules no existe, instalando dependencias..."
    npm ci
    if ($LASTEXITCODE -ne 0) {
        Write-Error "[FRONTEND] npm ci falló. Revisa los errores anteriores."
        exit $LASTEXITCODE
    }
} else {
    Write-Host "[FRONTEND] Dependencias ya instaladas."
}

# Ejecutar Vite
Write-Host "[FRONTEND] Iniciando Vite dev server..."
npm run dev
