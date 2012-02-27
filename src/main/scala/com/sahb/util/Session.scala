package com.sahb
package util

import net.liftweb.common. {Box, Empty, Full}

object Session extends SessionVar[Box[String]] ( Empty ) {

    def set ( id: String ) = {
        set ( Full ( id ) )
    }
    
    def isLoggedIn() : Boolean = is.isDefined
}
