package machine

import main.BaseDeDonnees
import main.AnalyserBDD
import main.AnalysePhrase
import main.AnalysePhraseD
import main.MainApp
import main.ToleranceErreur


object MachineImpl extends MachineDialogue{
  
  val bdd : BaseDeDonnees = new AnalyserBDD("doc/DonneesInitiales.txt", ToleranceErreur)
  val annalysePhrase : AnalysePhrase = new AnalysePhraseD(bdd, ToleranceErreur)
  
  // Pour la partie test par le client
  def reinit= ()
  
  def test(l:List[String]):List[String]= {
    l match {
      case Nil => Nil
      case s :: end => ask(s) ++ test(end)
    }
  }
  
  def ask(s: String): List[String] = {
    MainApp.response(s)
  }
}