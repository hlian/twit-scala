import com.typesafe.sbt.packager.docker._

name := "originate-play"

organization := "com.originate"

version := "0.1"

scalaVersion in ThisBuild := "2.12.2"

resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += Resolver.jcenterRepo

/*
 * scalac configuration
 */

val commonScalacOptions = Seq(
  "-encoding", "UTF-8", // Specify character encoding used by source files
  "-target:jvm-1.8", // Target platform for object files
  "-Xexperimental", // Enable experimental extensions
  "-Xfuture", // Turn on future language features
  "-Xlint:-unused,_", // Play compiles conf/routes into a file that triggers this warning a ton
  "-Ywarn-unused:-imports,-patvars,-locals,-privates,_" // See above
)

val compileScalacOptions = Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs
  "-feature", // Emit warning and location for usages of features that should be imported explicitly
  "-g:vars", // Set level of generated debugging info: none, source, line, vars, notailcalls
  "-unchecked", // Enable additional warnings where generated code depends on assumptions
  "-Xfatal-warnings", // Fail the compilation if there are any warnings
  "-Xlint:_", // Enable or disable specific warnings
  "-Yno-adapted-args", // Do not adapt an argument list to match the receiver
  "-Ywarn-dead-code", // Warn when dead code is identified
  "-Ywarn-numeric-widen" // Warn when numerics are widened (Not really useful)
)

scalacOptions ++= commonScalacOptions ++ compileScalacOptions ++ Seq(
  "-Ywarn-value-discard" // Warn when non-Unit expression results are unused
)

scalacOptions in (Test, compile) := commonScalacOptions ++ compileScalacOptions

scalacOptions in (Compile, console) := commonScalacOptions ++ Seq(
  "-language:_", // Enable or disable language features
  "-nowarn" // Generate no warnings
)

scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

/*
 * Play
 */

lazy val root = (project in file(".")).enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator

// Automatic imports in file generated from routes
routesImport ++= Seq(
  "com.originate.dto._",
  "org.joda.time._",
  "com.originate.util.QueryStringBinders._",
  "com.originate.util.PathBinders._"
)

val playManagedSources = Def.setting(crossTarget.value / "routes" / "main")
val twirlManagedSources = Def.setting(crossTarget.value / "twirl" / "main")

publishArtifact in (Compile, packageDoc) := false

publishArtifact in packageDoc := false

publishArtifact in packageSrc := false

sources in (Compile, doc) := Seq.empty

/*
 * Managed dependencies
 */

libraryDependencies ++= Seq(
  evolutions,
  filters,
  jdbc,
  ws
)

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  "commons-codec"                     % "commons-codec"                    % "1.10",
  "com.softwaremill.macwire"         %% "macros"                           % "2.2.5"                         % Provided,
  "com.softwaremill.macwire"         %% "proxy"                            % "2.2.5",
  "com.softwaremill.macwire"         %% "util"                             % "2.2.5",
  "org.eclipse.jgit"                  % "org.eclipse.jgit"                 % "4.5.0.201609210915-r",
  "org.scala-lang"                    % "scala-reflect"                    % scalaVersion.value,
  "com.github.pureconfig"            %% "pureconfig"                       % "0.7.2",
  "joda-time"                         % "joda-time"                        % "2.9.9",
  "com.indeed"                        % "java-dogstatsd-client"            % "2.0.16",
  "com.typesafe.play"                %% "anorm"                            % "2.6.0-M1",
  "com.typesafe.play"                %% "play-json"                        % "2.6.3",
  "com.typesafe.play"                %% "play-json-joda"                   % "2.6.3",
  "org.xerial"                        % "sqlite-jdbc"                      % "3.21.0",
  "com.iheart"                       %% "play-swagger"                     % "0.6.1-PLAY2.6",
  "org.webjars"                       % "swagger-ui"                       % "2.2.10"
)

libraryDependencies ++= Seq(
  "org.mockito"                       % "mockito-core"                     % "2.2.29",
  "org.scalatestplus.play"           %% "scalatestplus-play"               % "3.1.1",
  "io.cucumber"                       % "cucumber-core"                    % "2.0.0-SNAPSHOT",
  "io.cucumber"                      %% "cucumber-scala"                   % "2.0.0-SNAPSHOT",
  "io.cucumber"                       % "cucumber-jvm"                     % "2.0.0-SNAPSHOT",
  "com.h2database"                    % "h2"                               % "1.4.196"
) map (_ % Test)

