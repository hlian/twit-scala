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
* install running/build tools
  * `npm install`
* install backend dependencies
  * `sbt compile`
* install frontend dependencies
  * `cd frontend && npm install`
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
    $ npm run startall
  ```

You can also start the backend and client seperately by running the following commands in different terminal windows
  ```sh
    $ sbt run
    $ npm run startclient
  ```

## Deploying

### Heroku

To deploy to heroku just setup CircleCI with a Heroku API key. Once that is configured, CircleCI will automatically deploy the `production` branch to Heroku on changes.

## Architecture

The template is divided into two sections, the play backend and the react frontend. Each section can be worked on independently

## Play Backend

The play backend is setup to allow for easy/manual dependency injection with [macwire](https://github.com/adamw/macwire). Everything is wired up through the `Registry` and is loaded up through a custom [Play Application Loader](https://www.playframework.com/documentation/2.5.x/ScalaDependencyInjection) via `MacwireApplicationLoader.scala`. The play backend includes: a `CrudDao` to facilitate making basic CRUD requests in a database, a `Config` object that reads from `application.conf`.

### Testing

Testing is done using scalatest for both unit and function tests. Full features tests are also supported through Cucumber. To run tests the following commands can be used
  ```sh
    $ sbt test
    $ sbt cucumber
  ```

### Style

For styling the [Originate Scala Guide](https://github.com/Originate/guide/blob/master/scala/README.md) should be used. Scalastyle is used for linting the code and to make sure it meets style requirements. Scalastyle is executed when tests are run, but it can be invoked by itself by using
  ```
    $ sbt scalastyle
  ```

### Organizing Imports
Additionally [Bowbaq/scala-imports](https://github.com/Bowbaq/scala-imports) is used to organize imports and removed unused ones. To run, just execute `fix-imports` at the root of the repository. The configuration file can be found at `./.fix-imports.yml`

## React Frontend

The react frontend is setup to be build using [Webpack](https://webpack.github.io/) and the configuration is minimal enough where it should be easy to change settings to fit the needs of the project. By default the application is setup with `React`, `Redux` and `React-Router` using ES6. For styling `LESS` is used.

Additionally webpack is configured to include the path of the main `index.js` as a resolution path. This allows for imports to be done from the root of the frontend project without having to deal with relative paths.
  ```js
    // without resolution path
    import foo from '../../../components/foo'
    // with resolution path
    import foo from './components/foo'
  ```

### Testing

**Figure this out since we can go mochoa/chai route, but as far as I know jest is preferred when working with React**. Tests can be run by running `npm test` and the linter can be runned using `npm run lint`

### Style

**Need to figure out what language is going to be used in here**
