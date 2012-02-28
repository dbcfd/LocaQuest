package bootstrap.liftweb

import _root_.net.liftweb.util._
import _root_.net.liftweb.common.{Box, Logger, Logback, Full}
import _root_.net.liftweb.http.LiftRules
import _root_.java.net.URL
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import com.mongodb.casbah.commons.conversions.scala._
import com.mongodb.Mongo
import com.mongodb.{Mongo, MongoOptions, ServerAddress}
import net.liftweb.mongodb.{DefaultMongoIdentifier, MongoDB}
import net.liftweb.mapper.{DB, DefaultConnectionIdentifier}

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import com.sahb.foursquare.FoursquareHttp
import com.sahb.rest.Rest
import com.sahb.util.{Session, Logging}

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot extends Logging {
    DeregisterJodaTimeConversionHelpers()

    def pageRequiresLogin(req: Req): Boolean = {
        req.path match {
            case ParsePath(List("game"),_,_,_) => true
            case _ => false
        }
    }

    def isFoursquareOauth(req: Req): Boolean = {
        req.path match {
            case ParsePath(List("foursquare", "callback"),_,_,_) => true
            case _ => false
        }
    }

    def boot {
        val logUrl: Box[java.net.URL] = LiftRules.getResource({
            Props.get("logback.configuration") match {
                case Full(filename) => "/props/" + filename
                case _ => "/props/logback.xml"
            }
        })

        logUrl foreach ((url: URL) => (Logger.setup = Full(Logback.withFile(url))))

        val context: LoggerContext = LoggerFactory.getILoggerFactory().asInstanceOf[LoggerContext]

        val configurator: JoranConfigurator = new JoranConfigurator()
        configurator.setContext(context);
        context.reset();
        logUrl foreach ((url: URL) => (configurator.doConfigure(url)))


        StatusPrinter.print(context)

        /**
        val serverAddress:ServerAddress =
      new ServerAddress(LQPlayersDB.ipAddress, LQPlayersDB.port )
	val mongoObj:Mongo = new Mongo(serverAddress, new MongoOptions)
   MongoDB.defineDb(LQPlayersDB, mongoObj, LQPlayersDB.databaseId)
	MongoDB.defineDb(LQVenuesDB, mongoObj, LQVenuesDB.databaseId)
	MongoDB.defineDb(LQMonstersDB, mongoObj, LQMonstersDB.databaseId)
	MongoDB.defineDb(LQItemsDB, mongoObj, LQItemsDB.databaseId)
   MongoDB.defineDb(LQMarketDB, mongoObj, LQMarketDB.databaseId)
         **/

        // where to search snippet
        LiftRules.addToPackages("com.sahb")
        //Schemifier.schemify(true, Schemifier.infoF _, User)

        // Build SiteMap
        def sitemap(): SiteMap = SiteMap(
            Menu.i("Home") / "index",
            Menu.i("Game") / "game"
        )

        LiftRules.setSiteMapFunc(() => sitemap)

        /*
        * Show the spinny image when an Ajax call starts
        */
        LiftRules.ajaxStart =
            Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

        /*
        * Make the spinny image go away when it ends
        */
        LiftRules.ajaxEnd =
            Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

        LiftRules.early.append(makeUtf8)

        LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

        LiftRules.useXhtmlMimeType = false

        LiftRules.dispatch.append(Rest); //stateful
        LiftRules.statelessDispatchTable.append(Rest); //stateless, no session
        LiftRules.dispatch.append {
            case r: Req if (isFoursquareOauth(r)) => ({
                r.param("code") match {
                    case Full(code) => ( {
                        debug("received oauth code, redirecting to game")
                        Session.logIn(code)
                        () => Full(new RedirectResponse("/game", r))
                    } )
                    case _ => ( {
                        error("Failed to get oauth code from foursquare")
                        () => Full(new RedirectResponse("/index", r))
                    })
                }
            })
            case r: Req if (pageRequiresLogin(r) && !Session.isLoggedIn()) => ({
                debug("Session is " + Session.get)
                () => FoursquareHttp.performRedirect(r);
            })
        }

        ResourceServer.allow {
            case "images" :: _ => true
            case "js" :: _ => true
            case "css" :: _ => true
            case "js.min" :: _ => true
        }
    }

    /**
     * Force the request to be UTF-8
     */
    private def makeUtf8(req: HTTPRequest) {
        req.setCharacterEncoding("UTF-8")
    }
}