/*
 * sbt options
 */

// Statements executed when starting the Scala REPL (sbt's `console` task)

initialCommands := """
import
  scala.annotation.{switch, tailrec},
  scala.beans.{BeanProperty, BooleanBeanProperty},
  scala.collection.JavaConverters._,
  scala.collection.{breakOut, mutable},
  scala.concurrent.{Await, ExecutionContext, Future},
  scala.concurrent.ExecutionContext.Implicits.global,
  scala.concurrent.duration._,
  scala.language.experimental.macros,
  scala.math._,
  scala.reflect.macros.blackbox,
  scala.util.{Failure, Random, Success, Try},
  scala.util.control.NonFatal,
  java.io._,
  java.net._,
  java.nio.file._,
  java.time.{Duration => jDuration, _},
  System.{currentTimeMillis => now},
  System.nanoTime

def desugarImpl[T](c: blackbox.Context)(expr: c.Expr[T]): c.Expr[Unit] = {
  import c.universe._, scala.io.AnsiColor.{BOLD, GREEN, RESET}

  val exp = show(expr.tree)
  val typ = expr.actualType.toString takeWhile '('.!=

  println(s"$exp: $BOLD$GREEN$typ$RESET")
  reify { (): Unit }
}

def desugar[T](expr: T): Unit = macro desugarImpl[T]

import play.api.{ApplicationLoader, Environment, Mode}
import com.originate.MacwireApplicationLoader
import com.originate.models._
val env = Environment(new java.io.File("."), this.getClass.getClassLoader, Mode.Dev)
val context = ApplicationLoader.createContext(env)
val loader = new MacwireApplicationLoader
val registry = loader.loadRegistry(context)
import registry._
"""

// Do not exit sbt when Ctrl-C is used to stop a running app
cancelable in Global := true

// Improved incremental compilation
incOptions := incOptions.value.withNameHashing(true)

// Improved dependency management
updateOptions := updateOptions.value.withCachedResolution(true)

showSuccess := true

showTiming := true

// Uncomment to enable offline mode
// offline := true

// Enable colors in Scala console (2.11.4+)
initialize ~= { _ =>
  val ansi = System.getProperty("sbt.log.noformat", "false") != "true"
  if (ansi) System.setProperty("scala.color", "true")
}

// Draw a separator between triggered runs (e.g, ~test)
triggeredMessage := { ws =>
  if (ws.count > 1) {
    val ls = System.lineSeparator * 2
    ls + "#" * 100 + ls
  } else ""
}

shellPrompt := { state =>
  import scala.Console.{BLUE, BOLD, RESET}
  s"$BLUE$BOLD${name.value}$RESET $BOLD\u25b6$RESET "
}

/*
 * Scalastyle: http://www.scalastyle.org/
 */

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"
(scalastyleConfig in Test) := baseDirectory.value / "project" / "scalastyle-test-config.xml"

scalastyleFailOnError := true

// Create a default Scalastyle task to run with tests
lazy val mainScalastyle = taskKey[Unit]("mainScalastyle")
lazy val testScalastyle = taskKey[Unit]("testScalastyle")

mainScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value
testScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Test).toTask("").value

(test in Test) := ((test in Test) dependsOn testScalastyle).value
(test in Test) := ((test in Test) dependsOn mainScalastyle).value

/*
 * WartRemover: http://github.com/puffnfresh/wartremover
 */

wartremoverErrors ++= Seq(
  Wart.Any,
  Wart.EitherProjectionPartial,
  Wart.Equals,
  Wart.ExplicitImplicitTypes,
  Wart.ImplicitConversion,
  Wart.IsInstanceOf,
  Wart.JavaConversions,
  Wart.LeakingSealed,
  Wart.Null,
  Wart.Option2Iterable,
  Wart.OptionPartial,
  Wart.Product,
  Wart.Return,
  Wart.Serializable,
  Wart.StringPlusAny,
  Wart.TraversableOps,
  Wart.TryPartial,
  Wart.Var
)

