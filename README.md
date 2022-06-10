# Peaceland

## Module List

### Root

* `architecture.png` is our architecture solution for Peaceland project.
* `subject.pdf` contains the details of the Peaceland project.

### First presentation

* `first_presentation/report.*` contains our detailed answers to the four questions asked;
* `first_presentation/spark_soutenance_intermediaire.pdf` contains our slides for the presentation, as well as our architecture blueprint;

### Alert Screen
* `alert_screen/` contains the front end that peacekeepers use to see alerts 

### Alert Consumer

* `alert_consumer/` contains the module for the consumer that reads over a Kafka stream of peacewatcher reports and sends the ones with low peacescores on the alert stream

### Analytics

* `analytics/` contains the module that downloads data from AWS S3, and then computes analytics to answer the following questions:
  1. Days of the week with the most pissed off people
  1. Number of agitated person per words heard
  1. Agitation per peacewatcher id
  1. Agitation per location
  1. Agitation per hour
 
### Consumer API
 
 * `api_consumer/` contains the module that reads the alert stream and hooks its data to both a Discord and an API endpoint

### Peacewatcher Simulator

* `peacewatcher_simulator/` contains the module for the producer that generates reports and put them on the Kafka stream of peacewatcher reports

### Save Aggregate

* `save_aggregate/` contains the module for the consumer that reads a batch of messages on the Kafka stream of peacewatcher reports, and aggregate them into a single object file, which is then put on AWS S3

## Usage
1. Launch your **zookeeper server**
1. Launch your **kafka server**
1. Launch the **alert_consumer** module using `sbt run` in *alert_consumer/*
1. Launch the **save_aggregate** module using `sbt run` in *save_aggregate/*
1. Launch the **api_consumer** module using `/TODO\`
1. Launch the **analytics** module using `sbt run` in *analytics/*
1. Launch the **alert_screen** front end using `npm start` in *alert_screen/*
1. Launch the **peacewatcher_simulator** using `sbt run` in *peacewatcher_simulator/*
