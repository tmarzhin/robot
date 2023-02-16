package machine

import org.junit.Test
import org.junit.Assert._

import main.ToleranceErreur
import main.AnalysePhraseD
import main.Courtesy
import main.BaseDeDonnees
import main.AnalyserBDD

class TestAnalysePhraseD {
  
  val bdd : BaseDeDonnees = new AnalyserBDD("doc/DonneesTest.txt", ToleranceErreur)
  val analyser: AnalysePhraseD = new AnalysePhraseD(bdd, ToleranceErreur)
  // tests

  @Test
  def test1_1 {
    assertEquals(Set(("Mairie","adM")), analyser.analyse("je veux la mairie"))
    assertEquals(Set(("Mairie","adM")), analyser.analyse("je veux l'Hôtel de ville"))

    assertEquals( Set(("Boucher", "adB")), analyser.analyse("je veux le boucher"))
    assertEquals(Set(("Boucher", "adB")), analyser.analyse("Cherche moi la boucherie"))
    
    assertEquals(Set(("Parc Michelle", "adP")), analyser.analyse("Ou est le parc le plus proche"))
    assertEquals(Set(("Parc Michelle", "adP")), analyser.analyse("Comment aller au parc michelle"))

    assertEquals(Set(("Forêt du Loup", "adF")), analyser.analyse("Je veux me balader dans la Foret"))
    assertEquals(Set(("Forêt du Loup", "adF")), analyser.analyse("je veux bruler toute la Forêt"))
  }
  
   @Test
   def test_politesse_1 {
     val expected: List[Courtesy] = List(
      Courtesy(true,""),
      Courtesy(true,""),
      Courtesy(true,""),
      Courtesy(true,""),
      Courtesy(true,""),
      Courtesy(true,""))
      
      val requests : List[String] = List("bonjour", "bonsoir", "salut", "bonjur", "bonhsoir", "salot")
      (requests zip expected).map({ case (request,exp) => assertEquals(exp, analyser.politesse(request)) })
   }
   
   @Test
   def test_politesse_2 {
     val expected: List[Courtesy] = List(
      Courtesy(true,"autre mot"),
      Courtesy(true,"quel nuit ce soir"),
      Courtesy(true,"ca va"),
      Courtesy(true,"tres cher"),
      Courtesy(true,"bonhome"),
      Courtesy(true,"pates"))
      
      val requests : List[String] = List("bonjour autre mot", "bonsoir quel nuit ce soir", "salut ca va",
                                          "bonjur tres cher", "bonhsoir bonhome", "salot des pates")
      (requests zip expected).map({ case (request,exp) => assertEquals(exp, analyser.politesse(request)) })
   }
   
   @Test
   def test_politesse_3 {
     val expected: List[Courtesy] = List(
      Courtesy(false,"bonmaljour"),
      Courtesy(false,"bsoir"),
      Courtesy(false,"tulas"),
      Courtesy(false,"bonnuitjour ca vava"),
      Courtesy(false,"bsoir msieur"),
      Courtesy(false,"tulas ac av"))
      
      val requests : List[String] = List("bonmaljour", "bsoir", "tulas", "bonnuitjour ca vava", "bsoir msieur", "tulas ac av")
      (requests zip expected).map({ case (request,exp) => assertEquals(exp, analyser.politesse(request)) })
   }


  @Test
  def test_rawAnalyse {
    val expected: List[(String, String)] = List(
      ("Mairie", "adM"),
      ("Mairie", "adM"),
      ("Boucher", "adB"),
      ("Boucher", "adB"),
      ("Parc Michelle", "adP"),
      ("Parc Michelle", "adP"),
      ("Forêt du Loup", "adF"),
      ("Forêt du Loup", "adF"))
      
      val requests : List[String] = List("mairie", "hotel ville", "boucher", "boucherie", "parc michelle", "parc", "foret loup", "foret")
      (requests zip expected).map({ case (request,exp) => assertEquals(List(exp), analyser.rawAnalyse(request)) })
  }
  
}