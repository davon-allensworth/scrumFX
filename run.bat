cd src
del *.class
set PATH_TO_FX="..\javafx-sdk-11.0.2\lib"
javac --module-path %PATH_TO_FX% --add-modules javafx.controls Main.java
java --module-path %PATH_TO_FX% --add-modules javafx.controls Main
cd ..
