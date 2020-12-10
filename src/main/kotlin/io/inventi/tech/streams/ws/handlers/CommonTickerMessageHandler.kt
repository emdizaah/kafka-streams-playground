package io.inventi.tech.streams.ws.handlers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.AbstractWebSocketHandler

abstract class CommonTickerMessageHandler(private val wsMessageHelper: WsMessageHelper) : AbstractWebSocketHandler() {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(CommonTickerMessageHandler::class.java)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.debug("Connection to ${wsMessageHelper.exchange} established successfully!")
        wsMessageHelper.subscribeToTicker(session)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        logger.debug("Got message from ${wsMessageHelper.exchange}: ${message.payload}")
        wsMessageHelper.onMessageReceived(session, message)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error("Failed to read message from ${wsMessageHelper.exchange}", exception)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.warn("Connection to ${wsMessageHelper.exchange} was closed")
    }

}