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

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
   DeregisterJodaTimeConversionHelpers()
   
	def pageRequiresLogin(req: Req): Boolean = {
      req.path match {
         case ParsePath(List("index", _),_,_,_) => false
         case _ => true
      }
	}
	
   def boot {
      val logUrl: Box[java.net.URL] = LiftRules.getResource( {
         Props.get("logback.configuration") match {
            case Full(filename) => "/props/" + filename
            case _ => "/props/logback.xml"
         }
      } )
      
      logUrl foreach ((url: URL) => (Logger.setup = Full(Logback.withFile(url))))

      val context : LoggerContext = LoggerFactory.getILoggerFactory().asInstanceOf[LoggerContext]

		val configurator : JoranConfigurator = new JoranConfigurator()
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
      def sitemap():SiteMap = SiteMap(
         Menu.i("Home") / "index",
         Menu.i("Play") / "game" / "index"
         Menu.i("FoursquareCallback") / "foursquare" / "callback" >> Hidden,
         Menu.i("FoursquareRedirect") / "foursquare" / "redirectToFoursquare" >> Hidden
      )

      LiftRules.setSiteMapFunc(() => User.sitemapMutator(sitemap))

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
	
      LiftRules.htmlProperties.default.set((r: Req) =>new Html5Properties(r.userAgent))
	
      LiftRules.useXhtmlMimeType = false
   
      LiftRules.dispatch.append { 
         case r:Req if ( pageRequiresLogin(r) ) => ( {
            FoursquareHttp.performRedirect();
         } )
   }
	
	ResourceServer.allow {
		case "images" :: _ => true
		case "js" :: _ => true
		case "css" :: _ => true
	}
   
   TreeView.init
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }
}
