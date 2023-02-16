package main

import scala.collection.mutable.ListBuffer
import scala.swing._
import java.awt.Toolkit
import java.awt.Color
import event._
import javax.swing.text.rtf.Constants
import javax.swing.KeyStroke
import java.awt.KeyboardFocusManager
import java.awt.KeyEventDispatcher
import scala.util.Random
import sun.font.EAttribute

class GUI(response : (String) => List[String]) extends MainFrame with Interface {
  // La classe UI (User Interface) hérite de la classe MainFrame, le composant principal
  // de l'application graphique.
  // Une frame est une fenêtre qui peut contenir des données presque arbitraires
  // et s'afficher à l'écran

  // Ci dessous, on définit les attributs principaux d'une MainFrame.
  // Son titre, sa taille, et son contenu

  // Le titre de la fenêtre principale
  title = "Avatar chatbot"

  // get dimension of screen
  //-50 for adapting to taskbar
  val height = Toolkit.getDefaultToolkit.getScreenSize.getHeight.toInt - 50
  val width = Toolkit.getDefaultToolkit.getScreenSize.getWidth.toInt

  preferredSize = new Dimension(450, height)

  // Quelques champs définissant les composants
  var send = new SendBtn("Envoyer")
  var input = new TextArea
  input.lineWrap = true
  val sendZone = new BoxPanel(Orientation.Horizontal) {
      contents += new ScrollPane(input)
  }
  private var lastInput:String = null
  var hasSendBtn = false
  var previousKey = Key.Space
  sendZone.listenTo(input.keys)
  sendZone.reactions += {
    case key : KeyReleased => {
      val textTmp = input.text.trim
      if((textTmp equals "")  && hasSendBtn){
        sendZone.contents -= send
        hasSendBtn = false
        sendZone.revalidate()
        sendZone.repaint()
      }
      else if(!(textTmp equals "") && !hasSendBtn){
        sendZone.contents += send
        hasSendBtn = true
        sendZone.revalidate()
        sendZone.repaint()
      }
      if((key.key equals Key.Shift) && (previousKey == Key.Shift)){
         previousKey = Key.Space
      }
      else if((key.key equals Key.Enter) && (previousKey == Key.Space)){
        sendZone.contents -= send
        input.text = ""
      }
      if ((key.key equals Key.Up) && (lastInput != null)) {
        input.text = lastInput
        if (!(lastInput equals "") && !hasSendBtn) {
          sendZone.contents += send
        }
        sendZone.revalidate()
        sendZone.repaint()
      }
    }
    case key: KeyPressed => {
       if((key.key equals Key.Enter) && (previousKey == Key.Shift)){
         input.text += "\n"
       }
       else if ((key.key equals Key.Enter)){
         input.text = input.text.trim
         send.doClick()
         previousKey = Key.Space
       }
       else{
         previousKey = key.key
       }
    }
  }
  send.maximumSize = new Dimension(send.preferredSize.getWidth.toInt, sendZone.maximumSize.getHeight.toInt)


  // panel size
  // 400 is the width of the box panel minus 50
  sendZone.preferredSize = new Dimension(width, 70)
  sendZone.maximumSize = new Dimension(width, 70)
  sendZone.minimumSize = new Dimension(width, 70)

  val box = new BoxPanel(Orientation.Vertical) {
    contents += new Bulle("Bonjour", true).getBulle()
    listenTo(send)
    }
  box.border = Swing.LineBorder(Color.BLACK)
  box.reactions += {
      case e: ButtonClicked => {
        if (!(input.text equals "")) {
          addBulle(input.text, false)
        }
      }
  }
   
  contents = new BoxPanel(Orientation.Vertical) {
    contents += new ScrollPane(box)
    contents += sendZone
  }
  
  def addBulle(resp:String, AI:Boolean) = {
    box.contents += new Bulle(resp, AI).getBulle
    input.text = ""
    sendZone.contents -= send
    hasSendBtn = false
    sendZone.revalidate
    sendZone.repaint
    input.requestFocus
    if(!AI){
      lastInput = resp
      val l = response(resp)
      for(value <- l)
        addBulle(value, true)
    }
  }
  
  input.requestFocus
}

/**
 * A button for realizing a text-copy
 * @param lab the label of the button
 * @param from the Infield from which the text is copied
 * @param to the ResultText to which the text is copied
 */
protected class SendBtn(txt: String) extends Button { text = txt }
protected class InField extends TextField {
  background = Color.BLACK
  foreground = Color.WHITE
  text = ""
  columns = 80
  border = Swing.LineBorder(Color.GRAY)
}

class Bulle(text: String, AI: Boolean) extends BorderPanel {
  protected var bulle: BorderPanel = _
  protected var output: TextArea = _
  protected val position = if (AI) BorderPanel.Position.West else BorderPanel.Position.East
  output = new TextArea(text)
  protected val image = new ImagePanel { if(AI) imagePath = "doc/bot.png" else imagePath = "doc/account.png"}
  image.bufferedImage = ImagePanel.scaleImage(image.bufferedImage, 100, 50, Color.WHITE)
  protected val bulleBox = new BoxPanel(Orientation.Horizontal){
    if(AI) contents += image
    contents += output
    if(!AI) contents += image
    
    preferredSize = new Dimension(300,50)
    maximumSize = new Dimension(300,50)
  }
  bulle = new BorderPanel {
    add(bulleBox, position)
    preferredSize = new Dimension(350,50)
    minimumSize = new Dimension(350,50)
  }
  output.preferredSize = new Dimension(200,50)
  output.maximumSize = new Dimension(200,50)
  output.lineWrap = true
  output.editable = false
  output.border = Swing.LineBorder(new Color(0f,0f,0f,1f))
 


  def getBulle(): BorderPanel = {
    return bulle
  }

}


