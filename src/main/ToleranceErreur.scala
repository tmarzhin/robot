package main

import java.text.Normalizer
import java.util.regex.Pattern;


object ToleranceErreur extends CorrectionErreur {

   /**
   * Corrige un mot avec une tolérance de 
   * 1 caractère de différence ou 1 caractère en plus ou 1 caractère en moins
   * 
   * @param prend un string
   * @param dict, un dictionnaire de mot
   * @return le mot corrigé si besoin et s'il y a au plus une erreur
   */
  def corrige(s:String, dict:Set[String]):String = {
    val normalizedStr : String = this.nomalizeSentence(s) // Normalize la chaine pour corriger
    val quest: String = "\\A\\W+".r.replaceFirstIn(normalizedStr, "") // Assure que le début de la chaine soit un mot (pour la séparation)

    val nDict: Set[String] = dict.map((s: String) => nomalizeSentence(s)) // Normalise les termes du dictionnaire
    val words : List[String] = quest.split("\\W+").toList // Met les mots dans une liste
    
    if (words.size == 0) ""
    else if (words.size == 1) correctWord(words.head, nDict)
    else {
      val separator : List[String] = quest.split("\\w+").toList // Recupère les séparateurs entre chaque mot dans une liste
      val correctWords : List[String] = words.map(s => correctWord(s, nDict)) // Corrige les mots
      
      // Reconstruit la chaine de caractères avec les mots corrigés
      (separator zip correctWords).flatten({ case (a,b) => List(a,b) }).mkString
    }
  }
  
  /**
   * Corrige un mot a partir d'un dictionnaire
   * 
   * @param s, ce mot
   * @param dict, le dictionnaire
   */
  def correctWord(s : String, dict:Set[String]): String = {
    val nS: String = nomalizeSentence(s)
    
    // Sépare les expressions du dictionaire en mot pour chercher à corriger la phrase
    dict.flatMap(_.split("\\W+")).find(
        cle => { distLevenshtein(cle, cle.length, nS, nS.length)<=1 }
    ) match {
      case None => s
      case Some(word) => word
    }
  }
  
  /**
   * Normalise une phrase pour enlever les accents et les determinants
   * Utilise pour enlever les differences entre deux chaine de caractères
   * que l'on souhaite trouver identique
   * 
   * @param s, la chaine de caractères à normaliser
   * @return cette chaine normalisée
   */
  def nomalizeSentence(s: String): String = {
    removeDeterminant(stripAccent(s.toLowerCase))
  }
 
  /**
  * Remplace tous les caractères accentués par des caractères non accentués
  * @param s, une chaine de caractères
  * @return cette chaine sans caractère accentué
  */
 def stripAccent(s:String):String = {
    val strTemp = Normalizer.normalize(s, Normalizer.Form.NFD);
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        pattern.matcher(strTemp).replaceAll("");
 }
 
 /**
  * Enleve les determinants "le" "la" "les" "de" "des" "du" "un" "une" d'une chaine de caractère
  * 
  * @param s, une chaine de caractere
  * @return la chaine sans les determinants
  */
 def removeDeterminant(s: String): String = {
   " l(e[s]?|a) | d(e|es|u) | un[e]? ".r.replaceAllIn(s, " ")
 }
 
  /** 
   * @param prend un mot n 1
   * @param prend un mot n 2
   * @param prend la taille du mot n 1
   * @param prend la taille du mot n 2
   * @ return le nombre de différences entre le mot 1 et le mot 2
   */
  
   def distLevenshtein(mot1:String,taille1:Int,mot2:String,taille2:Int):Int={
    var cout:Int=0;
    if(taille1==0){
      return taille2
    }
    if(taille2==0){
      return taille1
    }
    
    if (mot1.charAt(taille1-1) == mot2.charAt(taille2-1)) {
      cout = 0;
    }
    else cout = 1;
    
    return min(distLevenshtein(mot1, taille1 - 1, mot2, taille2    ) + 1,
               distLevenshtein(mot1, taille1 , mot2, taille2 - 1) + 1,
               distLevenshtein(mot1, taille1 - 1, mot2, taille2 - 1) + cout);
  }
  
  /** Recupère le plus petit de 3 entiers donnees
   * @param un int
   * @param un int
   * @param un int
   * @return le plus petit
   */
   
  def min(i:Int,j:Int,k:Int):Int = { i min j min k }
}