package com.liddev.mad.util;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author Renlar <liddev.com>
 */
public class AnalysisHelpers {

  public static void checkClassVersion(String filename)
      throws IOException {
    DataInputStream in = new DataInputStream(new FileInputStream(filename));

    int magic = in.readInt();
    if (magic != 0xcafebabe) {//TODO: fix so java version's 5 and 6 are acceptable.
      System.out.println(filename + " is not a valid class!");
    }
    int minor = in.readUnsignedShort();
    int major = in.readUnsignedShort();
    System.out.println(filename + ": " + major + " . " + minor);
    in.close();
  }
}
