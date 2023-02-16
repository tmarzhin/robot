package machine
import org.junit.Test 
import org.junit.Assert._

import main.ToleranceErreur
import main.BaseDeDonnees
import main.AnalyserBDD

class TestBaseDeDonnees {
  val bdd : BaseDeDonnees = new AnalyserBDD("doc/DonneesInitiales.txt", ToleranceErreur)
   
  
  // tests
  
  @Test
  def testCreationBDD{
    assertNotNull(bdd)
   }
  
  @Test
  def testSearchInDict{
    assertEquals(("Mairie de Rennes","Place de la Mairie"), bdd.searchInDict("mairie"))
    assertEquals(("Théâtre National de Bretagne","1, Rue Saint-Hélier"), bdd.searchInDict("tnb"))
    assertEquals(("Gare SNCF","19, Place de la Gare"), bdd.searchInDict("gare sncf"))
    assertEquals(("",""), bdd.searchInDict(""))
    assertEquals(("Mairie de Rennes","Place de la Mairie"), bdd.searchInDict("mairiedsg"))
    assertNotEquals(("Mairie de Rennes","Place de la Mairie"), bdd.searchInDict("gare sncf"))
   }
  
  @Test
  def testGetDico{
    assertEquals(Set("Mairie", "Théâtre National de Bretagne", "Gare", "Gare SNCF", "Théâtre de Bretagne", "Théâtre La Paillette", "Mairie de Rennes", "Hôtel de ville", "TNB", "La Paillette", "Hôtel"), bdd.getDico())
  }
}