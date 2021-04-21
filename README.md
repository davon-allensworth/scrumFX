# scrumFX
A Scrum-based arcade game written in JavaFX.

![S C R U M](https://user-images.githubusercontent.com/23053903/109369955-79c9c700-786c-11eb-9622-acb08d0969b6.gif)

# Installation

## Compiling from source

1. Download `javafx` for your system from [here](https://gluonhq.com/products/javafx/).
2. Unzip it to the `Resources` folder
3. Setup your work environment (this will be different for each IDE)
    1. Add references to the `dll`s in the `lib` folder of `javafx`
    2. Add this to the launch args: `--module-path Resources/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.media`
        * Change the `module-path` to the path to your `lib` folder if it's different