package com.ss.service

object ParallelStructuredStreamsDriver {


  def main(args: Array[String]): Unit = {

    new EmployeeDeptEnrichmentService().filterEmployeeDeptAndEnrich
  }

}
