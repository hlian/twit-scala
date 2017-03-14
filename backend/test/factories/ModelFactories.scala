package com.originate.factories

import com.originate.models.Example

import org.joda.time.DateTime

/**
 * Place factories here as implicit objects so that CrudDaoBaseSpec can automagically
 * look them up by type
 */
object ModelFactories {
  implicit object ExampleFactory extends Factory[Example] {
    def newInstance: Example = Example("name", DateTime.now)

    def alter(example: Example): Example = example.copy(name = "other name")
  }
}
