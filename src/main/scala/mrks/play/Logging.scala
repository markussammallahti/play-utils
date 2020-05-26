package mrks.play

import play.api.{Logger, MarkerContext}


trait Logging extends play.api.Logging {
  object Log {
    sealed trait Message {
      val message: String
      val cause  : Option[Throwable]
    }

    case class Trace(message: String, cause: Option[Throwable] = None) extends Message

    object Trace {
      def apply(message: String): Trace = Trace(message, None)
      def apply(message: String, cause: Throwable): Trace = Trace(message, Some(cause))
    }

    case class Debug(message: String, cause: Option[Throwable] = None) extends Message

    object Debug {
      def apply(message: String): Debug = Debug(message, None)
      def apply(message: String, cause: Throwable): Debug = Debug(message, Some(cause))
    }

    case class Info(message: String, cause: Option[Throwable] = None) extends Message
    object Info {
      def apply(message: String): Info = Info(message, None)
      def apply(message: String, cause: Throwable): Info = Info(message, Some(cause))
    }

    case class Warning(message: String, cause: Option[Throwable] = None) extends Message

    object Warning {
      def apply(message: String): Warning = Warning(message, None)
      def apply(message: String, cause: Throwable): Warning = Warning(message, Some(cause))
    }

    case class Error(message: String, cause: Option[Throwable]) extends Message

    object Error {
      def apply(message: String): Error = Error(message, None)
      def apply(message: String, cause: Throwable): Error = Error(message, Some(cause))
    }
  }

  implicit class LoggerExtensions(l: Logger) {
    def log(message: Log.Message)(implicit mc: MarkerContext): Unit = {
      message match {
        case Log.Trace(message, Some(cause))   => l.trace(message, cause)
        case Log.Trace(message, _)             => l.trace(message)
        case Log.Debug(message, Some(cause))   => l.debug(message, cause)
        case Log.Debug(message, _)             => l.debug(message)
        case Log.Info(message, Some(cause))    => l.info(message, cause)
        case Log.Info(message, _)              => l.info(message)
        case Log.Warning(message, Some(cause)) => l.warn(message, cause)
        case Log.Warning(message, _)           => l.warn(message)
        case Log.Error(message, Some(cause))   => l.error(message, cause)
        case Log.Error(message, _)             => l.error(message)
      }
    }
  }
}
