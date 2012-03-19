package com
package sahb
package snippet

import net.liftweb._
import net.liftweb.json._
import net.liftweb.util.BindHelpers
import http._
import js._
import JsCmds._
import JE._
import scala.xml._
import net.liftweb.common.{Box, Full, Empty}

import com.sahb.util.{Logging, Session}
import com.sahb.foursquare.FoursquareHttp

case class FQUR_User(id:String)
case class FQUR_Response(user:FQUR_User)
case class FQUR(response: FQUR_Response)

class GameSnippet extends Logging with BindHelpers{
    implicit val formats = DefaultFormats

	def render = {
        //grab our information from foursquare
        val accessToken:String = FoursquareHttp.getAccessToken(Session.get.openTheBox)
        val jsonResponse:JValue = FoursquareHttp.getUserInformation(accessToken)
        debug("user information=" + jsonResponse.toString())
        val userId:String = jsonResponse.extract[FQUR].response.user.id
        Text(userId)
    }
}
