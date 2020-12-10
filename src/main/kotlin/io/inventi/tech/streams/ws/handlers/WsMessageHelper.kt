package io.inventi.tech.streams.ws.handlers

import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession

abstract class WsMessageHelper {

    abstract val exchange: String

    abstract fun subscribeToTicker(session: WebSocketSession)

    abstract fun onMessageReceived(session: WebSocketSession, message: WebSocketMessage<*>)

}