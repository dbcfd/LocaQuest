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

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import com.sahb.util.Session

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
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
    }
}
