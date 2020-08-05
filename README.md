## Term Tetris
Play tetris in your terminal

## Requirements
java 1.8
Ascii compatible terminal

## How to play
In terminal:
java -jar TermTetris.jar {speed}

1. press `r` to rotate shape
2. press `space` to drop shape right away
3. press `<-` to move shape left
4. press `->` to move shape right

P.S {speed} is delay in ms in between shapes been making its way down(the smaller it is the faster shapes will drop)
default is 300ms 

### To build

1. Install [gradle](https://gradle.org/)
2. ./gradlew fatjar or gradle flatjar 
3. java -jar ./build/libs/TermTetris-all-1.0-SNAPSHOT.jar

### TODO
* add exe build https://github.com/TheBoegl/gradle-launch4j
