package dev.teamuts.shared.error.constant

import org.springframework.http.HttpStatus

interface KtErrorCode {
  val name: String
  val code: String
  val messageKey: String
  val httpStatus: HttpStatus
}
