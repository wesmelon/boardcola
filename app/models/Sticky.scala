package models

import java.sql.Timestamp

case class Sticky(
  id: Option[Long], 
  bid: Long, 
  name: Option[String], 
  content: String, 
  xPos: Option[Int], 
  yPos: Option[Int], 
  creationTime: Option[Timestamp], 
  lastModified: Option[Timestamp])
