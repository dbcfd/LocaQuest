package com.sahb
package foursquare

import net.liftweb.util._
import net.liftweb.http.{S, LiftResponse, RedirectResponse, Req}
import org.apache.commons.httpclient._
import org.apache.commons.httpclient.methods._
import net.liftweb.json._
import xml._

import net.liftweb.common.{Box, Full, Empty}

case class StringAccessor(val string: String)

object FoursquareHttp {
    implicit val formats = DefaultFormats
    val client = new HttpClient()

    def clientId: String = {
        Props.get("foursquare.client.id") match {
            case Full(id) => id
            case _ => new String("CW14C1TQVGHFR2CMXWWLJPFUSZNWE00P52BZAC2WPGFPGEK4")
        }
    }

    def clientSecret: String = {
        Props.get("foursquare.client.secret") match {
            case Full(secret) => secret
            case _ => new String("R2NSPELUTSQVMVP5OSHAZ4O11KVFZ3LAAO1W11FWRERHLG5Q")
        }
    }

    def baseUrl: String = "https://api.foursquare.com/v2/"

    def clientParams: String = "client_id=" + clientId + "&client_secret=" + clientSecret

    def redirectUri: String = {
        Props.get("foursquare.callback") match {
            case Full(url) => Helpers.urlEncode("http://" + url + "/foursquare/callback")
            case _ => Helpers.urlEncode("http://localhost:8080/foursquare/callback")
        }
    }

    def version: String = "&v=20111115"

    def postAndParseURL(url: String): JValue = {
        val method = new PostMethod(url)
        client.executeMethod(method)
        //lqLogger.debug("Posting url " + url + " with response=" + method.getResponseBodyAsString())
        parse(method.getResponseBodyAsString())
    }

    def postAndParseQuery(query: String): JValue = postAndParseURL(baseUrl + query + version)

    def fetchAndParseURL(url: String): JValue = {
        val method = new GetMethod(url)
        client.executeMethod(method)
        //lqLogger.debug("Querying url " + url + " with response=" + method.getResponseBodyAsString())
        parse(method.getResponseBodyAsString())
    }

    def fetchAndParseQuery(query: String): JValue = fetchAndParseURL(baseUrl + query + version)

    def performRedirect(req:Req):Box[LiftResponse] = {
        val fqRedirect = "https://foursquare.com/oauth2/authenticate?client_id=" + clientId + "&response_type=code&redirect_uri=" + redirectUri
        //lqLogger.debug("redirecting to " + fqRedirect)
        return Full(new RedirectResponse(fqRedirect, req));
    }

    def getAccessToken(oauth: String): String = {
        val tokenUrl = "https://foursquare.com/oauth2/access_token?" + clientParams +
            "&grant_type=authorization_code&redirect_uri=" + redirectUri + "&code=" + oauth
        val method = new GetMethod(tokenUrl)
        client.executeMethod(method)
        val response = method.getResponseBodyAsString()
        //lqLogger.debug("Getting access token from " + tokenUrl + " with response=" + response)
        //json parse
        parse(response).extract[Map[String, String]].getOrElse("access_token", "")
    }

    def getUserInformation(accessToken: String): JValue = {
        val userUrl = "https://api.foursquare.com/v2/users/self?oauth_token=" + accessToken + version
        val method = new GetMethod(userUrl)
        client.executeMethod(method)
        val response = method.getResponseBodyAsString()
        //lqLogger.debug("Getting user from " + userUrl + " with response=" + response)
        parse(response)
    }
}
		
