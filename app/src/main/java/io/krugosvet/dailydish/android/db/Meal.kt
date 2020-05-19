package io.krugosvet.dailydish.android.db

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import java.util.*

@RealmClass
open class Meal constructor(
  @PrimaryKey var id: Int = UUID.randomUUID().hashCode(),
  @Required var title: String = "",
  @Required var description: String = "",
  @Required var date: Date = Date(),
  @Required var image: String = "",
  @Required var userId: String = ""
) :
  RealmModel
