package bootstrap.liftweb

object SessionChecker extends Function2[Map[String, SessionInfo], 
                                        SessionInfo => Unit, Unit] with 
Loggable 
{ 
  def defaultKillWhen = 180000L 
  // how long do we wait to kill single browsers 
  @volatile var killWhen = defaultKillWhen 
  @volatile var killCnt = 1 
  def apply(sessions: Map[String, SessionInfo], 
            destroyer: SessionInfo => Unit): Unit = { 
    val cutoff = millis - 180000L 
    sessions.foreach { 
      case (name, si @ SessionInfo(session, agent, _, cnt, lastAccess)) => 
        if (cnt <= killCnt && lastAccess < cutoff) { 
          logger.info("Purging "+agent) 
          destroyer(si) 
        } 
    } 
  } 
}