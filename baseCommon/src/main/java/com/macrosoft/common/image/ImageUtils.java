package com.macrosoft.common.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Locale;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

public class ImageUtils
{
  public static final int IMAGE_UNKNOWN = -1;
  public static final int IMAGE_JPEG = 0;
  public static final int IMAGE_PNG = 1;
  public static final int IMAGE_GIF = 2;

  private static int getImageTypeBySuffix(String suffix)
  {
    if (suffix == null) {
      throw new IllegalArgumentException("参数异常,suffix=" + suffix);
    }
    suffix = suffix.trim().toLowerCase();
    if ("jpeg".equals(suffix))
      return 0;
    if ("png".equals(suffix))
      return 1;
    if ("gif".equals(suffix)) {
      return 2;
    }
    return -1;
  }

  private static String getImageSuffixByType(int type)
  {
    String suffix = null;
    switch (type) {
    case 0:
      suffix = "jpeg";
      break;
    case 1:
      suffix = "png";
      break;
    case 2:
      suffix = "gif";
      break;
    default:
      throw new IllegalArgumentException("参数异常,type=" + type);
    }
    return suffix;
  }

  public static BufferedImage resizeImage(String imgName, int type, int maxWidth, int maxHeight)
  {
    try
    {
      if ((imgName == null) || ("".equals(imgName.trim()))) {
        throw new IllegalArgumentException("参数异常,imgName=" + imgName);
      }
      return resizeImage(ImageIO.read(new File(imgName)), type, maxWidth, maxHeight);
    } catch (IOException e) {
      e.printStackTrace();
    }return null;
  }

  public static BufferedImage resizeImage(BufferedImage image, int type, int maxWidth, int maxHeight)
  {
    Dimension largestDimension = new Dimension(maxWidth, maxHeight);

    int imageWidth = image.getWidth(null);
    int imageHeight = image.getHeight(null);
    float aspectRatio = imageWidth / imageHeight;
    if ((imageWidth > maxWidth) || (imageHeight > maxHeight)) {
      if (largestDimension.width / largestDimension.height > aspectRatio) {
        largestDimension.width = (int)Math.ceil(largestDimension.height * aspectRatio);
      }
      else {
        largestDimension.height = (int)Math.ceil(largestDimension.width / aspectRatio);
      }

      imageWidth = largestDimension.width;
      imageHeight = largestDimension.height;
    }
    return createHeadlessSmoothBufferedImage(image, type, imageWidth, imageHeight);
  }

