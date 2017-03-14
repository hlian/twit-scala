package com.originate.persistence

import com.originate.factories.ModelFactories._
import com.originate.models.Example
import com.originate.support.PgCrudDaoBaseSpec

import com.softwaremill.macwire._

class ExampleDaoSpec extends PgCrudDaoBaseSpec[Example]("example") {
  val dao = wire[ExampleDao]
}
