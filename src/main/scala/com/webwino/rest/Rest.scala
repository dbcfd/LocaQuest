package com.sahb
package rest

import net.liftweb.http._
import net.liftweb.http.rest.RestHelper
import net.liftweb.json._
import net.liftweb.common.{Box, Empty, Full}

object Rest extends RestHelper {
    serve{
    case req @ Req("api" :: "player" :: _, "json", GetRequest) => (
        req.param("nickname") match {
            case Full(player) => JBool(true)
            case _ => ( () => Full(BadResponse()) )
        }
    )
    case Req("api" :: "player" :: "items" :: id, "json", PutRequest) => (JBool(true))
    case Req("api" :: "player" :: "chests" :: id, "json", PutRequest) => (JBool(true))
    case Req("api" :: "player" :: "items" :: id, "json", DeleteRequest) => (JBool(true))
    case Req("api" :: "player" :: "chests" :: id, "json", DeleteRequest) => (JBool(true))
    case Req("api" :: "player" :: _, "json", PostRequest) => (JBool(true))
    case Req("api" :: "player" :: _, "json", PutRequest) => (JBool(true))
    case req @ Req("api" :: "monster" :: _, "json", GetRequest) => (
        req.param("name") match {
            case Full(monster) => JBool(true)
            case _ => ( () => Full(BadResponse()) )
        }
        )
    case Req("api" :: "monster" :: _, "json", PostRequest) => (JBool(true))
    case Req("api" :: "monster" :: _, "json", PutRequest) => (JBool(true))
    case req @ Req("api" :: "venue" :: venueId :: "monsters" :: monsterId, "json", GetRequest) => (
        req.param("name") match {
            case Full(venue) => JBool(true)
            case _ => ( () => Full(BadResponse()) )
        }
        )
    case Req("api" :: "venue" :: _, "json", PostRequest) => (JBool(true))
    case Req("api" :: "venue" :: _, "json", PutRequest) => (JBool(true))
    case Req("api" :: "venue" :: "monster" :: _, "json", DeleteRequest) => (JBool(true))
    case Req("api" :: "venue" :: "item" :: _, "json", DeleteRequest) => (JBool(true))
    }
}
