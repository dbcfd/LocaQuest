package com.sahb
package util

import net.liftweb.common.{Logger, Logback}

object GlobalLogger {
   val logInstance : Logger = Logger("LocaQuest")
}

trait Logging {
	def debug(s:String) {
      GlobalLogger.logInstance.debug(s)
   }
   
   def info(s:String) {
      GlobalLogger.logInstance.info(s)
   }
   
   def error(s:String) {
      GlobalLogger.logInstance.error(s)
   }
}
