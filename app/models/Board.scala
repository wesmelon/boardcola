package models

import java.util.UUID
import java.sql.Timestamp

case class Board(
  id: Option[Long], 
  uid: Option[UUID], 
  cid: Option[Long], 
  name: String, 
  creationTime: Option[Timestamp], 
  lastModified: Option[Timestamp])
