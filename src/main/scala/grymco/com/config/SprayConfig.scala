package grymco.com.config

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by Roy on 28/02/2016.
  */
object SprayConfig {
  private val config:Config = ConfigFactory.load()
  val sprayConfig:SprayConfig = new SprayConfig(config)

  def init() = {}
}

class SprayConfig(config:Config) {
  val host = config.getString("grymco.com.http.host")
  val port = config.getInt("grymco.com.http.port")

  def this() {
    this(ConfigFactory.load())
  }
}
