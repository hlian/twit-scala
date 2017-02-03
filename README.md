# Play-React Template

This is a template for building applications in Play & React. This includes basic infrastructure to minimize startup time on a new project, but as such it should be updated accordingly to fit the needs of the project.

## Setup

### Dependencies

* `brew update`
* install [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
  * `brew cask install java`
* install [sbt](http://www.scala-sbt.org/download.html)
  * `brew install sbt`
* install [Chromedriver](https://sites.google.com/a/chromium.org/chromedriver/)
  * `brew install chromedriver`
* install backend dependencies
  * `sbt compile`
* install run/build tools (also installs frontend dependencies)
  * `yarn install`
* install [docker](https://www.docker.com/products/docker#/mac)
* install [docker-compose](https://docs.docker.com/compose/install/)
  * note: on OSX the installation of docker comes with docker-compose
* install [fix-imports](https://github.com/Bowbaq/scala-imports)
  * go to [releases page](https://github.com/Bowbaq/scala-imports/releases) and download the binary for your platform and add it somewhere on your `$PATH`

### Database

The database is managed through Docker and can be started up with `docker-compose up`. Additionally if you want to run the docker process in the background you can run `docker-compose up -d` instead.

The database is mounted in the `tmp` directory, so that folder can be deleted if the database needs to be nuked.

## Running

Once all dependencies are installed and the database is running, you can start both the backend and client simultaneously by running
  ```sh
    $ yarn run startall
  ```

You can also start the backend and client seperately by running the following commands in different terminal windows
  ```sh
    $ sbt run
    $ yarn run startclient
  ```

## Deploying

### Heroku

To deploy to heroku just setup CircleCI with a Heroku API key. Once that is configured, CircleCI will automatically deploy the `production` branch to Heroku on changes.

## Architecture

The template is divided into two sections, the play backend and the react frontend. Each section can be worked on independently.

For specific documentation on each, please refer to the dev guides for the [backend](./DEVGUIDE_BACKEND.md) or [frontend](./DEVGUIDE_FRONTEND.md)
