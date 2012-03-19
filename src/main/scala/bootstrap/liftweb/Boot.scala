package bootstrap.liftweb

import net.liftweb.util._
import net.liftweb.common. {Box, Logger, Logback, Full}
import net.liftweb.http.LiftRules
import java.net.URL
import net.liftweb.http._
import net.liftweb.http.provider._
import net.liftweb.sitemap._
import net.liftweb.sitemap.Loc._
import Helpers._
<<<<<<< HEAD
=======
import com.mongodb.casbah.commons.conversions.scala._
import com.mongodb.Mongo
import com.mongodb.{Mongo, MongoOptions, ServerAddress}
import net.liftweb.mongodb.{DefaultMongoIdentifier, MongoDB}
import net.liftweb.mapper.{DB, DefaultConnectionIdentifier}
>>>>>>> 935170e7f830af268d7e43e24fcf559764651270

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

<<<<<<< HEAD
import com.sahb.util.Session
=======
import com.sahb.foursquare.FoursquareHttp
import com.sahb.rest.Rest
import com.sahb.util.{Session, Logging}
>>>>>>> 935170e7f830af268d7e43e24fcf559764651270

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
<<<<<<< HEAD
class Boot {
    def pageRequiresLogin ( req: Req ) : Boolean = {
        req.path match {
            case ParsePath ( List ( "index", _ ), _, _, _ ) => false
            case _ => true
        }
    }
    
    def boot {
        val logUrl: Box[java.net.URL] = LiftRules.getResource ( {
            Props.get ( "logback.configuration" ) match {
                case Full ( filename ) => "/props/" + filename
                case _ => "/props/logback.xml"
            }
        } )
        
        logUrl foreach ( ( url: URL ) => ( Logger.setup = Full ( Logback.withFile ( url ) ) ) )
        
        val context : LoggerContext = LoggerFactory.getILoggerFactory().asInstanceOf[LoggerContext]
        
        val configurator : JoranConfigurator = new JoranConfigurator()
        configurator.setContext ( context );
        context.reset();
        logUrl foreach ( ( url: URL ) => ( configurator.doConfigure ( url ) ) )
        
        
        StatusPrinter.print ( context )

        // where to search snippet
        LiftRules.addToPackages ( "com.webwino" )
        //Schemifier.schemify(true, Schemifier.infoF _, User)
        
        // Build SiteMap
        def sitemap() : SiteMap = SiteMap (
            Menu.i ( "Home" ) / "index",
            Menu.i ( "Play" ) / "game"
        )
        
        LiftRules.setSiteMapFunc ( () => sitemap )
        
        /*
         * Show the spinny image when an Ajax call starts
         */
        LiftRules.ajaxStart =
        Full ( () => LiftRules.jsArtifacts.show ( "ajax-loader" ).cmd )
        
        LiftRules.early.append ( makeUtf8 )
        
        LiftRules.htmlProperties.default.set ( ( r: Req ) => new Html5Properties ( r.userAgent ) )
        
        LiftRules.useXhtmlMimeType = false
        
        /**
        LiftRules.dispatch.append {
            case r: Req if ( pageRequiresLogin ( r ) && !Session.isLoggedIn ) => ( {
                FoursquareHttp.performRedirect();
            } )
        }
        **/
        
        ResourceServer.allow {
            case "images" :: _ => true
            case "js" :: _ => true
            case "js.min" :: _ => true
            case "css" :: _ => true
        }
    }
    
    /**
     * Force the request to be UTF-8
     */
    private def makeUtf8 ( req: HTTPRequest ) {
        req.setCharacterEncoding ( "UTF-8" )
=======
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
>>>>>>> 935170e7f830af268d7e43e24fcf559764651270
    }
}
