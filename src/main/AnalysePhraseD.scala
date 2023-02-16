package main

case class Courtesy(isPresent: Boolean,without: String)

class AnalysePhraseD(bdd : BaseDeDonnees, toleranceErreur: CorrectionErreur) extends AnalysePhrase {
  val dict : Set[String] = bdd.getDico();
  
  /**
   * Retourne une liste de réponses à une requête
   *  - Analyse pour trouver une forme de politesse
   *  - Corrige la phrase avec la tolérance à l'erreur
   *  - Analyse la phrase pour trouver une ou plusieurs réponses
   *  
   *  @param cle, la requête
   *  @return la liste de réponses avec une politesse si trouvé
   */
  def analyse(quest:String):Set[(String,String)] = {
    val courtesy: Courtesy = this.politesse(quest)
    if (courtesy.isPresent && courtesy.without.isEmpty) Set(("bulle","Bonjour"))
    else if (!courtesy.isPresent && courtesy.without.isEmpty()) Set(("bulle","Je ne comprends pas votre demande"))
    else {
      val correctedStr: String = toleranceErreur.corrige(courtesy.without, dict)
      val analyse: List[(String,String)] = rawAnalyse(correctedStr)
      val courtesyPart = if (courtesy.isPresent) Set(("bulle","bonjour")) else Set()
      
      val answers = if (!analyse.isEmpty) analyse else Set(("bulle","Je ne comprends pas votre demande"))
      
      courtesyPart ++ answers

    }
  }
  
  /**
   * Analyse une phrase pour trouver une formule de politesse
   *  - Corrige la phrase selon un dictionnaire de politesse courante
   *  - Trouve les politesses dans la phrase et les retirer
   *  
   *  @param s, la phrase à analyser
   *  @return un objet Courtesy, avec un boolean si la phrase contenait une politesse et la phrase sans 
   */
  def politesse(s: String): Courtesy = {
    val corrected: String = toleranceErreur.corrige(s, Set("bonjour","bonsoir","salut"))
    val salutation = "bonjour|bonsoir|salut".r
    Courtesy(salutation.findFirstIn(corrected) != None,
        salutation.replaceAllIn(corrected, "").trim)
  }
  
  /**
   * Prend une String et analyse le contenu à l'aide du dictionnaire
   * Retourne les couples (destination, adresse)
   * 	- Decoupe la String en fonction de "et", de "ou" et de ","
   * 	- Analyse chaque partie decoupée pour trouver une correspondance
   * 		dans la base de données
   * 
   * @param s, la string à analyser
   * @return les couples avec le nom du lieu et son adresse
   */
  def rawAnalyse(s : String): List[(String,String)] = {
    val lWords: List[String] = s.split(",| ou | et ").toList
    getAdress(lWords)
  }

  /**
   * Recherche dans la base de données l'adresse des lieux inscrits dans la liste
   * Retire les réponses qui ne correspondent à rien
   * 
   * @param l, la liste des lieux
   * @return le couple (lieux, adresse)
   */
  def getAdress(l : List[String]): List[(String,String)] = {
    l match {
      case Nil => Nil
      case e :: end => val r : (String,String) = bdd.searchInDict(e.trim);
        if (r._2.isEmpty) getAdress(end) 
        else r :: getAdress(end)
    }
  }
}