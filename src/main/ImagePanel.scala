package main

import swing._                                                                

import java.awt.image.BufferedImage                                           
import java.io.File                                                           
import javax.imageio.ImageIO                                                  
import java.awt.RenderingHints

class ImagePanel extends Panel                                                
{                                                                             
  private var _imagePath = ""                                                 
  var bufferedImage:BufferedImage = null                              

  def imagePath = _imagePath                                                  

  def imagePath_=(value:String)                                               
  {                                                                           
    _imagePath = value                                                        
    bufferedImage = ImageIO.read(new File(_imagePath))                        
  }
  
  


  override def paintComponent(g:Graphics2D) =                                 
  {                                                                           
    if (null != bufferedImage) g.drawImage(bufferedImage, 0, 0, null)         
  }                                                                           
}        

object ImagePanel                                                             
{                                                                             
  def apply() = new ImagePanel() 
  
  
  def scaleImage(img: BufferedImage, width:Int, height:Int, background:Color): BufferedImage = {
    val imgWidth = img.getWidth();
    val imgHeight = img.getHeight();
    var width1 = width
    var height1 = height
    if (imgWidth*height < imgHeight*width) {
        width1 = imgWidth*height/imgHeight;
    } else {
        height1 = imgHeight*width/imgWidth;
    }
    var newImage = new BufferedImage(width, height,
            BufferedImage.TYPE_INT_RGB);
    var g = newImage.createGraphics();
    try {
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setBackground(background);
        g.clearRect(0, 0, width, height);
        g.drawImage(img, 0, 0, width, height, null);
    } finally {
        g.dispose();
    }
    return newImage;
  }
  
}