@ECHO OFF

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.txt del ACTUAL.txt

REM compile the code into the bin folder
set SOURCES=
for /R ..\src\main\java %%f in (*.java) do call set SOURCES=%%SOURCES%% "%%f"

javac -cp ..\src\main\java -Xlint:none -d ..\bin %SOURCES%
IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)
REM no error here, errorlevel == 0

REM run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath ..\bin lumi.Lumi --test < input.txt > ACTUAL.txt

REM compare the output to the expected output
FC ACTUAL.txt EXPECTED.txt
