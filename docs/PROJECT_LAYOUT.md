# Project Layout

For new people joining the project, this should help you get the lay of the land. The following is a listing of
the directory structure in this repo, and short descriptions of each. Note that we are omitting some directories
that are not useful to describe.

```sh
.
├── .fix-imports.yml # config for scala import cleaner (see readme)
├── docs # Developer documentation
├── app
│   ├── Config.scala # case classes that are a scala representation of *.conf files
│   ├── MacwireApplicationLoader.scala # a replacement for the play app loader that allows for macwire dependency injection
│   ├── Registry.scala # the container of all instantiated dependencies in the project for the running application
│   ├── controllers
│   │   ├── api
│   │   │   ├── BaseApiController.scala # base trait for building API controllers
│   │   │   └── v1 # starting place for api controllers to be stored
│   │   └── composition # where to place action composition helpers like (eg authWithToken)
│   ├── dto # where to place virtual data types (container types used for communicating between layers, but not part of the core data model)
│   │   ├── ApiResponse.scala # DTO used to wrap all responses from the api
│   │   ├── ApiResponseHelpers.scala # mixin to provide helper methods for wrapping data from the API
│   │   └── PagingData.scala # DTO used to send/receive paging information to/from the frontend
│   ├── filters # play filters (similar to middleware in rails)
│   │   ├── DatadogFilter.scala # automatically log request metrics to datadog
│   │   └── LoggingFilter.scala # automatically log request info
│   ├── global # high level app components
│   │   ├── ApiErrorHandler.scala # the top level catcher of exceptions from the application
│   │   ├── Environment.scala # a value class used to represent the currently running environment
│   │   └── Exceptions.scala # all exceptions in the application, tagged with mixing traits to allow for specific matching
│   ├── models # the data models of the application
│   │   └── WithId.scala # a wrapper type to add an id to any model (you can receive a new User from the frontend, but a WithId[User] from the database after persisting)
│   ├── monitoring # helpers for monitoring the system
│   │   ├── StatsDRegistry.scala # a helper to construct the statsd service
│   │   ├── StatsDService.scala # service used to log metrics (usually to datadog)
│   │   └── StatsKey.scala # a wrapper type to help prevent accidental usage of invalid keys/tags
│   ├── persistence # DAOs
│   │   ├── BaseDao.scala # a base trait to provide a database connection
│   │   └── CrudDao.scala # a base trait to provide default CRUD operations and helpers to construct a DAO for a data type
│   ├── services # the core business logic of the system
│   │   └── BaseService.scala # a base trait with service level helpers
│   └── util # utility functions, traits, helpers, and implicit addons (Ops)
│       ├── Logging.scala # mixin to provide a scoped logger
│       ├── Ops.scala # general implicit mixins
│       ├── QueryStringBinder.scala # helpers to allow the play routes file to bind custom types from requests
│       ├── Tap.scala # super useful simple functions that should be methods on all objects
│       └── URIBuilder.scala # helper to allow paging data to generate next/previous urls
├── build.sbt # the main config file for the scala project
├── circle.yml # the build config for running the app on CI
├── conf # play specific config files
│   ├── application.conf # the default config file that gets extended for various special cases
│   ├── messages # i18n key => messages file (use this to avoid littering the code with long strings)
│   ├── prod.conf # the prod config file that extends application.conf
│   └── routes # the main play routes file that uses a DSL to generate request => controller functionality
├── docker-compose.yml # lists dependent application services and allows you to start them all with one command: docker-compose up
├── frontend # the frontend application
│   ├── README.md
│   ├── coffeelint.json
│   ├── dependency-lint.yml
│   ├── package.json
│   ├── postcss.config.js
│   ├── src
│   │   ├── components
│   │   │   ├── app
│   │   │   │   ├── container.coffee
│   │   │   │   ├── index.coffee
│   │   │   │   └── index.styl
│   │   │   ├── container_helpers.coffee
│   │   │   ├── demo
│   │   │   │   ├── app
│   │   │   │   │   ├── container.coffee
│   │   │   │   │   ├── index.coffee
│   │   │   │   │   └── index.styl
│   │   │   │   ├── filtered_todo_list
│   │   │   │   │   ├── index.coffee
│   │   │   │   │   ├── index.styl
│   │   │   │   │   └── index_test.coffee
│   │   │   │   ├── pages
│   │   │   │   │   ├── todos_index_page
│   │   │   │   │   │   ├── container.coffee
│   │   │   │   │   │   └── index.coffee
│   │   │   │   │   └── todos_new_page
│   │   │   │   │       ├── container.coffee
│   │   │   │   │       └── index.coffee
│   │   │   │   ├── todo.coffee
│   │   │   │   ├── todo_form
│   │   │   │   │   ├── index.coffee
│   │   │   │   │   └── index.styl
│   │   │   │   └── todo_list.coffee
│   │   │   ├── dismissable_error.coffee
│   │   │   ├── link.coffee
│   │   │   ├── loading.coffee
│   │   │   └── navigation_link.coffee
│   │   ├── global_styles
│   │   │   ├── clearfix.styl
│   │   │   ├── colors.styl
│   │   │   ├── fonts.styl
│   │   │   ├── index.styl
│   │   │   ├── links.styl
│   │   │   ├── reset.styl
│   │   │   └── text.styl
│   │   ├── index.coffee
│   │   ├── index.pug
│   │   ├── index.styl
│   │   ├── locales
│   │   │   └── en.coffee
│   │   ├── services
│   │   │   ├── builder.coffee
│   │   │   ├── dependencies.coffee
│   │   │   ├── index.coffee
│   │   │   └── todos_service.coffee
│   │   ├── store
│   │   │   ├── actions
│   │   │   │   ├── builder.coffee
│   │   │   │   ├── index.coffee
│   │   │   │   ├── promise_inspections_actions.coffee
│   │   │   │   └── todos_actions.coffee
│   │   │   ├── builder.coffee
│   │   │   ├── helpers
│   │   │   │   └── promise_inspection_builder.coffee
│   │   │   ├── index.coffee
│   │   │   ├── reducer
│   │   │   │   ├── builder.coffee
│   │   │   │   ├── index.coffee
│   │   │   │   ├── promise_inspections_reducer.coffee
│   │   │   │   └── todos_reducer.coffee
│   │   │   └── tests
│   │   │       ├── promise_inspections_test.coffee
│   │   │       └── todos_test.coffee
│   │   └── utils
│   │       └── i18n.coffee
│   ├── tertestrial.yml
│   ├── test
│   │   ├── mocha.opts
│   │   ├── mock_webpack_loaders.coffee
│   │   └── test_helper.coffee
│   ├── webpack.config.coffee
│   └── yarn.lock
├── package.json
├── project # scala project specific files
│   ├── build.properties # sets the sbt version
│   ├── plugins.sbt # adds sbt plugins to the project
│   ├── scalastyle-config.xml # scala linter
│   └── scalastyle-test-config.xml # scala linter used for tests (slightly less strict)
├── scripts # helper scripts for running the project
│   └── kill_chromedriver.sh # used to cleanup chromedriver during cucumber tests on the scala side
├── test #scala tests
│   ├── MockRegistry.scala # a mixin that provides mocked versions of everything found in Registry.scala
│   ├── factories # data model factories for easy generation
│   │   ├── Factory.scala # factory helper trait
│   │   └── ModelFactories.scala # data model factories placed inside an object so they can easily be implicitly imported
│   ├── integration # steps and helpers for running feature tests
│   │   ├── package.scala # shared global state and helpers for feature tests
│   │   └── steps
│   │       ├── BaseSteps.scala # base trait for step definitions
│   │       ├── BeforeTags.scala # available feature/suite tag functionality for cucumber
│   │       └── StepHelpers.scala # shared helpers for feature steps
│   ├── resources # non-code files used during tests
│   │   ├── application.conf # config overrides for all tests
│   │   ├── integration.conf # integration tests config overrides
│   │   ├── logback-test.xml # turns off logging during tests
│   ├── support # shared helpers
│   │   ├── BaseSpec.scala # base trait for tests with shared helper functions
│   │   ├── DBFunctionalSpec.scala # helper trait for running DAO tests against an in memory database
│   │   ├── EvolutionTransformingReader.scala # used to fix translation errors between H2 and Postgres in DBFunctionalSpec
│   │   ├── IntegrationSpec.scala # base trait for integration tests
│   │   ├── MockDependencies.scala # a place to store mock versions of external APIs
│   │   ├── MockHelpers.scala # mockito specific helper functions
│   │   └── MockStatsDServer.java # runs an in memory dummy statsd server for integration tests
└── yarn.lock # locks frontend dependencies to specific versions
```

for the curious... this table was generated using "tree":
* `brew install tree`
* `tree -I 'target*' | pbcopy` (ignore target folder for scala projects or else there's a lot of garbage)
