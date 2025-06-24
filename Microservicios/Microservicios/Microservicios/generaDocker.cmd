@echo off

echo ==== Iniciando construcción de imágenes Docker ====

for /d %%D in (*) do (
    if exist "%%D\Dockerfile" (
        echo.
        echo >>> Construyendo imagen para: %%D
        docker build -t gogga09/%%D:latest %%D

        echo >>> Subiendo imagen: gogga09/%%D:latest
        docker push gogga09/%%D:latest
    ) else (
        echo.
        echo >>> Omitido: %%D (no contiene Dockerfile)
    )
)

echo.
echo ==== Proceso finalizado ====
pause
