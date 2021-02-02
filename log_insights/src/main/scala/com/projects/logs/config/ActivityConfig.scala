package com.projects.logs.config

import com.projects.logs.util.CLIConfig

class ActivityConfig(val file:List[String] , val date:String)

object ActivityConfig {
  // object apply method applies any logic to supply values to class variables,
  // If direct values needed to be passed, then simply form case class and no need of companion object as values are assigned directly to variables.
  // apply method can be overloaded
  def apply(config:CLIConfig, del:String=","): ActivityConfig = {
    val splitfiles=config.file.split(del).toList
    new ActivityConfig(splitfiles,config.date)
  }
}
