package machine

import org.junit.Test
import org.junit.Assert._

class TestIntegration {

  // initialisation des objets sous test
  val m= MachineImpl
  m.reinit
  
  // tests
  @Test
  def test1_1{    
    assertEquals(List("L'adresse de Mairie de Rennes est : Place de la Mairie"),m.test(List("Où est donc la Mairie de Rennes?")))
  }
  
}