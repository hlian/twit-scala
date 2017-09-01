# Play-React Template

This is a template for building applications in Play & React. This includes basic infrastructure to minimize startup time on a new project, but as such it should be updated accordingly to fit the needs of the project.

## Documentation

Detailed documentation on a number of topics can be found in the `docs/` subdirectory of this repo.  This README provides a high-level overview of this system, while detailed information about specific subtopics can be found in separate documentation as follows:

* [Detailed project layout](./docs/PROJECT_LAYOUT.md)
* [Developer guide for backend engineering](./docs/DEVGUIDE_BACKEND.md)
* [Developer guide for frontend engineering](./docs/DEVGUIDE_FRONTEND.md)

## Contents

* [Project Setup](#setup)
  * [Dependencies](#dependencies)
  * [Database](#database)
* [Running the Project](#running)
* [Deploying](#deploying)
  * [Heroku](#heroku)
* [Architecture](#architecture)

## Setup

### Dependencies

The commands below are for macOS; you will have to tailor them to your own OS.

* `brew update`
* Install [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and [docker](https://www.docker.com/products/docker#/mac)
  * `brew cask install java docker`
* Install [sbt](http://www.scala-sbt.org/download.html), [Chromedriver](https://sites.google.com/a/chromium.org/chromedriver/), [yarn](https://yarnpkg.com/en/), [docker-compose](https://docs.docker.com/compose/install/)
  * `brew install sbt chromedriver yarn docker-compose`
  * On OS X, docker-compose should come with Docker and you should be able to drop it off the command
* Install backend dependencies
  * `cd backend; sbt compile`
* Install frontend dependencies
  * `cd frontend; yarn install`
* Install [fix-imports](https://github.com/Bowbaq/scala-imports)
  * Go to [releases page](https://github.com/Bowbaq/scala-imports/releases) and download the binary for your platform and add it somewhere on your `$PATH`

### Database

The database is managed through Docker and can be started up with `docker-compose up`. Additionally if you want to run the docker process in the background you can run `docker-compose up -d` instead.

The database is mounted in the `tmp` directory, so that folder can be deleted if the database needs to be nuked.

## Running

Once all dependencies are installed and the database is running, you can start the backend via `cd backend; sbt run` and the frontend via `cd frontend; yarn run startclient`.

## Deploying

### Heroku

To deploy to heroku just setup CircleCI with a Heroku API key. Once that is configured, CircleCI will automatically deploy the `production` branch to Heroku on changes.

## Architecture

The template is divided into two sections, the Play backend and the React frontend.

For specific documentation on each, please refer to the dev guides for the [backend](./DEVGUIDE_BACKEND.md) or [frontend](./DEVGUIDE_FRONTEND.md)
