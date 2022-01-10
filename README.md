# FormulaMetrics

This is a Scala 2.13 UDP telemetry client for the F1 2021 game. It was developed to export some race metrics (lap times, classification) in CSV format. The main purpose of this project was to learn a bit of Scala, so it is not full-featured and the code can be improved. You can find the protocol documentation [here](https://forums.codemasters.com/topic/80231-f1-2021-udp-specification/).

## Usage

```sh
# run tests
sbt test

# run cli with optional arguments
sbt run localhost 20777 f1-data/

# compile
sbt compile

# build fat/uber jar
sbt assembly
```
