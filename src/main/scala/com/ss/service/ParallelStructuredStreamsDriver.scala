package com.ss.service

import main.scala.com.ss.service.IndependentGenericClass

object ParallelStructuredStreamsDriver {


  def main (args: Array[String]): Unit = {

    new EmployeeDeptEnrichmentService().filterEmployeeDeptAndEnrich
    new IndependentGenericClass().genericMethod1()
  }

}
