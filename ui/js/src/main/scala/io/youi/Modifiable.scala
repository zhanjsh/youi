package io.youi

import reactify.Var

trait Modifiable {
  lazy val modified: Var[Long] = Var(0L)
}
