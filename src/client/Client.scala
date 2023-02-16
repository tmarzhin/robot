package client

import machine._
import automaticTester.TestAvatar

object Client extends App {
  // Verifier encodage du projet sur UTF-8
	TestAvatar.check(MachineImpl)
}
