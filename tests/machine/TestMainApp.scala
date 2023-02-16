package machine

import org.junit.Test
import org.junit.Assert._
import main.MainApp


class TestMainApp {
  MainApp.changeBDD("doc/DonneesTest.txt")
  
  @Test
  def test_response {  
    assertEquals(List("L'adresse de Mairie est : adM"), MainApp.response("je veux la mairie"))
    assertEquals(List("L'adresse de Mairie est : adM"), MainApp.response("je veux l'Hôtel de ville"))

    assertEquals(List("L'adresse de Boucher est : adB"), MainApp.response("je veux le boucher"))
    assertEquals(List("L'adresse de Boucher est : adB"), MainApp.response("Cherche moi la boucherie"))
    
    assertEquals(List("L'adresse de Parc Michelle est : adP"), MainApp.response("Ou est le parc le plus proche"))
    assertEquals(List("L'adresse de Parc Michelle est : adP"), MainApp.response("Comment aller au parc michelle"))

    assertEquals(List("L'adresse de Forêt du Loup est : adF"), MainApp.response("Je veux me balader dans la Foret"))
    assertEquals(List("L'adresse de Forêt du Loup est : adF"), MainApp.response("je veux bruler toute la Forêt"))
  }
  
  @Test
  def test_response_2 {  
    assertEquals(List("Je ne comprends pas votre demande"), MainApp.response("je veux aller à un spectacle"))
    assertEquals(List("Je ne comprends pas votre demande"), MainApp.response("Emmène moi à Londre et à Paris"))
    assertEquals(List("Je ne comprends pas votre demande"), MainApp.response("Emmène moi à Warzazat ou à Bali"))
    assertEquals(List("Je ne comprends pas votre demande"), MainApp.response("Emmène moi à Berlin, puis Madrid"))
    
    assertEquals(List("L'adresse de Mairie est : adM"), MainApp.response("Emmène moi à Mairie et à Paris"))
    assertEquals(List("L'adresse de Boucher est : adB"), MainApp.response("Emmène moi à Boucher ou à Bali"))
    assertEquals(List("L'adresse de Parc Michelle est : adP"), MainApp.response("Emmène moi à Parc Michelle, puis Madrid"))
    
    assertEquals(List("L'adresse de Mairie est : adM"), MainApp.response("Emmène moi à Paris et à Mairie"))
    assertEquals(List("L'adresse de Boucher est : adB"), MainApp.response("Emmène moi à Bali ou à Boucher"))
    assertEquals(List("L'adresse de Parc Michelle est : adP"), MainApp.response("Emmène moi à Madrid, puis Parc Michelle"))
  }
  
  @Test
  def test_response_3 {    
    assertEquals(List("L'adresse de Mairie est : adM","L'adresse de Boucher est : adB"),
        MainApp.response("Emmène moi à la mairie et chez le boucher"))
    assertEquals(List("L'adresse de Boucher est : adB","L'adresse de Parc Michelle est : adP"), 
        MainApp.response("Emmène moi chez le boucher ou au parc"))
    assertEquals(List("L'adresse de Parc Michelle est : adP", "L'adresse de Boucher est : adB"), 
        MainApp.response("Emmène moi au Parc Michelle, boucher"))
  }
  
  @Test
  /**
   * Test Cas ou tout est bon
   */
  def test_getAnswers {
    val l: List[(String, String)] = List(
      ("Mairie", "rue de la mairie"),
      ("Hotel de ville", "rue de la mairie"),
      ("Parc Michelle", "Place du parc"),
      ("Hopital", "Boulevard du chene"))

    assertEquals(
      List(
      "L'adresse de Mairie est : rue de la mairie",
      "L'adresse de Hotel de ville est : rue de la mairie",
      "L'adresse de Parc Michelle est : Place du parc",
      "L'adresse de Hopital est : Boulevard du chene"),
      MainApp.getAnswers(l))
  }

  @Test /**
   * Test Cas ou il y a un ou plusieur problème
   */
  def test_getAnswers2 {
    val exptected: List[String] = List(
      "L'adresse de Mairie est : rue de la mairie",
      "L'adresse de Hotel de ville est : rue de la mairie",
      "L'adresse de Parc Michelle est : Place du parc");

    val l1_1: List[(String, String)] = List(
      ("Mairie", "rue de la mairie"),
      ("Hotel de ville", "rue de la mairie"),
      ("Parc Michelle", "Place du parc"),
      ("", ""))

    assertEquals(exptected, MainApp.getAnswers(l1_1)) // Erreur simple a la fin

    val l1_2: List[(String, String)] = List(
      ("Mairie", "rue de la mairie"),
      ("Hotel de ville", "rue de la mairie"),
      ("Parc Michelle", "Place du parc"),
      ("", ""),
      ("", ""))

    assertEquals(exptected, MainApp.getAnswers(l1_2)) // Erreur multiple a la fin

    val l2_1: List[(String, String)] = List(
      ("", ""),
      ("Mairie", "rue de la mairie"),
      ("Hotel de ville", "rue de la mairie"),
      ("Parc Michelle", "Place du parc"))

    assertEquals(exptected, MainApp.getAnswers(l2_1)) // Erreur simple au debut

    val l2_2: List[(String, String)] = List(
      ("", ""),
      ("", ""),
      ("Mairie", "rue de la mairie"),
      ("Hotel de ville", "rue de la mairie"),
      ("Parc Michelle", "Place du parc"))

    assertEquals(exptected, MainApp.getAnswers(l2_2)) // Erreur multiple au debut

    val l3_1: List[(String, String)] = List(
      ("Mairie", "rue de la mairie"),
      ("", ""),
      ("Hotel de ville", "rue de la mairie"),
      ("Parc Michelle", "Place du parc"))

    assertEquals(exptected, MainApp.getAnswers(l3_1)) // Erreur simple au milieu

    val l3_2: List[(String, String)] = List(
      ("Mairie", "rue de la mairie"),
      ("Hotel de ville", "rue de la mairie"),
      ("", ""),
      ("", ""),
      ("Parc Michelle", "Place du parc"))

    assertEquals(exptected, MainApp.getAnswers(l3_2)) // Erreur multiple au milieu

    val l4_1: List[(String, String)] = List()

    assertEquals(List(), MainApp.getAnswers(l4_1)) // Pas de resultats

    val l4_2: List[(String, String)] = List(("", "")) // Un resultat erreur

    assertEquals(List(), MainApp.getAnswers(l4_2))

    val l4_3: List[(String, String)] = List(("", ""), ("", "")) // Ensemble de resultat erreur

    assertEquals(List(), MainApp.getAnswers(l4_3))
  }
}