// Play Framework
wartremoverWarnings ++= Seq(
  PlayWart.AssetsObject,
  PlayWart.CookiesPartial,
  PlayWart.FlashPartial,
  PlayWart.FormPartial,
  PlayWart.HeadersPartial,
  PlayWart.JavaApi,
  PlayWart.JsLookupResultPartial,
  PlayWart.JsReadablePartial,
  PlayWart.LangObject,
  PlayWart.MessagesObject,
  PlayWart.SessionPartial
)

// Bonus Warts
wartremoverWarnings ++= Seq(
  PlayWart.AssetsObject,
  PlayWart.CookiesPartial,
  PlayWart.FlashPartial,
  PlayWart.FormPartial,
  PlayWart.HeadersPartial,
  PlayWart.JavaApi,
  PlayWart.JsLookupResultPartial,
  PlayWart.JsReadablePartial,
  PlayWart.LangObject,
  PlayWart.MessagesObject,
  PlayWart.SessionPartial,
  PlayWart.TypedMapPartial,
  PlayWart.WSResponsePartial,
  ExtraWart.EnumerationPartial,
  ExtraWart.FutureObject,
  ExtraWart.GenMapLikePartial,
  ExtraWart.GenTraversableLikeOps,
  ExtraWart.GenTraversableOnceOps,
  ExtraWart.ScalaGlobalExecutionContext,
  ExtraWart.StringOpsPartial,
  ExtraWart.TraversableOnceOps
)

// Exclude Play auto-generated sources

wartremoverExcluded += playManagedSources.value / "router" / "Routes.scala"
wartremoverExcluded += playManagedSources.value / "router" / "RoutesPrefix.scala"
wartremoverExcluded += playManagedSources.value / "com" / "originate" / "controllers" / "ReverseRoutes.scala"

/*
 * Linter: http://github.com/HairyFotr/linter
 */

addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % "0.1.17")

