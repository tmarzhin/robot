package main

import scala.io.Source


class AnalyserBDD(file: String, toleranceErreur : CorrectionErreur) extends BaseDeDonnees {
  var bdd:Map[String, String] = Map()
  var donnees:Map[String, String] = Map()
   
  var lines = Source.fromFile(file).getLines   
  while(lines.hasNext){
      val tab = lines.next().split("\\s*;\\s*").toList
      // Crée une liste de tuples associant tout les éléments au premier sauf pour le dernier
      val names: List[(String, String)] = tab.take(tab.size - 1) zip List.fill(tab.size-1)(tab.head)
      
      // Associe ces couples dans la Map donnees pour retrouver le nom premier du lieu
      donnees ++= names.toMap
      
      // Crée une liste de tuples associant les n-1 premiers éléments au dernier élément
      val values: List[(String, String)] = tab.take(tab.size - 1) zip List.fill(tab.size-1)(tab.last)
      
      // Associe les noms de lieux à l'adresse
      bdd ++= values.toMap
  }
  
  /**
   * Recupère tous les mots clés de la base de données
   * 
   * @return les mots clés de la BDD
   */
   def getDico() :Set[String] = {
     bdd.keySet
   }
   
   
   /**
   * Recupère le nom principal du lieu
   * 
   * @param e mot clé recherché
   * @return str le nom principal de ce lieu
   * 
   */
  private def getPrimaryName(e:String) : String = {
    donnees.getOrElse(e, "NotFound")
  }
  
  /**
   * Recherche un mot clé dans la base de données et renvoie le lieu et son adresse
   * 
   * @param s le mot clé recherché
   * @return couple du lieu et son adresse
   */
  def searchInDict(s : String): (String, String) = {
    searchInDictRec(s, getDico().toList)
  }
  
  /**
   * Parcours le dictionnaire et cherche une correspondance avec
   * la chaine de caratère s selon une tolérance à l'erreur fourni
   * en paramètre de la classe
   * 
   * @param s, la chaine de caractère
   * @param dict, le dictionnaire
   * @return les couples avec le nom premier du lieu et son adresse
   */
  private def searchInDictRec(s : String, dict: List[String]): (String, String) = {
    if(s.isEmpty()) ("","")
    else {
      dict match {
        case Nil => ("","")
        case e :: end => if (s.contains(toleranceErreur.nomalizeSentence(e))) {
             (getPrimaryName(e), bdd.getOrElse(e,""))
          } else {
              searchInDictRec(s,end)
          }
      }
    }
  }
  
}