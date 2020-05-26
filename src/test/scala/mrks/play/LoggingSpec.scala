package mrks.play

import mrks.BaseSpec
import org.scalamock.scalatest.MockFactory
import play.api.Logger


class LoggingSpec extends BaseSpec with MockFactory with Logging {
  private val exception = new Exception("error")
  private val fakeLogger = mock[org.slf4j.Logger]
  override protected val logger = new Logger(fakeLogger)

  "log" should {
    "log message using matching log level" in {
      (fakeLogger.isErrorEnabled: () => Boolean).expects().returns(true).twice()
      (fakeLogger.error(_: String)).expects("e1")
      (fakeLogger.error(_: String, _: Throwable)).expects("e2", exception)

      (fakeLogger.isWarnEnabled: () => Boolean).expects().returns(true).twice()
      (fakeLogger.warn(_: String)).expects("w1")
      (fakeLogger.warn(_: String, _: Throwable)).expects("w2", exception)

      (fakeLogger.isInfoEnabled: () => Boolean).expects().returns(true).twice()
      (fakeLogger.info(_: String)).expects("i1")
      (fakeLogger.info(_: String, _: Throwable)).expects("i2", exception)

      (fakeLogger.isDebugEnabled: () => Boolean).expects().returns(true).twice()
      (fakeLogger.debug(_: String)).expects("d1")
      (fakeLogger.debug(_: String, _: Throwable)).expects("d2", exception)

      (fakeLogger.isTraceEnabled: () => Boolean).expects().returns(true).twice()
      (fakeLogger.trace(_: String)).expects("t1")
      (fakeLogger.trace(_: String, _: Throwable)).expects("t2", exception)

      logger.log(Log.Error("e1"))
      logger.log(Log.Error("e2", exception))
      logger.log(Log.Warning("w1"))
      logger.log(Log.Warning("w2", exception))
      logger.log(Log.Info("i1"))
      logger.log(Log.Info("i2", exception))
      logger.log(Log.Debug("d1"))
      logger.log(Log.Debug("d2", exception))
      logger.log(Log.Trace("t1"))
      logger.log(Log.Trace("t2", exception))
    }
  }
}
