package com.sahb
package rest

object Rest extends RestHelper {

    serve(
        "foursquare" prefix {
            case "callback"  => (
                Console.println("received code " + S.params("code")
            )
        }
        , "api" / "player" prefix {
        
        }
        , "api" / "venue" prefix {
        
        }
    )
}
