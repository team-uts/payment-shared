package dev.teamuts.shared.data.constant

interface KtLockGroup {
  val name: String

  fun getLockTtl(): LockTtl
}
