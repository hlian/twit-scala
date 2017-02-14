# Play Backend

The play backend is setup to allow for easy/manual dependency injection with [macwire](https://github.com/adamw/macwire). Everything is wired up through the `Registry` and is loaded up through a custom [Play Application Loader](https://www.playframework.com/documentation/2.5.x/ScalaDependencyInjection) via `MacwireApplicationLoader.scala`. The play backend includes: a `CrudDao` to facilitate making basic CRUD requests in a database, a `Config` object that reads from `application.conf`.

## Testing

Testing is done using scalatest for both unit and function tests. Full features tests are also supported through Cucumber. To run tests the following commands can be used
  ```sh
    $ sbt test
    $ sbt cucumber
  ```

## Style

For styling the [Originate Scala Guide](https://github.com/Originate/guide/blob/master/scala/README.md) should be used. Scalastyle is used for linting the code and to make sure it meets style requirements. Scalastyle is executed when tests are run, but it can be invoked by itself by using
  ```
    $ sbt scalastyle
  ```

## Organizing Imports
Additionally [Bowbaq/scala-imports](https://github.com/Bowbaq/scala-imports) is used to organize imports and removed unused ones. To run, just execute `fix-imports` at the root of the repository. The configuration file can be found at `./.fix-imports.yml`
