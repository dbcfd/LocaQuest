package com.webwino.snippet

import net.liftweb._
import net.liftweb.common. {Box, Logger, Logback, Full}
import net.liftweb.util._
import scala.xml._

class SignIn extends BindHelpers {
    def clientId:String = {
      Props.get("foursquare.client.id") match {
         case Full(id) => id
         case _ => new String("CW14C1TQVGHFR2CMXWWLJPFUSZNWE00P52BZAC2WPGFPGEK4")
      }
    }
    def clientSecret:String = {
      Props.get("foursquare.client.secret") match {
         case Full(secret) => secret
         case _ => new String("R2NSPELUTSQVMVP5OSHAZ4O11KVFZ3LAAO1W11FWRERHLG5Q")
      }
    }
        def redirectUri:String = {
      Props.get("foursquare.callback") match {
         case Full(url) => Helpers.urlEncode("http://" + url + "/foursquare/callback")
         case _ => Helpers.urlEncode("http://localhost:8081/foursquare/callback")
      }
   }
        def version:String = "&v=20111115"
    def displayLink(xhtml:NodeSeq):NodeSeq = {
        val callbackUri = "https://foursquare.com/oauth2/authenticate?client_id=" + clientId + "&response_type=token&redirect_uri=" + redirectUri
        <a href={callbackUri}>Login with Foursquare</a>
    }
}
