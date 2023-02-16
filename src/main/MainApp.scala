package main

import scala.swing._
import scala.swing.MainFrame

object MainApp extends SimpleSwingApplication {
  
  def top : Interface = new GUI(response)

  var bdd : BaseDeDonnees = new AnalyserBDD("doc/DonneesInitiales.txt", ToleranceErreur)
  var annalysePhrase : AnalysePhrase = new AnalysePhraseD(bdd, ToleranceErreur)
  
  /**
   * Retourne la réponse à une phrase
   */
  def response(s: String): List[String] = {
    getAnswers(annalysePhrase.analyse(s).toList)
  }
  
  /**
   * Crée des reponses à afficher à partir de couples (lieu, adresse) 
   * 
   * @param une liste de (lieu, adresse)
   * @return une liste de réponses formulés "L'adresse de [lieu] est : [adresse]"
   */
  def getAnswers(l : List[(String,String)]): List[String] = {
    // Collecte les élements si verifie et leurs applique une transformation
    l.collect({
      case ("bulle",text) => text
      case (name, adress) if (!adress.isEmpty)=> s"L'adresse de $name est : $adress"}
    ) 
  }
  
  /**
   * Change la base de données utilisée par l'application
   * Utilisé pour des tests
   */
  def changeBDD(file: String) {
    bdd = new AnalyserBDD(file, ToleranceErreur)
    annalysePhrase = new AnalysePhraseD(bdd, ToleranceErreur)
  }
}