  public static BufferedImage createHeadlessBufferedImage(BufferedImage image, int type, int width, int height)
  {
    if ((type == 1) && (hasAlpha(image)))
      type = 2;
    else {
      type = 1;
    }
    BufferedImage bi = new BufferedImage(width, height, type);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        bi.setRGB(x, y, image.getRGB(x * image.getWidth() / width, y * image.getHeight() / height));
      }

    }

    return bi;
  }

  public static BufferedImage createHeadlessSmoothBufferedImage(BufferedImage source, int type, int width, int height)
  {
    if ((type == 1) && (hasAlpha(source)))
      type = 2;
    else {
      type = 1;
    }
    BufferedImage dest = new BufferedImage(width, height, type);

    double scalex = width / source.getWidth();
    double scaley = height / source.getHeight();

    for (int y = 0; y < height; y++) {
      int sourcey = y * source.getHeight() / dest.getHeight();
      double ydiff = scale(y, scaley) - sourcey;
      for (int x = 0; x < width; x++) {
        int sourcex = x * source.getWidth() / dest.getWidth();
        double xdiff = scale(x, scalex) - sourcex;
        int x1 = Math.min(source.getWidth() - 1, sourcex + 1);
        int y1 = Math.min(source.getHeight() - 1, sourcey + 1);
        int rgb1 = getRGBInterpolation(source.getRGB(sourcex, sourcey), source.getRGB(x1, sourcey), xdiff);

        int rgb2 = getRGBInterpolation(source.getRGB(sourcex, y1), source.getRGB(x1, y1), xdiff);

        int rgb = getRGBInterpolation(rgb1, rgb2, ydiff);
        dest.setRGB(x, y, rgb);
      }
    }
    return dest;
  }

  private static double scale(int point, double scale) {
    if (scale == 0.0D) {
      throw new IllegalArgumentException("除数不能为0!");
    }
    return point / scale;
  }

  private static int getRGBInterpolation(int value1, int value2, double distance) {
    int alpha1 = (value1 & 0xFF000000) >>> 24;
    int red1 = (value1 & 0xFF0000) >> 16;
    int green1 = (value1 & 0xFF00) >> 8;
    int blue1 = value1 & 0xFF;
    int alpha2 = (value2 & 0xFF000000) >>> 24;
    int red2 = (value2 & 0xFF0000) >> 16;
    int green2 = (value2 & 0xFF00) >> 8;
    int blue2 = value2 & 0xFF;
    int rgb = (int)(alpha1 * (1.0D - distance) + alpha2 * distance) << 24 | (int)(red1 * (1.0D - distance) + red2 * distance) << 16 | (int)(green1 * (1.0D - distance) + green2 * distance) << 8 | (int)(blue1 * (1.0D - distance) + blue2 * distance);

    return rgb;
  }

  public static boolean hasAlpha(Image image)
  {
    try
    {
      PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
      pg.grabPixels();
      return pg.getColorModel().hasAlpha(); } catch (InterruptedException e) {
    }
    return false;
  }

  public static boolean resizeImage(String srcPath, String toPath, int width, int height)
  {
    try
    {
      if ((srcPath == null) || ("".equals(srcPath.trim()))) {
        throw new IllegalArgumentException("参数异常,srcPath=" + srcPath);
      }
      if ((toPath == null) || ("".equals(toPath.trim()))) {
        throw new IllegalArgumentException("参数异常,toPath=" + toPath);
      }
      File file = new File(srcPath);
      String filename = file.getName();
      int type = getImageTypeBySuffix(filename.substring(filename.lastIndexOf("."), filename.length()));
      BufferedImage bufferedImage = resizeImage(ImageIO.read(file), type, width, height);
      return saveImage(bufferedImage, toPath, type);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static boolean saveImage(byte[] data, String toFileName)
  {
    if (toFileName == null) {
      throw new IllegalArgumentException("参数异常,toFileName=" + toFileName);
    }
    String suffix = toFileName.substring(toFileName.lastIndexOf(".") + 1);
    InputStream inputStream = new ByteArrayInputStream(data);
    return saveImage(inputStream, suffix, toFileName);
  }

  public static boolean saveImage(BufferedImage image, String toFileName, int type)
  {
    try
    {
      return ImageIO.write(image, getImageSuffixByType(type), new File(toFileName));
    } catch (IOException e) {
      e.printStackTrace();
    }return false;
  }

  public static void saveCompressedImage(BufferedImage image, String toFileName, int type)
  {
    ImageOutputStream ios = null;
    try {
      if (type == 1) {
        throw new UnsupportedOperationException("PNG compression not implemented");
      }

      Iterator iter = ImageIO.getImageWritersByFormatName("jpg");

      ImageWriter writer = (ImageWriter)iter.next();
      ios = ImageIO.createImageOutputStream(new File(toFileName));
      writer.setOutput(ios);
      ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());

      iwparam.setCompressionMode(2);
      iwparam.setCompressionQuality(0.7F);
      writer.write(null, new IIOImage(image, null, null), iwparam);
      ios.flush();
      writer.dispose();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (ios != null)
        try {
          ios.close();
          ios = null;
        } catch (IOException e) {
          ios = null;
          e.printStackTrace();
        }
    }
  }

  public static boolean saveImage(InputStream stream, String formatName, String toPath)
  {
    try
    {
      if (toPath == null) {
        throw new IllegalArgumentException("参数异常,toPath=" + toPath);
      }
      if (formatName == null) {
        throw new IllegalArgumentException("参数异常,formatName=" + formatName);
      }
      return ImageIO.write(ImageIO.read(stream), formatName, new File(toPath));
    } catch (IOException e) {
      e.printStackTrace();
    }return false;
  }

  public static BufferedImage getBufferedImage(String imgSrc)
  {
    if (imgSrc == null) {
      throw new IllegalArgumentException("参数异常,imgSrc=" + imgSrc);
    }
    File imgFile = new File(imgSrc);
    return getBufferedImage(imgFile);
  }

  public static BufferedImage getBufferedImage(File imgFile)
  {
    try
    {
      return ImageIO.read(imgFile);
    } catch (IOException e) {
      e.printStackTrace();
    }return null;
  }

  public static boolean rotateImage(String srcPath, String toPath, int value)
  {
    if ((srcPath == null) || ("".equals(srcPath.trim()))) {
      throw new IllegalArgumentException("参数异常,srcPath=" + srcPath);
    }
    if ((toPath == null) || ("".equals(toPath.trim()))) {
      throw new IllegalArgumentException("参数异常,toPath=" + toPath);
    }
    File file = new File(srcPath);
    if (!file.exists())
      return false;
    try
    {
      String filename = file.getName();
      System.out.println(filename);
      System.out.println(filename.substring(filename.lastIndexOf(".") + 1));
      int type = getImageTypeBySuffix(filename.substring(filename.lastIndexOf(".") + 1));
      System.out.println(type);
      BufferedImage bufferedimage = rotateImage(ImageIO.read(file), value);
      saveImage(bufferedimage, toPath, type);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static BufferedImage rotateImage(BufferedImage bufferedimage, int degree)
  {
    int w = bufferedimage.getWidth();
    int h = bufferedimage.getHeight();
    int type = bufferedimage.getColorModel().getTransparency();
    BufferedImage img;
    Graphics2D graphics2d;
    (graphics2d = (img = new BufferedImage(w, h, type)).createGraphics()).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

    graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
    graphics2d.drawImage(bufferedimage, 0, 0, null);
    graphics2d.dispose();
    return img;
  }

  public static final void pressImage(String pressImg, String targetImg, int x, int y, float alpha)
  {
    try
    {
      File img = new File(targetImg);
      Image src = ImageIO.read(img);
      int wideth = src.getWidth(null);
      int height = src.getHeight(null);
      BufferedImage image = new BufferedImage(wideth, height, 1);
      Graphics2D g = image.createGraphics();
      g.drawImage(src, 0, 0, wideth, height, null);

      Image src_biao = ImageIO.read(new File(pressImg));
      int wideth_biao = src_biao.getWidth(null);
      int height_biao = src_biao.getHeight(null);
      g.setComposite(AlphaComposite.getInstance(10, alpha));

      g.drawImage(src_biao, (wideth - wideth_biao) / 2, (height - height_biao) / 2, wideth_biao, height_biao, null);

      g.dispose();
      ImageIO.write(image, "jpg", img);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void pressText(String pressText, String targetImg, String fontName, int fontStyle, Color color, int fontSize, int x, int y, float alpha)
  {
    try
    {
      File img = new File(targetImg);
      Image src = ImageIO.read(img);
      int width = src.getWidth(null);
      int height = src.getHeight(null);
      BufferedImage image = new BufferedImage(width, height, 1);

      Graphics2D g = image.createGraphics();
      g.drawImage(src, 0, 0, width, height, null);
      g.setColor(color);
      g.setFont(new Font(fontName, fontStyle, fontSize));
      g.setComposite(AlphaComposite.getInstance(10, alpha));

      g.drawString(pressText, (width - getLength(pressText) * fontSize) / 2 + x, (height - fontSize) / 2 + y);

      g.dispose();
      ImageIO.write(image, "jpg", img);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static int getLength(String text) {
    int length = 0;
    for (int i = 0; i < text.length(); i++) {
      if (new String(text.charAt(i) + "").getBytes().length > 1)
        length += 2;
      else {
        length++;
      }
    }
    return length / 2;
  }
}