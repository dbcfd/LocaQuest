package bootstrap.liftweb

object SessionInfoDumper extends LiftActor with Loggable { 
  private var lastTime = millis 
  private def cyclePeriod = 1 minute 
  import net.liftweb.example.lib.SessionChecker 
  protected def messageHandler = 
    { 
      case SessionWatcherInfo(sessions) => 
        if ((millis - cyclePeriod) > lastTime) { 
          lastTime = millis 
          val rt = Runtime.getRuntime 
          rt.gc 
          RuntimeStats.lastUpdate = timeNow 
          RuntimeStats.totalMem = rt.totalMemory 
          RuntimeStats.freeMem = rt.freeMemory 
          RuntimeStats.sessions = sessions.size 
          val percent = (RuntimeStats.freeMem * 100L) / RuntimeStats.totalMem 
          // get more aggressive about purging if we're 
          // at less than 35% free memory 
          if (percent < 35L) { 
            SessionChecker.killWhen /= 2L 
    if (SessionChecker.killWhen < 5000L) 
      SessionChecker.killWhen = 5000L 
            SessionChecker.killCnt *= 2 
          } else { 
            SessionChecker.killWhen *= 2L 
    if (SessionChecker.killWhen > 
                SessionChecker.defaultKillWhen) 
     SessionChecker.killWhen = SessionChecker.defaultKillWhen 
            val newKillCnt = SessionChecker.killCnt / 2 
    if (newKillCnt > 0) SessionChecker.killCnt = newKillCnt 
          } 
          val dateStr: String = timeNow.toString 
          logger.info("[MEMDEBUG] At " + dateStr + " Number of open 
sessions: " + sessions.size) 
          logger.info("[MEMDEBUG] Free Memory: " + 
pretty(RuntimeStats.freeMem)) 
          logger.info("[MEMDEBUG] Total Memory: " + 
pretty(RuntimeStats.totalMem)) 
          logger.info("[MEMDEBUG] Kill Interval: " + 
(SessionChecker.killWhen / 1000L)) 
          logger.info("[MEMDEBUG] Kill Count: " + (SessionChecker.killCnt)) 
        } 
    } 
  private def pretty(in: Long): String = 
    if (in > 1000L) pretty(in / 1000L) + "," + (in % 1000L) 
    else in.toString 
}