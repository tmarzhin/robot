package main
import scala.swing.MainFrame

trait BaseDeDonnees {
  def getDico() :Set[String]
  def searchInDict(s : String): (String, String)
}
trait CorrectionErreur{
  def corrige(cle:String, dict:Set[String]):String
  def nomalizeSentence(s: String): String
  
}
trait AnalysePhrase{
 def analyse(quest:String):Set[(String,String)]
}

trait Interface extends MainFrame {
 def addBulle(resp:String, AI:Boolean):Unit
}