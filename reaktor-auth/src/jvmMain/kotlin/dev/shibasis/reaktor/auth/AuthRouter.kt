package dev.shibasis.reaktor.auth

import dev.shibasis.reaktor.auth.api.SignInRequest
import dev.shibasis.reaktor.auth.api.SignInResponse
import dev.shibasis.reaktor.auth.apps.jsonResponse
import dev.shibasis.reaktor.auth.jwt.TokenVerifierService
import dev.shibasis.reaktor.auth.repositories.AppRepository
import dev.shibasis.reaktor.auth.repositories.UserRepository
import dev.shibasis.reaktor.auth.services.LoginService
import io.netty.handler.codec.http.HttpResponseStatus
import org.jetbrains.exposed.sql.Database
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.coRouter

fun authRouter(
    database: Database,
    clientId: String
) = coRouter {
    val loginService = LoginService(database, clientId)

    POST("/sign-in") { request ->
        val body = request.awaitBody<SignInRequest>()

        val response = loginService.login(body)
        val status = when(response) {
            is SignInResponse.Failure.RequiresSignUp -> HttpStatus.FOUND
            is SignInResponse.Failure -> HttpStatus.BAD_REQUEST
            else -> HttpStatus.OK
        }

        jsonResponse(response, status)
    }
}