scalacOptions += "-P:linter:enable-only:" +
  "AssigningOptionToNull+" +
  "AvoidOptionCollectionSize+" +
  "AvoidOptionMethod+" +
  "AvoidOptionStringSize+" +
  "BigDecimalNumberFormat+" +
  "BigDecimalPrecisionLoss+" +
  "CloseSourceFile+" +
  "ContainsTypeMismatch+" +
  "DecomposingEmptyCollection+" +
  "DivideByOne+" +
  "DivideByZero+" +
  "DuplicateIfBranches+" +
  "DuplicateKeyInMap+" +
  "EmptyStringInterpolator+" +
  "FilterFirstThenSort+" +
  "FloatingPointNumericRange+" +
  "FuncFirstThenMap+" +
  "IdenticalCaseBodies+" +
  "IdenticalCaseConditions+" +
  "IdenticalIfCondition+" +
  "IdenticalIfElseCondition+" +
  "IdenticalStatements+" +
  "IfDoWhile+" +
  "IndexingWithNegativeNumber+" +
  "InefficientUseOfListSize+" +
  "IntDivisionAssignedToFloat+" +
  "InvalidParamToRandomNextInt+" +
  "InvalidStringConversion+" +
  "InvalidStringFormat+" +
  "InvariantCondition+" +
  "InvariantExtrema+" +
  "InvariantReturn+" +
  "JavaConverters+" +
  "LikelyIndexOutOfBounds+" +
  "MalformedSwap+" +
  "MergeNestedIfs+" +
  "ModuloByOne+" +
  "NumberInstanceOf+" +
  "OnceEvaluatedStatementsInBlockReturningFunction+" +
  "OperationAlwaysProducesZero+" +
  "OptionOfOption+" +
  "PassPartialFunctionDirectly+" +
  "PatternMatchConstant+" +
  "PossibleLossOfPrecision+" +
  "PreferIfToBooleanMatch+" +
  "ProducesEmptyCollection+" +
  "ReflexiveAssignment+" +
  "ReflexiveComparison+" +
  "RegexWarning+" +
  "StringMultiplicationByNonPositive+" +
  "SuspiciousMatches+" +
  "SuspiciousPow+" +
  "TransformNotMap+" +
  "TypeToType+" +
  "UndesirableTypeInference+" +
  "UnextendedSealedTrait+" +
  "UnitImplicitOrdering+" +
  "UnlikelyEquality+" +
  "UnlikelyToString+" +
  "UnnecessaryMethodCall+" +
  "UnnecessaryReturn+" +
  "UnnecessaryStringIsEmpty+" +
  "UnnecessaryStringNonEmpty+" +
  "UnsafeAbs+" +
  "UnthrownException+" +
  "UnusedForLoopIteratorValue+" +
  "UseAbsNotSqrtSquare+" +
  "UseCbrt+" +
  "UseConditionDirectly+" +
  "UseContainsNotExistsEquals+" +
  "UseCountNotFilterLength+" +
  "UseExistsNotCountCompare+" +
  "UseExistsNotFilterIsEmpty+" +
  "UseExistsNotFindIsDefined+" +
  "UseExp+" +
  "UseExpm1+" +
  "UseFilterNotFlatMap+" +
  "UseFindNotFilterHead+" +
  "UseFlattenNotFilterOption+" +
  "UseFuncNotFold+" +
  "UseFuncNotReduce+" +
  "UseFuncNotReverse+" +
  "UseGetOrElseNotPatMatch+" +
  "UseGetOrElseOnOption+" +
  "UseHeadNotApply+" +
  "UseHeadOptionNotIf+" +
  "UseHypot+" +
  "UseIfExpression+" +
  "UseInitNotReverseTailReverse+" +
  "UseIsNanNotNanComparison+" +
  "UseIsNanNotSelfComparison+" +
  "UseLastNotApply+" +
  "UseLastNotReverseHead+" +
  "UseLastOptionNotIf+" +
  "UseLog10+" +
  "UseLog1p+" +
  "UseMapNotFlatMap+" +
  "UseMinOrMaxNotSort+" +
  "UseOptionExistsNotPatMatch+" +
  "UseOptionFlatMapNotPatMatch+" +
  "UseOptionFlattenNotPatMatch+" +
  "UseOptionForallNotPatMatch+" +
  "UseOptionForeachNotPatMatch+" +
  "UseOptionGetOrElse+" +
  "UseOptionIsDefinedNotPatMatch+" +
  "UseOptionIsEmptyNotPatMatch+" +
  "UseOptionMapNotPatMatch+" +
  "UseOptionOrNull+" +
  "UseOrElseNotPatMatch+" +
  "UseQuantifierFuncNotFold+" +
  "UseSignum+" +
  "UseSqrt+" +
  "UseTakeRightNotReverseTakeReverse+" +
  "UseUntilNotToMinusOne+" +
  "UseZipWithIndexNotZipIndices+" +
  "VariableAssignedUnusedValue+" +
  "WrapNullWithOption+" +
  "YodaConditions+" +
  "ZeroDivideBy"

// cucumber setup
enablePlugins(CucumberPlugin)
CucumberPlugin.glue := "com/originate/integration/steps"
CucumberPlugin.features := List("test/features")

def beforeAll(): Unit = {
  "./scripts/kill_chromedriver.sh".!
}

CucumberPlugin.beforeAll := beforeAll

watchSources ++= (baseDirectory map { path =>
  (path / "test/features").**("*.feature").get
}).value

// Specify the frontend build as resources for the Assets controller
// When doing reverse routing files for the frontend build are accessed as if they are in the "public" directory
unmanagedResourceDirectories in Assets += baseDirectory.value / "frontend/build"

// docker deployment
enablePlugins(JavaAppPackaging)
enablePlugins(DockerSpotifyClientPlugin)

packageName in Docker := packageName.value
version in Docker := version.value
maintainer in Docker := "Antonio Morales <antonio.morales@originate.com>"
daemonUser in Docker := "daemon"

dockerBaseImage := "relateiq/oracle-java8"
dockerExposedPorts := Seq(9000)

defaultLinuxInstallLocation in Docker := "/opt/docker"

dockerCommands ++= Seq(
  Cmd("ENV", "LANG", "C.UTF-8"),
  Cmd("ENV", "TZ", "/usr/share/zoneinfo/UTC"),
  ExecCmd("ENTRYPOINT", "bin/originate-play", "-Dconfig.file=conf/prod.conf")
)
